package org.vkedco.nutrition.pnuts.coordinator;

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
 * Servlet implementation class CheckProgramCoordinatorLoginInfo
 */
@WebServlet("/CheckProgramCoordinatorLoginInfo")
public class CheckProgramCoordinatorLoginInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckProgramCoordinatorLoginInfo() {
		super();
		// TODO Auto-generated constructor stub
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
		HttpSession session = request.getSession(true);
		
		String pc_id = request.getParameter(CommonConstants.PC_ID_KEY);
		String pc_pwd = request.getParameter(CommonConstants.PC_PWD_KEY);
		ProgramCoordinatorBean pcBean = new ProgramCoordinatorBean();
		pcBean.setId(pc_id);
		pcBean.setPassword(pc_pwd);
		session.setAttribute(CommonConstants.PC_BEAN_KEY, pcBean);
		if (!CommonValidator.validateProgramCoordinator(pcBean)) {
			System.err.println("Invalid credentials");
			RequestDispatcher dispatcher = request
					.getRequestDispatcher(CommonConstants.INCORRECT_CREDENTIALS_INFO);
			dispatcher.forward(request, response);
		} else {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/ConflictingEIER");
			dispatcher.forward(request, response);
		}
	}

}
