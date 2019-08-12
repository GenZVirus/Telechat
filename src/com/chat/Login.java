package com.chat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JTextField txtAddress;
	private JLabel lblIpAddress;
	private JTextField txtPort;
	private JLabel lblPort;
	private JLabel lblEgIPAddress;
	private JLabel lblEgPort;

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUsername = new JTextField();
		txtUsername.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtUsername.getText();
					String address = txtAddress.getText();
					int port =Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		txtUsername.setBounds(79, 75, 136, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(113, 50, 68, 14);
		contentPane.add(lblUsername);
		
		txtAddress = new JTextField();
		txtAddress.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtUsername.getText();
					String address = txtAddress.getText();
					int port =Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		txtAddress.setBounds(79, 145, 136, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblIpAddress = new JLabel("IP Adress:");
		lblIpAddress.setBounds(118, 120, 58, 14);
		contentPane.add(lblIpAddress);
		
		txtPort = new JTextField();
		txtPort.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtUsername.getText();
					String address = txtAddress.getText();
					int port =Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		txtPort.setColumns(10);
		txtPort.setBounds(79, 233, 136, 20);
		contentPane.add(txtPort);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(130, 208, 34, 14);
		contentPane.add(lblPort);
		
		lblEgIPAddress = new JLabel("(eg. 192.168.0.2)");
		lblEgIPAddress.setBounds(96, 171, 102, 14);
		contentPane.add(lblEgIPAddress);
		
		lblEgPort = new JLabel("(eg. 8192)");
		lblEgPort.setBounds(118, 256, 58, 14);
		contentPane.add(lblEgPort);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtUsername.getText();
				String address = txtAddress.getText();
				int port =Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}

		});
		btnLogin.setBounds(102, 309, 89, 23);
		contentPane.add(btnLogin);
		
	}
	
	private void login(String name, String address, int port) {
		dispose();
		new ClientWindow(name, address, port);
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
