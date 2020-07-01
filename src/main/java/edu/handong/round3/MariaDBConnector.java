package edu.handong.round3;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MariaDBConnector {
	String driver = "org.mariadb.jdbc.Driver";
	Connection con;
	Statement stmt;
	ResultSet rs;

	public MariaDBConnector() {
		try {
			Class.forName(driver); // Drive 클래스 로드
			con = DriverManager.getConnection( // connection 객체 생성
					"jdbc:mariadb://172.17.206.32:3306/userdb", // url
					"haewi", // user
					"1234"); // password
			stmt = con.createStatement();
			if( con == null ) {
				System.out.println("실패");
			}

		} catch (ClassNotFoundException e) { 
			System.out.println("드라이버 로드 실패");
		} catch (SQLException e) {
			System.out.println("DB 접속 실패");
			e.printStackTrace();
		}
		
	}
	
	// adding user to database, if another user is using the username, it returns false. Else, true
	public boolean addUser(String name, String pw, String email) {
		
		String sql = "INSERT INTO userInfo(name, password, email) VALUES";
		
		try {
			sql += "('" + new String(name.getBytes(), "ISO-8859-1") +  "','" + new String(pw.getBytes(), "ISO-8859-1") +  "','" + new String(email.getBytes(), "ISO-8859-1") + "')";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	// returns the ResultSet with the user info
	public ResultSet getUser(String name) {
		String sql2 = "select * from userInfo where name='" + name + "'";
		
		try {
			rs = stmt.executeQuery(sql2);
			rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	// returns the ResultSet with the user info
	public ResultSet getAllUser() {
		String sql2 = "select * from userInfo";

		try {
			rs = stmt.executeQuery(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	// returns the ResultSet with the user info
	public int getUserNum() {
		String sql2 = "select * from userInfo";
		
		int count=0;
		try {
			rs = stmt.executeQuery(sql2);
			while(rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}
	
	// returns true when there is a user with the name
	public boolean is_user(String name) {
		String sql2 = "select * from userInfo where name='" + name + "'";
		
		ResultSet r = null;
		
		try {
			r = stmt.executeQuery(sql2);
			r.next();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			r.getString(1);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	// changes the data
	public void changeData(String name, String newName, String newPw, String newEmail) {
//		System.out.println("  name: " + name + " new name: " + newName + " new password: " + newPw + " new email: " + newEmail);
		String sql1 = "update userInfo set password='" + newPw + "' where name='" + name + "'";
		String sql2 = "update userInfo set email='" + newEmail + "' where name='" + name + "'";
		String sql3 = "update userInfo set name='" + newName + "' where email='" + newEmail + "'";
		
//		System.out.println(sql1 + "\n");
		try {
			rs = stmt.executeQuery(sql1);
			rs = stmt.executeQuery(sql2);
			rs = stmt.executeQuery(sql3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// deletes the user data
	public void deleteUser(String name) {
		String sql = "DELETE FROM userInfo WHERE name = '" + name + "'";
		
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
