package org.vkedco.nutrition.pnuts.nutritionist;

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
 * Servlet implementation class NutritionistRegistration
 */
@WebServlet("/NutritionistRegistration")
public class NutritionistRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String NUT_ID = "nut_id";
	private static final String NUT_EMAIL = "nut_email";
	private static final String NUT_PWD = "nut_pwd";
	private static final String NUT_CONFIRMED_PWD = "nut_confirmed_pwd";
	private static final String NUT_FIRST_NAME = "nut_first_name";
	private static final String NUT_LAST_NAME = "nut_last_name";
	private static final String NUT_ROLE = "nut_role";

	private static final String INSERT_STATEMENT_PATTERN = "insert into nutritionist (id, first_name, last_name, email, password, role) values (?, ?, ?, ?, ?, ?)";
	private static final String CHECK_ID_STATEMENT_PATTERN = "select id from nutritionist where id=?";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NutritionistRegistration() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		NutritionistBean nutBean = new NutritionistBean();

		nutBean.setId(request.getParameter(NUT_ID));
		nutBean.setFirstName(request.getParameter(NUT_FIRST_NAME));
		nutBean.setLastName(request.getParameter(NUT_LAST_NAME));
		nutBean.setEmail(request.getParameter(NUT_EMAIL));
		nutBean.setPassword(request.getParameter(NUT_PWD));
		nutBean.setConfirmedPassword(request.getParameter(NUT_CONFIRMED_PWD));
		nutBean.setRole(request.getParameter(NUT_ROLE));
		request.setAttribute(CommonConstants.NUT_REQUEST_KEY, nutBean);
		System.out.println("Calling insert method");
		insertNutritionist(nutBean, request, response);
	}

	private boolean idExists(String id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(CHECK_ID_STATEMENT_PATTERN);
			preparedStatement.setString(1, id);
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

	private void insertNutritionist(NutritionistBean nutBean,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		synchronized (this) {
			// forwardRequest returns true if one of the ClientBean's fields
			// is missing.
			
			System.err.println("in insert method");
			System.err.println("Nut ID:  " + nutBean.getId() + "\nNut FN:  "
					+ nutBean.getFirstName() + "\nNut LN:  "
					+ nutBean.getLastName() + "\nNut email:  "
					+ nutBean.getEmail() + "\nNut pwd:  "
					+ nutBean.getPassword() + "\nNut Role:  "
					+ nutBean.getRole());

			if (forwardRequest(nutBean, request, response)) {
				System.err.println("Some fields are missing");
				return;
			}
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
						.prepareStatement(INSERT_STATEMENT_PATTERN);
				preparedStatement.setString(1, nutBean.getId());
				preparedStatement.setString(2, nutBean.getFirstName());
				preparedStatement.setString(3, nutBean.getLastName());
				preparedStatement.setString(4, nutBean.getEmail());
				preparedStatement.setString(5, nutBean.getPassword());
				preparedStatement.setString(6, nutBean.getRole());

				System.err.println("Prepared Statement"
						+ preparedStatement.toString());
				// sends the statement to the database server
				// I need to check if the id already exists
				int row = preparedStatement.executeUpdate();

				System.err.println("inserted successfully");
				if (row > 0) {
					connection.commit();
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(CommonConstants.THANK_YOU_PAGE);
					dispatcher.forward(request, response);
				} else {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(CommonConstants.REGISTRATION_FAILURE_PAGE);
					dispatcher.forward(request, response);
				}
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

	private boolean forwardRequest(NutritionistBean nutBean,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check ID
		final String id = nutBean.getId();
		if ("".equals(id)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.MISSING_ID_PAGE);
			dispatcher.forward(request, response);
			return true;
		} else if (idExists(id)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.ID_EXISTS_PAGE);
			dispatcher.forward(request, response);
			return true;
		}

		// check first name
		final String firstName = nutBean.getFirstName();
		if ("".equals(firstName)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.MISSING_FIRST_NAME);
			dispatcher.forward(request, response);
			return true;
		}

		// check last name
		final String lastName = nutBean.getLastName();
		if ("".equals(lastName)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.MISSING_FIRST_NAME);
			dispatcher.forward(request, response);
			return true;
		}

		// check email
		final String email = nutBean.getEmail();
		if ("".equals(email)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.MISSING_EMAIL_PAGE);
			dispatcher.forward(request, response);
			return true;
		}

		// check password
		final String password = nutBean.getPassword();
		if ("".equals(password)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.MISSING_PASSWORD_PAGE);
			dispatcher.forward(request, response);
			return true;
		}

		// check confirmed password
		final String confirmedPassword = nutBean.getConfirmedPassword();
		if (!passwordsMatch(password, confirmedPassword)) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.PASSWORD_MISMATCH_PAGE);
			dispatcher.forward(request, response);
			return true;
		}

		return false;
	}

	private boolean passwordsMatch(String pwd, String confirmed_pwd) {
		return pwd.equals(confirmed_pwd);
	}

}
