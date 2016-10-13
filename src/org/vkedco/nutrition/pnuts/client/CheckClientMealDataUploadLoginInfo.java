package org.vkedco.nutrition.pnuts.client;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;

/**
 * Servlet implementation class CheckClientMealDataUploadLoginInfo
 */
@WebServlet("/CheckClientMealDataUploadLoginInfo")
public class CheckClientMealDataUploadLoginInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckClientMealDataUploadLoginInfo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
		String id = request.getParameter(CommonConstants.CLIENT_ID_KEY);
		String pwd = request.getParameter(CommonConstants.CLIENT_PWD_KEY);
		if (request == null || id.isEmpty() || id.equals(null) || id.equals("")) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.ILLEGAL_CLIENT_ACCESS);
			dispatcher.forward(request, response);
		} else {
			ClientBean cb = new ClientBean();
			cb.setId(id);
			cb.setPassword(pwd);

			if (!CommonValidator.validateClient(cb)) {
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(CommonConstants.INCORRECT_LOGIN_INFO);
				dispatcher.forward(request, response);
			} else {
				request.setAttribute(CommonConstants.CLIENT_ID_KEY, id);
				request.setAttribute(CommonConstants.CLIENT_PWD_KEY, pwd);
				session.setAttribute(CommonConstants.CLIENT_BEAN_KEY, cb);
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(CommonConstants.CLIENT_MEAL_DATA_UPLOAD);
				dispatcher.forward(request, response);
			}
		}
	}
}
