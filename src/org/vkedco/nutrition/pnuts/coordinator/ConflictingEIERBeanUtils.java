package org.vkedco.nutrition.pnuts.coordinator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.EIEstimationTableConstants;
import org.vkedco.nutrition.pnuts.HtmlUtils;
import org.vkedco.nutrition.pnuts.nutritionist.EIEBean;

public class ConflictingEIERBeanUtils {
	private static final String EIE_SELECT_STATEMENT = "select req_id, nut_id, eie_file, cals, time from ei_estimation where req_id=?";
	private static String CONFLICT_EIER_LIST = "select * from conflict_resolution_table";
	private static String INSERT_CONFLICT_EIER_PATTERN = "insert into conflict_resolution_table (req_id, client_id, vid_before_meal, before_meal_time, vid_after_meal, after_meal_time, status) values (? ,?, ?, ?, ?, ?, ?)";

	private static int NUM_COLS = 7;

	public static void insertIntoConflictResolutionTable(
			ConflictingEIERBean conflictingEIERBean) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			connection.setAutoCommit(false);

			// Execute SQL query
			// statement = connection.createStatement();
			preparedStatement = connection
					.prepareStatement(INSERT_CONFLICT_EIER_PATTERN);
			preparedStatement.setInt(1, conflictingEIERBean.getReqId());
			preparedStatement.setString(2, conflictingEIERBean.getClientId());
			preparedStatement.setString(3,
					conflictingEIERBean.getBeforeMealFilePath());
			preparedStatement.setTimestamp(4,
					conflictingEIERBean.getBeforeMealTimestamp());
			preparedStatement.setString(5,
					conflictingEIERBean.getAfterMealFilePath());
			preparedStatement.setTimestamp(6,
					conflictingEIERBean.getAfterMealTimestamp());
			preparedStatement.setString(7, "not resolved");

			int row = preparedStatement.executeUpdate();
			if (row > 0) {
				connection.commit();
			} else {
				Exception ex = new Exception("Database failure");
				throw ex;
			}

			// Clean-up environment
			// statement.close();
			// connection.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			if (connection != null)
				try {
					connection.rollback();
				} catch (SQLException e) {
					// e.printStackTrace();
					throw e;
				}
			throw se;
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			throw e;
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

	}

	public static String retrieveConflictEIERList() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String result = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(CONFLICT_EIER_LIST);
			StringBuffer sb = new StringBuffer();
			// String req_id = null;
			// String client_id = null;
			if (rs.first()) {
				while (!rs.isAfterLast()) {
					sb.append(HtmlUtils.beginTR());
					for (int i = 1; i <= NUM_COLS; i++) {
						if (i == 1) {
							// System.err.println("ConflictingEIERBeanUtils.retrieveConflictEIERList: i = "+
							// i + " , rs.getString("+i+") = "+rs.getString(i));
							sb.append(HtmlUtils.TD(HtmlUtils
									.AHREF(CommonConstants.CONFLICTING_ESTIMATIONS_SERVLET
											+ "?req_id=" + rs.getString(i),
											rs.getString(i))));
						} else if (i == 2) {
							// client_id = rs.getString(i);
							// System.err.println("ConflictingEIERBeanUtils.retrieveConflictEIERList: i = "+
							// i + " , rs.getString("+i+") = "+rs.getString(i));

							sb.append(HtmlUtils.TD(HtmlUtils.AHREF(
									CommonConstants.CLIENT_SERVLET
											+ "?client_id=" + rs.getString(i),
									rs.getString(i))));
						} else if (i == 3 || i == 5) {
							// System.err.println("ConflictingEIERBeanUtils.retrieveConflictEIERList: i = "+
							// i + " , rs.getString("+i+") = "+rs.getString(i));

							sb.append(HtmlUtils.TD(HtmlUtils.AHREF(
									CommonConstants.DOWNLOAD_FILE_SERVLET
									+"?downloadKey="
									+ CommonConstants.DOWNLOAD_CLIENT
											+ "?fileName=" + rs.getString(i),
									CommonConstants.DATA_LINK_NAME)));
						} else {
							// System.err.println("ConflictingEIERBeanUtils.retrieveConflictEIERList: i = "+
							// i + " , rs.getString("+i+") = "+rs.getString(i));
							sb.append(HtmlUtils.TD(rs.getString(i)));
						}
					}
					sb.append(HtmlUtils.endTR());
					rs.next();
				}
			}
			return sb.toString();

		} catch (SQLException se) {
			result = se.toString();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se) {
				result = "finally " + se.toString();
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				result = "finally " + se.toString();
			}
			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				result = "finally " + ex.toString();
			}
		}
		return result;
	}

	/*
	 * Retrieves EIE beans from conflict_resolution_table which have same req_id
	 */
	public static ArrayList<EIEBean> retrieveConflictingEstimationsFromDB(
			int req_id) {

		ArrayList<EIEBean> eieBeans = new ArrayList<EIEBean>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(EIE_SELECT_STATEMENT);
			preparedStatement.setInt(1, req_id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				int reqId = rs
						.getInt(EIEstimationTableConstants.REQ_ID_COL_NUM);
				String nutId = rs
						.getString(EIEstimationTableConstants.NUT_ID_COL_NUM);
				String eieFile = rs
						.getString(EIEstimationTableConstants.EIE_FILE_COL_NUM);
				int cals = rs.getInt(EIEstimationTableConstants.CALS_COL_NUM);
				java.sql.Timestamp ts = rs
						.getTimestamp(EIEstimationTableConstants.TIME_COL_NUM);

				EIEBean eb = new EIEBean();
				eb.setReqId(reqId);
				eb.setNutId(nutId);
				eb.setFileName(eieFile);
				eb.setCals(cals);
				eb.setTimestamp(ts);
				eieBeans.add(eb);
			}

			return eieBeans;
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
		return eieBeans;
	}
}
