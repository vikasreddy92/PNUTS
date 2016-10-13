package org.vkedco.nutrition.pnuts.coordinator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;
import org.vkedco.nutrition.pnuts.HtmlUtils;
import org.vkedco.nutrition.pnuts.nutritionist.EIEBean;
import org.vkedco.nutrition.pnuts.nutritionist.NutritionistBean;
import org.vkedco.nutrition.pnuts.nutritionist.NutritionistBeanUtils;

/**
 * Servlet implementation class ConflictingEstimations
 */
@WebServlet("/ConflictingEstimations")
public class ConflictingEstimations extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Conflicting estimations";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConflictingEstimations() {
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
		if (pcBean == null
				|| !CommonValidator.validateProgramCoordinator(pcBean)) {
			request.getRequestDispatcher(
					CommonConstants.INCORRECT_CREDENTIALS_INFO).forward(
					request, response);
		} else {
			synchronized (this) {
				BufferedReader in = null;
				try {
					int req_id = Integer.parseInt(request
							.getParameter(CommonConstants.REQ_ID_KEY));
					ArrayList<EIEBean> eieBeans = new ArrayList<EIEBean>();
					/*
					 * retrieving nutritionist information from ei_estimation
					 */
					eieBeans = ConflictingEIERBeanUtils
							.retrieveConflictingEstimationsFromDB(req_id);

					/*
					 * Reading the nutritionists' estimations and storing them
					 * in estimations string array
					 */
					// System.err
					// .println("ConflictingEstimations.doPost: eieBeans size: "
					// + eieBeans.size());
					String[] estimations = new String[eieBeans.size()];
					String[] emails = new String[eieBeans.size()];
					int index = 0;
					/*
					 * reading the estimation from file to a string array
					 */
					for (EIEBean eieBean : eieBeans) {
						in = new BufferedReader(new FileReader(
								CommonConstants.NUT_DATA_DIR + File.separator
										+ eieBean.getFileName()));
						StringBuffer sb = new StringBuffer();
						String str = new String();
						while ((str = in.readLine()) != null) {
							sb.append(HtmlUtils.BR);
							sb.append(str);
						}
						estimations[index] = sb.toString();
						index++;
					}
					if (in != null) {
						in.close();
					}
					index = 0;
					/*
					 * reading emails from nutritionist table to a string array
					 */
					for (EIEBean eieBean : eieBeans) {
						NutritionistBean nBean = new NutritionistBean();
						nBean = NutritionistBeanUtils
								.retrieveNutritionistBean(eieBean.getNutId());
						if (nBean != null) {
							emails[index] = nBean.getEmail();
							index++;
						}
					}
					response.setContentType(HtmlUtils.TEXT_HTML);
					PrintWriter out = response.getWriter();
					out.println(HtmlUtils.DOCTYPE + HtmlUtils.beginHTML()
							+ HtmlUtils.headWithTitle(TITLE)
							+ HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6)
							+ HtmlUtils.h1AlignCenter(TITLE));
					for (int i = 0; i < eieBeans.size(); i++) {
						out.println(HtmlUtils.BEGIN_UL
								+ HtmlUtils.ULLI("Nutritionist ID: "
										+ eieBeans.get(i).getNutId())
								+ HtmlUtils.ULLI("Email ID: " + emails[i])
								+ HtmlUtils.ULLI("Estimation: "
										+ HtmlUtils
												.AHREF(CommonConstants.DOWNLOAD_FILE_SERVLET
														+"?downloadKey="
														+ CommonConstants.DOWNLOAD_NUT
														+ "?fileName="
														+ eieBeans.get(i)
																.getFileName(),
														eieBeans.get(i)
																.getFileName()))
								+ HtmlUtils.BEGIN_PARA + "Summary: "
								+ estimations[i] + HtmlUtils.END_PARA
								+ HtmlUtils.END_UL + HtmlUtils.BR
								+ HtmlUtils.BR);
					}
					out.println(HtmlUtils.endBody());
					out.println(HtmlUtils.endHTML());
				} catch (FileNotFoundException ffe) {
					System.err.println("File not found");
				}
			}

		}
	}
}
