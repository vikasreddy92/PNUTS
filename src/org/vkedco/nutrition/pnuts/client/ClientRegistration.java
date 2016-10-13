package org.vkedco.nutrition.pnuts.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vkedco.nutrition.pnuts.CommonConstants;

/**
 * *****************************************************************
 * Servlet implementation class ClientRegistration
 * Author: Vladimir Kulyukin
 * 
 * This servlet is called by WebConent/ClientRegistration.html.
 * It creates a ClientBean object implemented in 
 * org.vkedco.nutrition.pnuts.client.ClientBean.java. 
 * The method insertClient inserts a record into the client table
 * of the pnuts MySQL database. The database design is as follows:
 * 
 * mysql> describe client;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| id           | varchar(10)  | NO   | PRI | NULL    |       |
| email        | varchar(100) | NO   |     | NULL    |       |
| password     | varchar(100) | NO   |     | NULL    |       |
| tip_day      | varchar(15)  | NO   |     | NULL    |       |
| tip_time     | varchar(15)  | NO   |     | NULL    |       |
| summary_time | varchar(15)  | NO   |     | NULL    |       |
+--------------+--------------+------+-----+---------+-------+
*
* When the registration is successful, the request is forwarded to
* the JSP page WebContent/WEB-INF/Message/ThankYou.jsp.
* 
* When the registration is unsuccessful, the request is forwarded to
* the JSP page WebContent/WEB-INF/Message/FileUploadResponse.jsp.
*******************************************************************
 */

@WebServlet("/ClientRegistration")
public class ClientRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_EMAIL = "client_email";
	private static final String CLIENT_PWD = "client_pwd";
	private static final String CLIENT_CONFIRMED_PWD = "client_confirmed_pwd";
	private static final String NUTRITION_TIP_DAY = "nutrition_tip_day";
	private static final String NUTRITION_TIP_TIME = "nutrition_tip_time";
	private static final String CALORIC_SUMMARY_TIME = "caloric_summary_time";

	private static final String INSERT_STATEMENT_PATTERN = "insert into client (id, email, password, tip_day, tip_time, summary_time) values (?, ?, ?, ?, ?, ?)";
	private static final String CHECK_ID_STATEMENT_PATTERN = "select id from client where id=?";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientRegistration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ClientBean clientBean = new ClientBean();
		clientBean.setId(request.getParameter(CLIENT_ID));
		clientBean.setEmail(request.getParameter(CLIENT_EMAIL));
		clientBean.setPassword(request.getParameter(CLIENT_PWD));
		clientBean.setConfirmedPassword(request
				.getParameter(CLIENT_CONFIRMED_PWD));
		clientBean.setNutritionTipDay(request.getParameter(NUTRITION_TIP_DAY));
		clientBean
				.setNutritionTipTime(request.getParameter(NUTRITION_TIP_TIME));
		clientBean.setCaloricSummaryTime(request
				.getParameter(CALORIC_SUMMARY_TIME));
		request.setAttribute(CommonConstants.CLIENT_REQUEST_KEY, clientBean);
		//RequestDispatcher dispatcher = request
		//		.getRequestDispatcher(CLIENT_ECHO_ADDRESS);
		//dispatcher.forward(request, response);
		insertClient(clientBean, request, response);
	}

	private void insertClient(ClientBean clientBean, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		synchronized (this) {
			// forwardRequest returns true if one of the ClientBean's fields
			// is missing.
			if ( forwardRequest(clientBean, request, response) )
				return;
			
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try {
				// Register JDBC driver
				Class.forName(CommonConstants.JDBC_DRIVER);
				// Open a connection
				connection = DriverManager
						.getConnection(CommonConstants.DB_URL, 
									   CommonConstants.DB_USER, 
									   CommonConstants.DB_PWD);
				connection.setAutoCommit(false);

				// Execute SQL query
				//statement = connection.createStatement();
				preparedStatement = connection
						.prepareStatement(INSERT_STATEMENT_PATTERN);
				preparedStatement.setString(1, clientBean.getId());
				preparedStatement.setString(2, clientBean.getEmail());
				preparedStatement.setString(3, clientBean.getPassword());
				preparedStatement.setString(4, clientBean.getNutritionTipDay());
				preparedStatement.setString(5, clientBean.getNutritionTipTime());
				preparedStatement.setString(6, clientBean.getCaloricSummaryTime());

				// sends the statement to the database server
				// I need to check if the id already exists
				int row = preparedStatement.executeUpdate();
				if ( row > 0 ) {
					connection.commit();
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(CommonConstants.THANK_YOU_PAGE);
					dispatcher.forward(request, response);
				}
				else {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(CommonConstants.REGISTRATION_FAILURE_PAGE);
					dispatcher.forward(request, response);
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
						e.printStackTrace();
					}
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
		}
	}
	
	private boolean idExists(String id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager
					.getConnection(CommonConstants.DB_URL, 
								   CommonConstants.DB_USER, 
								   CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(CHECK_ID_STATEMENT_PATTERN);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if ( rs.next() ) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException se) {
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
	
	private boolean forwardRequest(ClientBean clientBean, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		final String id = clientBean.getId();
		if ( "".equals(id) ) {
			RequestDispatcher dispatcher =
					request.getRequestDispatcher(CommonConstants.MISSING_ID_PAGE);
			dispatcher.forward(request, response);
			return true;
		}
		else if ( idExists(id) ) {
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher(CommonConstants.ID_EXISTS_PAGE);
			dispatcher.forward(request, response);
			return true;
		}
		final String email = clientBean.getEmail();
		if ( "".equals(email) ) {
			RequestDispatcher dispatcher =
					request.getRequestDispatcher(CommonConstants.MISSING_EMAIL_PAGE);
			dispatcher.forward(request, response);
			return true;
		}
		final String password = clientBean.getPassword();
		if ( "".equals(password) ) {
			RequestDispatcher dispatcher =
					request.getRequestDispatcher(CommonConstants.MISSING_PASSWORD_PAGE);
			dispatcher.forward(request, response);
			return true;
		}
		final String confirmedPassword = clientBean.getConfirmedPassword();
		if ( !passwordsMatch(password, confirmedPassword) ) {
			RequestDispatcher dispatcher =
					request.getRequestDispatcher(CommonConstants.PASSWORD_MISMATCH_PAGE);
			dispatcher.forward(request, response);
			return true;
		}
		return false;
	}
	
	private boolean passwordsMatch(String pwd, String confirmed_pwd) {
		return pwd.equals(confirmed_pwd);
	}

}
