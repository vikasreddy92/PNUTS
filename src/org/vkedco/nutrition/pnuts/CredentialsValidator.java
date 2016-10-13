//package org.vkedco.nutrition.pnuts;
//
///*
// ************************************************************
// * CredentialsValidator.java
// * Author: Vladimir Kulyukin
// * 
// * This is the class where we should put all validation
// * methods. Right now it has two methods: one for validating
// * clients, the other for validating nutritionists.
// ************************************************************
// */
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import org.vkedco.nutrition.pnuts.client.ClientBean;
//import org.vkedco.nutrition.pnuts.nutritionist.NutritionistBean;
//
//
//public class CredentialsValidator {
//
//	private static final String NUT_ID_AND_PWD_STATEMENT_PATTERN = 
//			"select id from nutritionist where id=? and password=?";
//	private static final String CLIENT_ID_AND_PWD_STATEMENT_PATTERN = 
//			"select id from client where id=? and password=?";
//	
//	public static boolean validateClient(ClientBean cb) {
//		if ( cb.getId() == null || cb.getPassword() == null ) return false;
//		
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			Class.forName(CommonConstants.JDBC_DRIVER);
//			connection = DriverManager
//					.getConnection(CommonConstants.DB_URL, 
//								   CommonConstants.DB_USER, 
//								   CommonConstants.DB_PWD);
//			preparedStatement = connection
//					.prepareStatement(CLIENT_ID_AND_PWD_STATEMENT_PATTERN);
//			preparedStatement.setString(1, cb.getId());
//			preparedStatement.setString(2, cb.getPassword());
//			ResultSet rs = preparedStatement.executeQuery();
//			if ( rs.next() ) {
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
//		catch (SQLException se) {
//			// do nothing
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			// finally block used to close resources
//			try {
//				if (preparedStatement != null)
//					preparedStatement.close();
//			} catch (SQLException se2) {
//			} // nothing we can do
//			try {
//				if (connection != null)
//					connection.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}// end finally try
//		} // end try
//		return false;
//	}
//	
//	public static boolean validateNutritionist(NutritionistBean nb) {
//		if ( nb.getId() == null || nb.getPassword() == null ) return false;
//		
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		try {
//			Class.forName(CommonConstants.JDBC_DRIVER);
//			connection = DriverManager
//					.getConnection(CommonConstants.DB_URL, 
//								   CommonConstants.DB_USER, 
//								   CommonConstants.DB_PWD);
//			preparedStatement = connection
//					.prepareStatement(NUT_ID_AND_PWD_STATEMENT_PATTERN);
//			preparedStatement.setString(1, nb.getId());
//			preparedStatement.setString(2, nb.getPassword());
//			
////			System.err.println("In validator\nprepared statement is: "+ preparedStatement.toString());
//			
//			
//			ResultSet rs = preparedStatement.executeQuery();
//			if ( rs.next() ) {
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
//		catch (SQLException se) {
//			// do nothing
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			// finally block used to close resources
//			try {
//				if (preparedStatement != null)
//					preparedStatement.close();
//			} catch (SQLException se2) {
//			} // nothing we can do
//			try {
//				if (connection != null)
//					connection.close();
//			} catch (SQLException se) {
//				se.printStackTrace();
//			}// end finally try
//		} // end try
//		return false;
//	}
//	
//}
