package org.vkedco.nutrition.pnuts;

/*
 ************************************************************
 * CommonValidator.java
 * Author: Vladimir Kulyukin
 * 
 * This is the class where we should put all validation
 * methods. Right now it has two methods: one for validating
 * clients, the other for validating nutritionists.
 ************************************************************
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.vkedco.nutrition.pnuts.client.ClientBean;
import org.vkedco.nutrition.pnuts.coordinator.ProgramCoordinatorBean;
import org.vkedco.nutrition.pnuts.nutritionist.EIEBean;
import org.vkedco.nutrition.pnuts.nutritionist.NutritionistBean;

public class CommonValidator {

	private static final String NUT_ID_AND_PWD_STATEMENT_PATTERN = "select id from nutritionist where id=? and password=?";
	private static final String CLIENT_ID_AND_PWD_STATEMENT_PATTERN = "select id from client where id=? and password=?";
	private static final String PROG_COORD_ID_AND_PWD_STATEMENT_PATTERN = "select id from nutritionist where id=? and password=? and role='coordinator'";
	private static String EIE_REQ_ID_STATEMENT = "select "
			+ "pending_eier_before_meal.req_id from pending_eier_before_meal "
			+ "inner join pending_eier_after_meal " + "on "
			+ "pending_eier_before_meal.req_id = "
			+ "pending_eier_after_meal.req_id " + "and "
			+ "pending_eier_before_meal.req_id = ?";

	public static boolean validateClient(ClientBean cb) {
		if (cb == null)
			return false;

		if (cb.getId() == null || cb.getPassword() == null)
			return false;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(CLIENT_ID_AND_PWD_STATEMENT_PATTERN);
			preparedStatement.setString(1, cb.getId());
			preparedStatement.setString(2, cb.getPassword());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) {
			// do nothing
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		} // end try
		return false;
	}

	public static boolean validateNutritionist(NutritionistBean nb) {
		if (nb == null)
			return false;

		if (nb.getId() == null || nb.getPassword() == null)
			return false;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(NUT_ID_AND_PWD_STATEMENT_PATTERN);
			preparedStatement.setString(1, nb.getId());
			preparedStatement.setString(2, nb.getPassword());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) {
			// do nothing
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		} // end try
		return false;
	}

	public static boolean validateEIE(EIEBean eb) {

		if (eb == null) {
//			System.err.println("CommonValidator: eb is null");
			return false;
		}

		if (!eb.isValid()) {
//			System.err.println("CommonValidator: eb is not valid");
			return false;
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(EIE_REQ_ID_STATEMENT);
			preparedStatement.setInt(1, eb.getReqId());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
//				System.err.println("CommonValidator: Resultset is not empty "+rs.getFetchSize());
				return true;
			} else {
//				System.err.println("CommonValidator: Resultset is empty "+rs.getFetchSize());
				return false;
			}
		} catch (SQLException se) {
			// do nothing
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		} // end try
		return false;
	}
	
	public static boolean validateProgramCoordinator(ProgramCoordinatorBean pcBean){
		if (pcBean == null)
			return false;

		if (pcBean.getId() == null || pcBean.getPassword() == null)
			return false;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(PROG_COORD_ID_AND_PWD_STATEMENT_PATTERN);
			preparedStatement.setString(1, pcBean.getId());
			preparedStatement.setString(2, pcBean.getPassword());
//			System.err.println("PC Id: " + pcBean.getId() + "\nPC pwd: " + pcBean.getPassword());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
//				System.err.println("Returning true");
				return true;
			} else {
//				System.err.println("Returning true");
				return false;
			}
		} catch (SQLException se) {
			// do nothing
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		} // end try
		return false;
	}
}