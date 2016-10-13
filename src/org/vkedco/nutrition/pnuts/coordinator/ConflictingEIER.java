package org.vkedco.nutrition.pnuts.coordinator;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class ConflictingEIER
 */
@WebServlet("/ConflictingEIER")
public class ConflictingEIER extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Conflicted Energy Intake Estimation Requests";
	private static String BEFORE_MEAL = "Before Meal";
	private static String TIME = "Time";
	private static String AFTER_MEAL = "After Meal";
	private static String STATUS = "Status";
	private static String REQ_ID = "Request ID";
	private static String CLIENT_ID = "Client ID";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConflictingEIER() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ProgramCoordinatorBean pcBean = (ProgramCoordinatorBean) session
				.getAttribute(CommonConstants.PC_BEAN_KEY);
		if (pcBean == null
				|| !CommonValidator.validateProgramCoordinator(pcBean)) {
			request.getRequestDispatcher(
					CommonConstants.INCORRECT_CREDENTIALS_INFO).forward(
					request, response);
		} else {
			response.setContentType(HtmlUtils.TEXT_HTML);
			PrintWriter out = response.getWriter();
			final String conflict_eier_list = ConflictingEIERBeanUtils
					.retrieveConflictEIERList();
			out.println(HtmlUtils.DOCTYPE + HtmlUtils.beginHTML()
					+ HtmlUtils.headWithTitle(TITLE)
					+ HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6)
					+ HtmlUtils.h1AlignCenter(TITLE) + HtmlUtils.beginCenter()
					+ HtmlUtils.beginTableWithBorder(1)
					+ HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00)
					+ HtmlUtils.TH(REQ_ID)
					+ HtmlUtils.TH(CLIENT_ID)
					+ HtmlUtils.TH(BEFORE_MEAL) + HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(AFTER_MEAL) + HtmlUtils.TH(TIME)
					+ HtmlUtils.TH(STATUS) + conflict_eier_list
					+ HtmlUtils.endTable() + HtmlUtils.endCenter()
					+ HtmlUtils.endBody() + HtmlUtils.endHTML());
			out.flush();
			out.close();
		}
	}
}