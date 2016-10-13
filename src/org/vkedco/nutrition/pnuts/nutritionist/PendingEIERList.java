package org.vkedco.nutrition.pnuts.nutritionist;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;
import org.vkedco.nutrition.pnuts.HtmlUtils;

/**
 * Servlet implementation class PendingEIERList Author: Vladimir Kulyukin This
 * servlet creates an HTML page from the pending_eier_before_meal and
 * pending_eier_after_meal tables of the PNUTS database.
 * 
 * We need to pass the nutritionist id and password and save it in the Session
 * object.
 */
@WebServlet("/PendingEIERList")
public class PendingEIERList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static int NUM_JOIN_COLS = 6;
	private static String EIER_LIST_STATEMENT = "select "
			+ "pending_eier_before_meal.req_id, pending_eier_before_meal.client_id, "
			+ "pending_eier_before_meal.vid_before_meal, pending_eier_before_meal.time, "
			+ "pending_eier_after_meal.vid_after_meal, pending_eier_after_meal.time "
			+ "from pending_eier_before_meal "
			+ "inner join pending_eier_after_meal " + "on "
			+ "pending_eier_before_meal.req_id = "
			+ "pending_eier_after_meal.req_id " + "and "
			+ "pending_eier_before_meal.client_id = "
			+ "pending_eier_after_meal.client_id";
	private static String REQ_ID = "Request ID";
	private static String CLIENT_ID = "Client ID";
	private static String BEFORE_MEAL = "Before Meal";
	private static String TIME = "time";
	private static String AFTER_MEAL = "After Meal";
	private static String EVALUATION = "Evaluation";

	private static String TITLE = "Pending Energy Intake Estimation Requests";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PendingEIERList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(CommonConstants.INCORRECT_NUT_LOGIN_INFO)
				.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		NutritionistBean nBean = new NutritionistBean();
		nBean = (NutritionistBean) session
				.getAttribute(CommonConstants.NUT_BEAN_KEY);
		if (nBean == null || !CommonValidator.validateNutritionist(nBean)) {
			request.getRequestDispatcher(
					CommonConstants.INCORRECT_NUT_LOGIN_INFO).forward(request,
					response);
		} else {
			response.setContentType(HtmlUtils.TEXT_HTML);
			PrintWriter out = response.getWriter();
			final String eier_list = retrieveEIERList();
			out.println(HtmlUtils.DOCTYPE + HtmlUtils.beginHTML()
					+ HtmlUtils.headWithTitle(TITLE)
					+ HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6)
					+ HtmlUtils.h1AlignCenter(TITLE) + HtmlUtils.beginCenter()
					+ HtmlUtils.beginTableWithBorder(1)
					+ HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00)
					+ HtmlUtils.TH(REQ_ID) + HtmlUtils.TH(CLIENT_ID)
					+ HtmlUtils.TH(BEFORE_MEAL) + HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(AFTER_MEAL) + HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(EVALUATION) + eier_list
					+ HtmlUtils.endTable() + HtmlUtils.endCenter()
					+ HtmlUtils.endBody() + HtmlUtils.endHTML());
			out.flush();
			out.close();
		}
	}

	private String retrieveEIERList() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String result = null;
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);

			statement = connection
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			rs = statement.executeQuery(EIER_LIST_STATEMENT);
			StringBuffer sb = new StringBuffer();
			String req_id = null;
			String client_id = null;
			if (rs.first()) {
				while (!rs.isAfterLast()) {
					sb.append(HtmlUtils.beginTR());
					for (int i = 1; i <= NUM_JOIN_COLS; i++) {
						if (i == 1) {
							req_id = rs.getString(i);
						}
						if (i == 2) {
							client_id = rs.getString(i);
						}
						if (i == 3 || i == 5) {
							sb.append(HtmlUtils.TD(HtmlUtils.AHREF(
									CommonConstants.DOWNLOAD_FILE_SERVLET
											+ "?downloadKey="
											+ CommonConstants.DOWNLOAD_CLIENT
											+ "?fileName=" + rs.getString(i),
									CommonConstants.DATA_LINK_NAME)));
						} else {
							sb.append(HtmlUtils.TD(rs.getString(i)));
						}
					}
					sb.append(HtmlUtils.TD(HtmlUtils.AHREF(
							CommonConstants.EVAL_EIER_SERVLET + "?"
									+ CommonConstants.REQ_ID_KEY + "=" + req_id
									+ "&" + CommonConstants.CLIENT_ID_KEY + "="
									+ client_id, "evaluate")));

					sb.append(HtmlUtils.endTR());
					rs.next();
				}
			}

			return sb.toString();

			// Clean-up environment
			// statement.close();
			// connection.close();
		} catch (SQLException se) {
			result = se.toString();
		} catch (ClassNotFoundException e) {
			result = e.toString();
		} finally {
			// finally block used to close resources
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se) {
				result = "finally " + se.toString();
			} // nothing we can do
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
		} // end try
		return result;
	}

	// <a href="TestFileDownload?fileName=VID01.mp4">video</a>
	// private String generateRefLink(String fileName) {
	// return "<a href=\"TestFileDownload?fileName=" + fileName
	// + "\">video</a>";
	//
	// }

}