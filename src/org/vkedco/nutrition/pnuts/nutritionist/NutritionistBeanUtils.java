package org.vkedco.nutrition.pnuts.nutritionist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.vkedco.nutrition.pnuts.CommonConstants;

public class NutritionistBeanUtils {

	private static final String NUT_LIST_STATEMENT = "select * from nutritionist where id=?";

	public static NutritionistBean retrieveNutritionistBean(String nut_id) {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		NutritionistBean nBean = new NutritionistBean();
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);

			preparedStatement = connection.prepareStatement(NUT_LIST_STATEMENT);
			preparedStatement.setString(1, nut_id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				nBean.setId(rs.getString("id"));
				nBean.setFirstName(rs.getString("first_name"));
				nBean.setLastName(rs.getString("last_name"));
				nBean.setEmail(rs.getString("email"));
			}
			return nBean;

			// Clean-up environment
			// statement.close();
			// connection.close();
		} catch (SQLException se) {
			System.err.println("NutritionistBeanUtils: Exception +" + se.toString());
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
		return nBean;
	}
}
