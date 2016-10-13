package org.vkedco.nutrition.pnuts.client;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import java.sql.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;

/**
 * Servlet implementation class ClientMealDataUpload Verifies client credentials
 * Uploads both before meal data and after meal data to the server directory
 * Inserts the records into two tables, pending_eier_before_meal and
 * pending_eier_after_meal
 * 
 */
@WebServlet("/ClientMealDataUpload")
public class ClientMealDataUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String mUploadFilePath = "";
	private static final String INSERT_INTO_EIER_BEFORE_MEAL_STATEMENT_PATTERN = "insert into pending_eier_before_meal (client_id, vid_before_meal, time) values (?, ?, ?)";
	private static final String INSERT_INTO_EIER_AFTER_MEAL_STATEMENT_PATTERN = "insert into pending_eier_after_meal (client_id, vid_after_meal, time) values (?, ?, ?)";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientMealDataUpload() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) if he access this servlet illegally, the request will be
	 *      sent using GET method, so handling that request by sending him to
	 *      the login page.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher(CommonConstants.ILLEGAL_CLIENT_ACCESS)
				.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		ClientBean cb = (ClientBean) session
				.getAttribute(CommonConstants.CLIENT_BEAN_KEY);
		/*
		 * Checking if there is a ClientBean in the session if there is no
		 * clientbean object, i.e. he did not login or if client is not
		 * authorized sending him to the login page
		 */
		if (cb == null || !CommonValidator.validateClient(cb)) {
			request.getRequestDispatcher(CommonConstants.ILLEGAL_CLIENT_ACCESS)
					.forward(request, response);
		}
		// reading filepaths, and inserting the records in appropriate tables.
		// beforeMealItem holds path of before meal data
		// afterMealItem holds path of after meal data
		else {
			if (ServletFileUpload.isMultipartContent(request)) {
				try {
					List<FileItem> multiparts = new ServletFileUpload(
							new DiskFileItemFactory()).parseRequest(request);
					Calendar calendar = Calendar.getInstance();
					String fileName = new java.sql.Timestamp(
							calendar.getTimeInMillis()).toString();
					FileItem beforeMealItem = multiparts.get(0);
					if (!beforeMealItem.isFormField()
							&& !beforeMealItem.getName().equals("")
							&& !beforeMealItem.getName().isEmpty()) {
						fileName = cb.getId()
								+ "_"
								+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
										.format(new Date()) + "_"
								+ new File(beforeMealItem.getName()).getName();
						mUploadFilePath = CommonConstants.CLIENT_DATA_DIR
								+ File.separator + fileName;
						System.err
								.println("ClientMealDataUpload1: mUploadFilePath:"
										+ mUploadFilePath);
						beforeMealItem.write(new File(mUploadFilePath));
						insertBeforeMealData(fileName, request, response);
						request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1,
								"File uploaded successfully");
					}
					FileItem afterMealItem = multiparts.get(1);
					if (!afterMealItem.isFormField()
							&& !afterMealItem.getName().equals("")
							&& !afterMealItem.getName().isEmpty()) {
						fileName = cb.getId()
								+ "_"
								+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
										.format(new Date()) + "_"
								+ new File(afterMealItem.getName()).getName();
						mUploadFilePath = CommonConstants.CLIENT_DATA_DIR
								+ File.separator + fileName;
						afterMealItem.write(new File(mUploadFilePath));
						insertAfterMealData(fileName, request, response);
						request.setAttribute(CommonConstants.UPLOAD_MSG_KEY2,
								"File uploaded successfully");
					} else {
						request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1,
								"File failed to upload ");
						request.setAttribute(CommonConstants.UPLOAD_MSG_KEY2,
								"File failed to upload ");
					}
				} catch (Exception ex) {
					request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1,
							"File failed to upload ");
					request.setAttribute(CommonConstants.UPLOAD_MSG_KEY2,
							"File failed to upload ");
					System.err.println(mUploadFilePath + " exception: "
							+ ex.toString() + "\nstack trace"+ ex.getStackTrace());
				} finally {
					session.invalidate();
				}

				request.getRequestDispatcher(
						"/WEB-INF/Message/FileUploadResponse.jsp").forward(
						request, response);
			}

		}
	}

	/*
	 * Inserts after meal data upload record into pending_eier_after_meal table.
	 */
	private void insertAfterMealData(String fileName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		synchronized (this) {
			// forwardRequest returns true if one of the ClientBean's fields
			// is missing.

			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				// Register JDBC driver
				Class.forName(CommonConstants.JDBC_DRIVER);
				// Open a connection
				connection = DriverManager.getConnection(
						CommonConstants.DB_URL, CommonConstants.DB_USER,
						CommonConstants.DB_PWD);
				connection.setAutoCommit(false);

				// Execute SQL query
				// statement = connection.createStatement();
				preparedStatement = connection
						.prepareStatement(INSERT_INTO_EIER_AFTER_MEAL_STATEMENT_PATTERN);
				HttpSession session = request.getSession();
				ClientBean cb = (ClientBean) session
						.getAttribute(CommonConstants.CLIENT_BEAN_KEY);
				preparedStatement.setString(1, cb.getId());
				preparedStatement.setString(2, fileName);
				Calendar calendar = Calendar.getInstance();
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						calendar.getTimeInMillis()));
				// sends the statement to the database server
				// I need to check if the id already exists
				int row = preparedStatement.executeUpdate();
				if (row > 0) {
					connection.commit();
					/*
					 * RequestDispatcher dispatcher = request
					 * .getRequestDispatcher(CommonConstants.THANK_YOU_PAGE);
					 * dispatcher.forward(request, response);
					 */
				} else {
					/*
					 * RequestDispatcher dispatcher = request
					 * .getRequestDispatcher
					 * (CommonConstants.REGISTRATION_FAILURE_PAGE);
					 * dispatcher.forward(request, response);
					 */
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

	}

	/*
	 * Inserts before meal data upload record into pending_eier_before_meal
	 * table.
	 */
	private void insertBeforeMealData(String fileName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		synchronized (this) {
			// forwardRequest returns true if one of the ClientBean's fields
			// is missing.

			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				// Register JDBC driver
				Class.forName(CommonConstants.JDBC_DRIVER);
				// Open a connection
				connection = DriverManager.getConnection(
						CommonConstants.DB_URL, CommonConstants.DB_USER,
						CommonConstants.DB_PWD);
				connection.setAutoCommit(false);

				// Execute SQL query
				// statement = connection.createStatement();
				preparedStatement = connection
						.prepareStatement(INSERT_INTO_EIER_BEFORE_MEAL_STATEMENT_PATTERN);
				HttpSession session = request.getSession();
				ClientBean cb = (ClientBean) session
						.getAttribute(CommonConstants.CLIENT_BEAN_KEY);
				preparedStatement.setString(1, cb.getId());
				preparedStatement.setString(2, fileName);
				Calendar calendar = Calendar.getInstance();
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						calendar.getTimeInMillis()));
				// sends the statement to the database server
				// I need to check if the id already exists
				int row = preparedStatement.executeUpdate();
				if (row > 0) {
					connection.commit();
					/*
					 * RequestDispatcher dispatcher = request
					 * .getRequestDispatcher(CommonConstants.THANK_YOU_PAGE);
					 * dispatcher.forward(request, response);
					 */
				} else {
					/*
					 * RequestDispatcher dispatcher = request
					 * .getRequestDispatcher
					 * (CommonConstants.REGISTRATION_FAILURE_PAGE);
					 * dispatcher.forward(request, response);
					 */
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
	}
}