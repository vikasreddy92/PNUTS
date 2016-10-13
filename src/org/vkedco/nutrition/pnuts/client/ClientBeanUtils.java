package org.vkedco.nutrition.pnuts.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.vkedco.nutrition.pnuts.CommonConstants;

public class ClientBeanUtils {
	private static final String CLIENT_LIST_STATEMENT = "select * from client where id=?";
	public static ClientBean retrieveClientBean(String client_id) {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		ClientBean cBean = new ClientBean();
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);

			preparedStatement = connection.prepareStatement(CLIENT_LIST_STATEMENT);
			preparedStatement.setString(1, client_id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				cBean.setId(rs.getString("id"));
				cBean.setEmail(rs.getString("email"));
			}
			return cBean;

			// Clean-up environment
			// statement.close();
			// connection.close();
		} catch (SQLException se) {
			System.err.println("ClientBeanUtils: Exception +" + se.toString());
		} catch (ClassNotFoundException e) {
		} finally {
			// finally block used to close resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se) {
			} // nothing we can do
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
			}
		} // end try
		return cBean;
	}
}
