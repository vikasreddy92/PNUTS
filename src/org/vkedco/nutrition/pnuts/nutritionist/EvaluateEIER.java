package org.vkedco.nutrition.pnuts.nutritionist;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;
import org.vkedco.nutrition.pnuts.HtmlUtils;

/*
 * *****************************************************
 * EvaluateEIER
 * Author: Vladimir Kulyukin
 * 
 * This servlet will allow the nutritionist to enter
 * his/her evaluation of the energy intake evaluation request.
 * 
 * get the nutritionist's id and password from
 * the session's object and do not do anything if the
 * validation is checked.
 * ******************************************************
 */

/**
 * Servlet implementation class EvaluateEIER
 */
@WebServlet("/EvaluateEIER")
public class EvaluateEIER extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// private static final int MAX_NUM_INGREDIENTS = 10;

	private static int NUM_JOIN_COLS = 4;
	private static String EIER_LIST_PATTERN = "select "
			+ "pending_eier_before_meal.vid_before_meal, pending_eier_before_meal.time, "
			+ "pending_eier_after_meal.vid_after_meal, pending_eier_after_meal.time "
			+ "from pending_eier_before_meal "
			+ "inner join pending_eier_after_meal " + "on "
			+ "pending_eier_before_meal.req_id = ? " + "and "
			+ "pending_eier_after_meal.req_id = ? " + "and "
			+ "pending_eier_before_meal.client_id = ? " + "and "
			+ "pending_eier_after_meal.client_id = ?";
	private static String DATA_BEFORE_MEAL = "Data Before Meal";
	private static String TIME = "Time";
	private static String DATA_AFTER_MEAL = "Data After Meal";
	private static String USDA = "USDA";
	private static String NDB_NUMBER = "NDB Number";
	private static String FOOD_NAME = "Food Name";
	private static String FOOD_AMOUNT = "Amount (grams)";
	private static String CALORIES = "Calories";
	private static String TITLE = "Energy Intake Estimation Evaluator";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EvaluateEIER() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
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

			EIERBean eierBean = new EIERBean();
			int req_id = 0;

			try {
				req_id = Integer.parseInt(request
						.getParameter(CommonConstants.REQ_ID_KEY));
			} catch (NumberFormatException ex) {
			}

			eierBean.setReqId(req_id);
			eierBean.setClientId(request
					.getParameter(CommonConstants.CLIENT_ID_KEY));
			session.setAttribute(CommonConstants.EIER_BEAN_KEY, eierBean);
			response.setContentType(HtmlUtils.TEXT_HTML);
			PrintWriter out = response.getWriter();
			out.println(HtmlUtils.DOCTYPE
					+ HtmlUtils.beginHTML()
					+ HtmlUtils.headWithTitle(TITLE)
					+ HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6)
					+ HtmlUtils.h1AlignCenter(TITLE)
					+ HtmlUtils.beginCenter()
					+ HtmlUtils.beginTableWithBorder(1)
					+ HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00)
					+ HtmlUtils.TH(DATA_BEFORE_MEAL)
					+ HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(DATA_AFTER_MEAL)
					+ HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(USDA)
					+ retreiveEIERRow(eierBean, request, response)
					+ HtmlUtils.endTable()
					+

					HtmlUtils.beginForm(CommonConstants.SAVE_EIER_EVAL_SERVLET)
					+ HtmlUtils.beginTableWithBorder(1)
					+ HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00)
					+ HtmlUtils.TH(NDB_NUMBER)
					+ HtmlUtils.TH(FOOD_NAME)
					+ HtmlUtils.TH(FOOD_AMOUNT)
					+ HtmlUtils.TH(CALORIES)
					+ HtmlUtils.emptyInputRows1(CommonConstants.EIE_NUM_ROWS,
							CommonConstants.EIE_NUM_COLS)
					+ HtmlUtils.endTable()
					+ HtmlUtils.inputSubmit(HtmlUtils.SUBMIT)
					+ HtmlUtils.inputReset() + HtmlUtils.endForm()
					+ HtmlUtils.endCenter() + HtmlUtils.endBody()
					+ HtmlUtils.endHTML());
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	private String retreiveEIERRow(EIERBean eierBean,
			HttpServletRequest request, HttpServletResponse response) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String result = null;
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager.getConnection(CommonConstants.DB_URL,
					CommonConstants.DB_USER, CommonConstants.DB_PWD);

			statement = connection.prepareStatement(EIER_LIST_PATTERN);
			statement.setInt(1, eierBean.getReqId());
			statement.setInt(2, eierBean.getReqId());
			statement.setString(3, eierBean.getClientId());
			statement.setString(4, eierBean.getClientId());

			rs = statement.executeQuery();
			StringBuffer sb = new StringBuffer();

			if (rs.first()) {
				while (!rs.isAfterLast()) {
					sb.append(HtmlUtils.beginTR());
					for (int i = 1; i <= NUM_JOIN_COLS; i++) {
						if (i == 1 || i == 3) {
							sb.append(HtmlUtils.TD(HtmlUtils.AHREF(
									CommonConstants.DOWNLOAD_FILE_SERVLET
									+"?downloadKey="
									+ CommonConstants.DOWNLOAD_CLIENT
											+ "?fileName=" + rs.getString(i),
									"video")));
						} else {
							sb.append(HtmlUtils.TD(rs.getString(i)));
						}
					}
					// <a href="http://ndb.nal.usda.gov/" target="popup"
					// onclick="window.open('http://ndb.nal.usda.gov/', 'USDA', 'width=400,height=400')">USDA</a>
					final String usda_popup = HtmlUtils.BEGIN_AHREF
							+ HtmlUtils.ESCAPE_DBLQT
							+ CommonConstants.USDA_SITE
							+ HtmlUtils.ESCAPE_DBLQT
							+ " target="
							+ HtmlUtils.ESCAPE_DBLQT
							+ "popup"
							+ HtmlUtils.ESCAPE_DBLQT
							+ " onclick="
							+ HtmlUtils.ESCAPE_DBLQT
							+ "window.open('http://ndb.nal.usda.gov/', 'USDA', 'width=400,height=400')"
							+ HtmlUtils.ESCAPE_DBLQT + HtmlUtils.GREATER_THAN
							+ CommonConstants.USDA_SITE + HtmlUtils.END_AHREF;

					sb.append(HtmlUtils.TD(usda_popup));
					sb.append(HtmlUtils.endTR());
					rs.next();
				}
			}

			// <iframe src="http://ndb.nal.usda.gov/" width="700" height="500"/>
			// sb.append(HtmlUtils.IFRAME("http://ndb.nal.usda.gov", 700, 500));

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
}