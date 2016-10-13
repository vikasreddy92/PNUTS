package org.vkedco.nutrition.pnuts.nutritionist;

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
 * Author: Vikas Reddy Sudini 
 * Servlet implementation class
 * CheckNutritionistLoginInfo if login fails, redirects to an error page else
 * forwards the request to the PendingEIERList servlet.
 */
@WebServlet(description = "verifies the credentials of nutritionist", urlPatterns = { "/CheckNutritionistLoginInfo" })
public class CheckNutritionistLoginInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckNutritionistLoginInfo() {
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

		String id = request.getParameter(CommonConstants.NUT_ID_KEY);
		String pwd = request.getParameter(CommonConstants.NUT_PWD_KEY);

		NutritionistBean nBean = new NutritionistBean();
		nBean.setId(id);
		nBean.setPassword(pwd);
		session.setAttribute(CommonConstants.NUT_BEAN_KEY, nBean);

		if (!(CommonValidator.validateNutritionist(nBean))) {
			System.err.println("Nutritionist Login failed");
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.INCORRECT_NUT_LOGIN_INFO);
			dispatcher.forward(request, response);
		} else {

			// request.setAttribute(CommonConstants.NUT_ID_KEY, id);
			// request.setAttribute(CommonConstants.NUT_PWD_KEY, pwd);
//			System.err.println("Nutrionist Login was successfull\nGoing to PendingEIERList.");
			RequestDispatcher dispatcher1 = request
					.getRequestDispatcher(CommonConstants.PendingEIERList);
			dispatcher1.forward(request, response);
		}
	}
}