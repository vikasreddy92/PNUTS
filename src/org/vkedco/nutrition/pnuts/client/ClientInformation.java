package org.vkedco.nutrition.pnuts.client;

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
import org.vkedco.nutrition.pnuts.coordinator.ProgramCoordinatorBean;

/**
 * Servlet implementation class ClientInformation
 */
@WebServlet("/ClientInformation")
public class ClientInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Client information";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ClientInformation() {
		super();
		// TODO Auto-generated constructor stub
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
		HttpSession session = request.getSession();
		ProgramCoordinatorBean pcBean = (ProgramCoordinatorBean) session
				.getAttribute(CommonConstants.PC_BEAN_KEY);
		ClientBean cBean = new ClientBean();
		cBean = ClientBeanUtils.retrieveClientBean(request
				.getParameter("client_id"));
		if (pcBean == null
				|| !CommonValidator.validateProgramCoordinator(pcBean)) {
			request.getRequestDispatcher(
					CommonConstants.INCORRECT_CREDENTIALS_INFO).forward(
					request, response);
		} else {
			response.setContentType(HtmlUtils.TEXT_HTML);
			PrintWriter out = response.getWriter();
			out.println(HtmlUtils.DOCTYPE + HtmlUtils.beginHTML()
					+ HtmlUtils.headWithTitle(TITLE)
					+ HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6)
					+ HtmlUtils.h1AlignCenter(TITLE)
					+ HtmlUtils.beginTableWithBorder(1)
					+ HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00)
					+ HtmlUtils.BEGIN_UL
					+ HtmlUtils.ULLI("Client ID: " + cBean.getId())
					+ HtmlUtils.ULLI("Client Email: " + cBean.getEmail())
					+ HtmlUtils.END_UL + HtmlUtils.endTable()
					+ HtmlUtils.endBody() + HtmlUtils.endHTML());
			out.flush();
			out.close();
		}
	}

}
