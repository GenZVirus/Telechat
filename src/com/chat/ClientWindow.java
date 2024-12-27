package com.chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JPanel contentClient;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;	
	private Thread run, listen;
	private Client client;
	
	private boolean running = false;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOnlineUsers;
	private JMenuItem mntmExit;
	
	private OnlineUsers users;
	
	public ClientWindow(String name, String address, int port) {
		setTitle("Client");
		client = new Client(name, address, port);
		boolean connect = client.openConnection(address);
		if(!connect) {
			System.err.println("Connection failed!");
			console("Connection failed!");
		}
		CreateWindow();
		console("Attempting a connection to " + address + ":" + port + ", user: " + name);
		String connection ="/c/" + name + "/e/";
		client.send(connection.getBytes());
		users = new OnlineUsers();
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	private void CreateWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880, 550);
		setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOnlineUsers = new JMenuItem("Online Users");
		mntmOnlineUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				users.setVisible(true);
			}
		});
		mnFile.add(mntmOnlineUsers);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.close();
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		contentClient = new JPanel();
		contentClient.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentClient);
		
		GridBagLayout gbl_contentClient = new GridBagLayout();
		gbl_contentClient.columnWidths = new int[]{25, 810, 40, 5}; //SUM =  880
		gbl_contentClient.rowHeights = new int[]{30, 480, 40}; //SUM = 550
		contentClient.setLayout(gbl_contentClient);
		
		history = new JTextArea();
		history.setEditable(false);
		JScrollPane scroll = new JScrollPane(history);
		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 5, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		scrollConstraints.weightx = 1;
		scrollConstraints.weighty = 1;
		contentClient.add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(), true);
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.gridwidth = 2;
		gbc_txtMessage.insets = new Insets(0, 5, 5, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.weightx = 1;
		gbc_txtMessage.weighty = 0;
		contentClient.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText(), true);
				txtMessage.requestFocusInWindow();
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.weightx = 0;
		gbc_btnSend.weighty = 0;
		contentClient.add(btnSend, gbc_btnSend);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.close();
			}
		});

		setVisible(true);
		txtMessage.requestFocusInWindow();
	}
	
	public void run() {
		listen();
	}
	
	public void send(String message, boolean text) {
		if (message.equals("")) return;
		if(text) {
			message = client.getName() + "|" + message;
			message ="/m/" + message + "/e/";
			txtMessage.setText("");
			txtMessage.requestFocusInWindow();
		}
		client.send(message.getBytes());
	}
	
	public void listen() {
		listen = new Thread() {
			public void run() {
				while(running) {
					String message = client.receive();
					if(message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						console("Successfully connected to Server! ID: " + client.getID());
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
					} else if(message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					} else if(message.startsWith("/u/")) {
						String[] u = message.split("/u/|/n/|/e/");
						users.update(Arrays.copyOfRange(u, 1, u.length - 1));
					}

				}
			}
		};
		listen.start();
	}

	public void console(String message) {
		history.append(message + "\n");
		history.setCaretPosition(history.getDocument().getLength());
	}
}
