package edu.handong.round3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Frame {
	
	// Frames
	JFrame loginPage;
	JFrame register;
	JFrame personal;
	JFrame adminPage;
	
	// DataBase Connector
	MariaDBConnector db = new MariaDBConnector();
	
	// panels
	JPanel title = new JPanel();
	JPanel loginPanel = new JPanel();
	JPanel registerPanel = new JPanel();
	JPanel personalPanel = new JPanel();
	
	// login page labels
	JLabel loginLabel = new JLabel("Login Page");
	JLabel lid = new JLabel("ID:");
	JLabel lpw = new JLabel("PW:");
	JLabel pwCheck = new JLabel("PW Check:");
	JLabel createLabel = new JLabel("Create Account");
	JLabel noUser = new JLabel("There is no user with that username.");
	JLabel wrongPw = new JLabel("Wrong password!");
	
	// login page text fields
	JTextField ltfID = new JTextField();
	JPasswordField ltfPW = new JPasswordField();
	
	// login page buttons
	JButton b1 = new JButton("Login");
	JButton b2 = new JButton("Create Account");
	
	// personal page buttons
//	JButton exit = new JButton("Exit");
//	JButton deleteAccount = new JButton("Delete Account");
//	JButton updateInfo = new JButton("Update Information");
	
	// personal page user
	String userName;
	
	//register buttons
//	JButton back = new JButton("Login Page");
//	JButton create = new JButton("Create");
	
	// register text fields
	JTextField rtfID = new JTextField();
	JPasswordField rtfPW = new JPasswordField();
	JPasswordField rtfPW2 = new JPasswordField();
	JTextField emailTf = new JTextField();
	
	// register labels
	JLabel idCheck = new JLabel("");
	
	JLabel rid = new JLabel("ID:");
	JLabel rpw = new JLabel("PW:");
	
	String idText = "", pwText = "";
	String newId = "", newPw = "", newPwDouble = "";
	String email = "";
	
	
	public Frame() {
		// login page set-up
		loginPage = new JFrame("Login Page");
		loginPage.setBounds(100, 300, 400, 250);
		loginPage.setVisible(true);
		loginPage.getContentPane().setBackground(Color.white);
		loginPage.add(loginPanel);
		loginPage.add(title);
		loginPage.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// login panel set-up
		loginPanel.setBounds(10, 50, 380, 170);
		loginPanel.setBackground(new Color(234, 234, 234));
		loginPanel.setLayout(null);
		
		// title panel set-up
		title.setBounds(0, 0, 300, 50);
		title.setBackground(Color.white);
		
		// login label set-up
		loginLabel.setBounds(150, 6, 100, 40);
		loginLabel.setFont(new Font("Chalkboard", Font.PLAIN, 20));
		title.add(loginLabel);
		
		// id label and textfield set-up
		lid.setBounds(25, 10, 30, 15);
		lid.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		lid.setForeground(new Color(124, 124, 124));
		ltfID.setBounds(50, 10, 320, 20);
		ltfID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idText = ltfID.getText();
				System.out.println(idText);
			}
			
		});
		
		// pw label and textfield set-up
		lpw.setBounds(16, 25, 34, 50);
		lpw.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		lpw.setForeground(new Color(124, 124, 124));
		ltfPW.setBounds(50, 40, 320, 20);
		ltfPW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwText = String.valueOf(ltfPW.getPassword());
				System.out.println(pwText);
			}
			
		});
		
		JCheckBox cb = new JCheckBox("Show password");
		cb.setFont(new Font("Chalkboard", Font.PLAIN, 12));
		cb.setForeground(new Color(124, 124, 124));
		cb.setBounds(45, 60, 140, 25);
		cb.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if(cb.isSelected()) {
					ltfPW.setEchoChar((char) 0);
				}
				else {
					ltfPW.setEchoChar('•');
				}
				
			}
			
		});
		
		loginPanel.add(lid);
		loginPanel.add(ltfID);
		loginPanel.add(lpw);
		loginPanel.add(ltfPW);
		
		b1.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		b1.setBounds(38, 125, 150, 40);
		b1.setForeground(new Color(124, 124, 124));
		b1.addActionListener(new LoginListener());
		
		b2.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		b2.setBounds(198, 125, 150, 40);
		b2.setForeground(new Color(110, 110, 110));
		b2.addActionListener(new LoginListener());
		
		loginPanel.add(b1);
		loginPanel.add(b2);
		loginPanel.add(cb);
		
	}
	
	public class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			if(e.getActionCommand().equals("Login")) {
				idText = ltfID.getText();
				pwText = String.valueOf(ltfPW.getPassword());
				
				// There is a username
				if(db.is_user(idText)) {
					ResultSet rs = db.getUser(idText);
					String enteredPw = "";
					
					try {
						enteredPw = rs.getString(2);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					// password is correct 
					if(enteredPw.equals(pwText)) {
						
						// adminstrator account
						if(idText.equals("admin")) {
							idText = "";
							pwText = "";
							ltfID.setText("");
							ltfPW.setText("");
							
							openAdminPage();
							
						}
						else { // personal page
							idText = "";
							pwText = "";
							ltfID.setText("");
							ltfPW.setText("");
							
							try {
								userName = rs.getString(1);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
							
							openPersonalPage();
						}
					}
					// password is wrong
					else {
						wrongPw.setBounds(30, 55, 350, 100);
						wrongPw.setFont(new Font("Chalkboard", Font.PLAIN, 15));
						wrongPw.setForeground(Color.red);
						
						loginPanel.add(wrongPw);
						loginPanel.remove(noUser);
						loginPanel.repaint();
					}
				}
				// there is no username
				else {
					noUser.setBounds(30, 55, 350, 100);
					noUser.setFont(new Font("Chalkboard", Font.PLAIN, 15));
					noUser.setForeground(Color.red);
					
					loginPanel.add(noUser);
					loginPanel.remove(wrongPw);
					loginPanel.repaint();
				}
			}
			else if(e.getActionCommand().equals("Create Account")) {
				createRegisterFrame();
			}
		}
	}
	
	public void openAdminPage() {
		
		loginPage.setVisible(false);
		
		// administrator page set-up
		adminPage = new JFrame("Administration");
		adminPage.setLayout(null);
		adminPage.setVisible(true);
		adminPage.setBounds(100, 300, 700, 400);
		adminPage.getContentPane().setBackground(new Color(220, 220, 220));
		adminPage.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		
		// table set-up
		String[] header = {"name", "password", "email"};
		
		ResultSet rs = db.getAllUser();
		String[][] contents = new String[db.getUserNum()][3];
		String[] originalName = new String[db.getUserNum()];
		try {
			int i=0;
			while(rs.next()) {
				for(int j=0; j<3; j++) {
					contents[i][j] = rs.getString(j+1);
				}
				originalName[i] = rs.getString(1);
				i++;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		JTable table = new JTable(contents, header);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVisible(true);
		scroll.setBounds(50, 50, 550, 280);
		
		adminPage.add(scroll);
		
		// table label set-up
		JLabel userTable = new JLabel("Users");
		userTable.setBounds(300, 15, 100, 40);
		userTable.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		userTable.setForeground(Color.black);
		adminPage.add(userTable);
		
		// refresh button set-up
		JButton refresh = new JButton("Refresh");
		refresh.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		refresh.setBounds(610, 50, 80, 40);
		refresh.setForeground(new Color(124, 124, 124));
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String[] originalName = new String[db.getUserNum()];
				ResultSet rs = db.getAllUser();
				
				// get database name information
				try {
					int i=0;
					while(rs.next()) {
						originalName[i] = rs.getString(1);
						i++;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				// data update
				int size = db.getUserNum();
				for(int i=0; i<size; i++) {
					String[] data = new String[table.getColumnCount()];
					String name = originalName[i];
					for(int j=0; j<table.getColumnCount(); j++) {
						data[j] = (String) table.getValueAt(i, j);
					}
					db.changeDate(name, data[0], data[1], data[2]);
				}
			}
		}); // refresh action listener
		
		// delete button set-up
		JButton delete = new JButton("Delete");
		delete.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		delete.setBounds(610, 100, 80, 40);
		delete.setForeground(new Color(124, 124, 124));
		
		// logout button set-up
		JButton logout = new JButton("Logout");
		logout.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		logout.setBounds(610, 150, 80, 40);
		logout.setForeground(new Color(124, 124, 124));
		logout.addActionListener(new registerListener());
		
		adminPage.add(refresh);
		adminPage.add(delete);
		adminPage.add(logout);
		
	}
	
	public void openPersonalPage() {
		JButton exit = new JButton("Exit");
		JButton deleteAccount = new JButton("Delete Account");
		JButton updateInfo = new JButton("Update Information");
		
		// mainFrame for post-login
		personal = new JFrame("Welcome");
		personal.setLayout(null);
		personal.setVisible(true);
		personal.setBounds(100, 300, 400, 300);
		personal.getContentPane().setBackground(Color.white);
		personal.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		loginPage.setVisible(false);
		
		personalPanel.setBounds(20, 20, 360, 240);
		personalPanel.setBackground(Color.LIGHT_GRAY);
		personalPanel.setLayout(null);
		
		exit.setBounds(10, 190, 50, 40);
		exit.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginPage.setVisible(true);
				personal.setVisible(false);
			}
		});
		
		deleteAccount.setBounds(60, 190, 130, 40);
		deleteAccount.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		deleteAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmDelete();
			}
		});
		
		updateInfo.setBounds(190, 190, 160, 40);
		updateInfo.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		
		personalPanel.add(exit);
		personalPanel.add(deleteAccount);
		personalPanel.add(updateInfo);
		personal.add(personalPanel);
	}
	
	private void confirmDelete() {
		// frame set-up
		JFrame confirm = new JFrame("Really?");
		confirm.setLayout(null);
		confirm.setBounds(200, 400, 200, 100);
		confirm.setVisible(true);
		
		// panel set-up
		JPanel confirmPanel = new JPanel();
		confirmPanel.setBounds(0, 0, 200, 100);
		confirmPanel.setLayout(null);
		
		// label set-up
		JLabel sure = new JLabel("Are you sure?");
		sure.setBounds(50, 10, 100, 20);
		
		confirmPanel.add(sure);
		
		JButton yes = new JButton("Yes");
		yes.setBounds(50, 38, 40, 34);
		yes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				db.deleteUser(userName);
				confirm.setVisible(false);
				personal.setVisible(false);
				loginPage.setVisible(true);
				loginPanel.remove(noUser);
				loginPanel.remove(wrongPw);
			}
		});
		
		JButton no = new JButton("No");
		no.setBounds(90, 38, 40, 34);
		no.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirm.setVisible(false);
			}
		});
		
		confirmPanel.add(yes);
		confirmPanel.add(no);
		
		confirm.add(confirmPanel);
	}
	
	public class registerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			newId = rtfID.getText();
			newPw = String.valueOf(rtfPW.getPassword());
			newPwDouble = String.valueOf(rtfPW2.getPassword());
			email = emailTf.getText();
			
			if(e.getActionCommand().equals("Login Page")) {
				loginPage.setVisible(true);
				register.setVisible(false);
				idCheck.setText("Please enter your user ID.");
				newId = "";
				newPw = "";
				newPwDouble = "";
				email = "";
				loginPanel.remove(noUser);
				loginPanel.remove(wrongPw);
			}
			else if(e.getActionCommand().equals("Logout")) {
				loginPage.setVisible(true);
				adminPage.setVisible(false);
				loginPanel.remove(noUser);
				loginPanel.remove(wrongPw);
			}
			else if(e.getActionCommand().equals("Create")) {
				
//				System.out.println(newId.length());
//				System.out.println(newPw.length());
//				System.out.println(newPwDouble.length());
//				System.out.println();
//				
				if(newId.length()==0) {
					idCheck.setText("Please enter your user ID.");
//					System.out.println("id");
				}
				else if(newPw.length()==0) {
					idCheck.setText("Please enter your password.");
//					System.out.println("pw");
				}
				else if(newPwDouble.length()==0) {
					idCheck.setText("Please double check your password.");
//					System.out.println("dpw");
				}
				else if(email.length() == 0) {
					idCheck.setText("Please enter your email.");
				}
				else if(db.is_user(newId)) {
					idCheck.setText("There is another user with that username.");
//					System.out.println("exist username");
				}
				else if(!newPw.equals(newPwDouble)) {
					idCheck.setText("The passwords does not match each other.");
//					System.out.println("not match");
				}
				else {
					db.addUser(newId, newPw, email);
					idCheck.setText("Account Created!");
					
					newId = "";
					rtfID.setText("");
					newPw = "";
					rtfPW.setText("");
					newPwDouble = "";
					rtfPW2.setText("");
					email = "";
					emailTf.setText("");
				}
				
				registerPanel.repaint();
			}
		}
		
	}
	
	public void createRegisterFrame() {
		title = new JPanel();
		register = new JFrame("Create Account");
		JButton back = new JButton("Login Page");
		JButton create = new JButton("Create");
		
		// register for post-login
		register.setVisible(true);
		register.setBounds(100, 300, 400, 300);
		register.getContentPane().setBackground(Color.white);
		register.add(registerPanel);
		register.add(title);
		register.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		loginPage.setVisible(false);
		
		// register panel set-up
		registerPanel.setBounds(10, 50, 380, 220);
		registerPanel.setBackground(new Color(234, 234, 234));
		registerPanel.setLayout(null);
		
		// title panel set-up
		title.setBounds(0, 0, 300, 50);
		title.setBackground(Color.white);
		title.setLayout(null);
		
		// create account label set-up
		createLabel.setBounds(130, 3, 170, 40);
		createLabel.setFont(new Font("Chalkboard", Font.PLAIN, 20));
		title.add(createLabel);
		
		// new id label and textfield set-up
		rid.setBounds(19, 10, 30, 15);
		rid.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		rid.setForeground(new Color(124, 124, 124));
		rtfID.setBounds(44, 10, 326, 20);
		rtfID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newId = rtfID.getText();
				System.out.println(newId);
			}
		});
		
		// new pw label and textfield set-up
		rpw.setBounds(10, 25, 34, 50);
		rpw.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		rpw.setForeground(new Color(124, 124, 124));
		rtfPW.setBounds(44, 40, 326, 20);
		rtfPW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newPw = String.valueOf(rtfPW.getPassword());
				System.out.println(newPw);
			}
			
		});
		
		// password check label set-up
		pwCheck.setBounds(10, 55, 90, 50);
		pwCheck.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		pwCheck.setForeground(new Color(124, 124, 124));
		rtfPW2.setBounds(100, 70, 270, 20);
		rtfPW2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newPwDouble = String.valueOf(rtfPW2.getPassword());
				System.out.println(newPwDouble);
			}
			
		});
		
		JCheckBox cb = new JCheckBox("Show password");
		cb.setFont(new Font("Chalkboard", Font.PLAIN, 12));
		cb.setForeground(new Color(124, 124, 124));
		cb.setBounds(45, 90, 140, 25);
		cb.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if(cb.isSelected()) {
					rtfPW.setEchoChar((char) 0);
					rtfPW2.setEchoChar((char) 0);
				}
				else {
					rtfPW.setEchoChar('•');
					rtfPW2.setEchoChar('•');
				}
				
			}
			
		});
		
		// email label and text field
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setBounds(10, 105, 48, 50);
		emailLabel.setFont(new Font("Chalkboard", Font.PLAIN, 18));
		emailLabel.setForeground(new Color(124, 124, 124));
		emailTf.setBounds(58, 120, 312, 20);
		emailTf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				email = String.valueOf(emailTf.getText());
				System.out.println(email);
			}
			
		});
		
		// idCheck set-up
		idCheck.setForeground(new Color(138, 165, 198));
		idCheck.setBounds(40, 140, 300, 40);
		idCheck.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		
		back.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		back.setBounds(38, 175, 150, 40);
		back.setForeground(new Color(124, 124, 124));
		back.addActionListener(new registerListener());
		
		create.setFont(new Font("Chalkboard", Font.PLAIN, 15));
		create.setBounds(198, 175, 150, 40);
		create.setForeground(new Color(110, 110, 110));
		create.addActionListener(new registerListener());
		
		registerPanel.add(rid);
		registerPanel.add(rtfID);
		registerPanel.add(idCheck);
		registerPanel.add(rpw);
		registerPanel.add(rtfPW);
		registerPanel.add(pwCheck);
		registerPanel.add(rtfPW2);
		registerPanel.add(cb);
		registerPanel.add(back);
		registerPanel.add(create);
		registerPanel.add(emailLabel);
		registerPanel.add(emailTf);
	}
}




