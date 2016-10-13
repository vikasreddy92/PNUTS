package org.vkedco.nutrition.pnuts.nutritionist;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.CommonValidator;
import org.vkedco.nutrition.pnuts.HtmlUtils;
import org.vkedco.nutrition.pnuts.coordinator.ConflictingEIERBean;
import org.vkedco.nutrition.pnuts.coordinator.ConflictingEIERBeanUtils;
import org.vkedco.nutrition.pnuts.coordinator.EmailUtils;

/**
 ******************************************************************** 
 * Servlet implementation class SaveEIEstimation
 * 
 * Get the nutritionist's id and password from the session and check. Save it in
 * a table. Need to implement the logic: the request must be evaluated by two
 * nuts. If they agree the request is taken off the pending eier list. If they
 * do not agree, the request can be estimated by one more evaluator.
 * 
 * this is the schema of the ei_estimation table mysql> create table
 * ei_estimation ( req_id int not null, nut_id varchar(10) not null, eie_file
 * varchar(255) not null, cals int not null, time datetime not null );
 * 
 * 
 * req_id | ei_estimation_file | grams calories | nut_id | time_stamp |
 * 
 * mysql> desc ei_estimation;
 * +----------+--------------+------+-----+---------+-------+ | Field | Type |
 * Null | Key | Default | Extra |
 * +----------+--------------+------+-----+---------+-------+ | req_id | int(11)
 * | NO | | NULL | | | nut_id | varchar(10) | NO | | NULL | | | eie_file |
 * varchar(255) | NO | | NULL | | | cals | int(11) | NO | | NULL | | | time |
 * datetime | NO | | NULL | |
 * +----------+--------------+------+-----+---------+-------+
 * 
 * 
 * 
 ******************************************************************** 
 */
@WebServlet("/SaveEIEstimation")
public class SaveEIEstimation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int INGREDIENT_CODE_COL = 0;
	private static final int INGREDIENT_NAME_COL = 1;
	private static final int INGREDIENT_GRAMS_COL = 2;
	private static final int INGREDIENT_CALS_COL = 3;

	private String DELETE_BEFORE_MEAL_REQUEST = "delete from pending_eier_before_meal where req_id = ?";
	private String DELETE_AFTER_MEAL_REQUEST = "delete from pending_eier_after_meal where req_id = ?";

	private static final String EI_ESTIMATION_INPUT_VALS = "EIEstimation Input Values";

	private static String EIER_STATEMENT = "select "
			+ "pending_eier_before_meal.req_id, pending_eier_before_meal.client_id, "
			+ "pending_eier_before_meal.vid_before_meal, pending_eier_before_meal.time, "
			+ "pending_eier_after_meal.vid_after_meal, pending_eier_after_meal.time "
			+ "from pending_eier_before_meal "
			+ "inner join pending_eier_after_meal " + "on "
			+ "pending_eier_before_meal.req_id = "
			+ "pending_eier_after_meal.req_id " + "and "
			+ "pending_eier_before_meal.client_id = "
			+ "pending_eier_after_meal.client_id "
			+ "where pending_eier_before_meal.req_id = ?";

	// these two are for debugging purposes only
	// These parameters should be retrieved from the request.
	private String REQ_ID = "9";
	private String NUT_ID = "A1000";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveEIEstimation() {
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
		NutritionistBean nBean = (NutritionistBean) session
				.getAttribute(CommonConstants.NUT_BEAN_KEY);
		EIERBean eierBean = (EIERBean) session
				.getAttribute(CommonConstants.EIER_BEAN_KEY);
		if (nBean == null || !CommonValidator.validateNutritionist(nBean)) {
			request.getRequestDispatcher(
					CommonConstants.INCORRECT_NUT_LOGIN_INFO).forward(request,
					response);
		} else {
			REQ_ID = "" + eierBean.getReqId();
			NUT_ID = nBean.getId();

			HashMap<Integer, IngredientBean> ingredientTable = new HashMap<Integer, IngredientBean>();
			for (int r = 0; r < CommonConstants.EIE_NUM_ROWS; r++) {
				IngredientBean ib = null;
				for (int c = 0; c < CommonConstants.EIE_NUM_COLS; c++) {
					String ingredient_key = "ingredient" + "_" + r + "_" + c
							+ "value";
					String ingredient_val = request
							.getParameter(ingredient_key);
					// System.err.println("SaveEIEstimation: request("
					// + ingredient_key + "): "
					// + request.getParameter(ingredient_key));
					if (ingredient_val != null && !ingredient_val.equals("")) {
						if (ib == null)
							ib = new IngredientBean();
						switch (c) {
						case INGREDIENT_CODE_COL:
							ib.setCode(ingredient_val);
							break;
						case INGREDIENT_NAME_COL:
							ib.setName(ingredient_val);
							break;
						case INGREDIENT_GRAMS_COL:
							int grams = 0;
							try {
								grams = Integer.parseInt(ingredient_val);
							} catch (NumberFormatException ex) {
								// forward request to illegal input
							}
							ib.setGrams(grams);
							break;
						case INGREDIENT_CALS_COL:
							int cals = 0;
							try {
								cals = Integer.parseInt(ingredient_val);
							} catch (NumberFormatException ex) {
								// forward request to illegal input
							}
							ib.setCals(cals);
							break;
						}
					}
				}
				if (ib != null) {
					// System.err
					// .println("SaveEIEstimation.doGet(): IngredientBean not null");
					if (ib.getName() != null && ib.getCode() != null
							&& !ib.getName().equals("")
							&& !ib.getName().equals(""))
						ingredientTable.put(new Integer(r), ib);
					ib = null;
				}

			}

			synchronized (this) {
				// Extract the table
				boolean insertion_flag = true;
				String exceptionMessage = null;
				try {
					// System.err
					// .println("SaveEIEstimation.doGet(): Calling insertEIEstimate()");
					this.insertEIEstimate(REQ_ID, NUT_ID, ingredientTable,
							request, response);
				} catch (SQLException e) {
					// Send to Failure Page
					// e.printStackTrace();
					insertion_flag = false;
					exceptionMessage = e.toString();
				} catch (ClassNotFoundException e) {
					// e.printStackTrace();
					// Send to failure page
					insertion_flag = false;
					exceptionMessage = e.toString();
				} catch (Exception e) {
					// e.printStackTrace();
					insertion_flag = false;
					exceptionMessage = e.toString();
				}

				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(HtmlUtils.DOCTYPE + HtmlUtils.beginHTML()
						+ HtmlUtils.headWithTitle(EI_ESTIMATION_INPUT_VALS)
						+ HtmlUtils.beginBodyBgColor("#FDF5E6")
						+ HtmlUtils.h1AlignCenter(EI_ESTIMATION_INPUT_VALS));
				out.println("<UL>");
				for (Map.Entry<Integer, IngredientBean> entry : ingredientTable
						.entrySet()) {
					out.println(HtmlUtils.ULLI(entry.getKey() + " : "
							+ entry.getValue().toString()));
				}
				out.println("</UL>");
				if (insertion_flag == true) {
					out.println("<p>insertion successful</p>");
				} else {
					out.println("<p>insertion unsuccessful</p>");
					out.println("<p>" + exceptionMessage + "</p>");
				}
				out.println("</BODY></HTML>");
			}
			session.invalidate();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	// THIS IS THE METHOD THAT SHOULD DO THE BUSINESS LOGIC.
	private void insertEIEstimate(String req_id, String nut_id,
			HashMap<Integer, IngredientBean> ingredientTable,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException,
			ClassNotFoundException, Exception {
		synchronized (this) {
			int reqId = 0;
			try {
				reqId = Integer.parseInt(req_id);
				// System.err.println("SaveEIEstimation.insertEIEstimate(): Request ID: "
				// + reqId
				// + "\nNut ID: " + nut_id);
			} catch (NumberFormatException ex) {
				// forward return to Error
				throw ex;
			}

			// EIEBean inputEIEBean = EIEBeanUtils.createEIEBean(reqId, nut_id,
			// ingredientTable);
			EIEBean inputEIEBean = null;

			try {
				// System.err
				// .println("SaveEIEstimation: calling createEIEBean() ");
				inputEIEBean = EIEBeanUtils.createEIEBean(reqId, nut_id,
						ingredientTable);

				// request.setAttribute(CommonConstants.EIE_BEAN_KEY,
				// inputEIEBean);
				// RequestDispatcher dispatcher = request
				// .getRequestDispatcher(CommonConstants.TEST_EIE_PAGE);
				// dispatcher.forward(request, response);
				if (!CommonValidator.validateEIE(inputEIEBean)) {
					// System.err
					// .println("SaveEIEstimation.insertEIEstimate(): Validation of inputEIEBean Failed.");
					request.setAttribute(CommonConstants.ERROR_MSG_KEY,
							"Invalid information/credentials");
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(CommonConstants.ERROR_PAGE);
					dispatcher.forward(request, response);
				} else {
					// insert a new EIEBean
					// ERROR: inputEIEBean is inserted twice in the table.
					// System.err
					// .println("SaveEIEstimation.insertEIEstimate(): calling EIEBeanUtils.saveEIEBeanToDB");
					EIEBeanUtils.saveEIEBeanToDB(inputEIEBean, request,
							response);
					// System.err.println("SaveEIEstimation: after saveEIEBeanToDB");
				}

			} catch (Exception se) {
				throw se;
			}

			// This is wehere the business logic should begin.
			ArrayList<EIEBean> eieBeans = EIEBeanUtils
					.retrieveEIEBeansFromDB(reqId);
			// System.err.println("SaveEIEstimation.insertEIEstimate(): retrieved EIEBeans: "+eieBeans.size());
			/*
			 * If there is only one eieBean returned, that is just what we
			 * inserted.
			 */
			if (eieBeans.isEmpty() || eieBeans.size() == 1) {
				return;
			}
			/*
			 * If there are two eieBeans returned, then we can compare them. If
			 * they aggree we can delete them from pendingEIERList
			 */
			else if (eieBeans.size() == 2) {
				// System.err
				// .println("SaveEIEstimation: eieBeans ArrayList, size is 2."+inputEIEBean.getNutId()+"::::"+eieBeans.get(0).getNutId());
				/*
				 * If a nutritionist submits evaluation again, then we should
				 * delete his old eval and insert new eval
				 */
				if (inputEIEBean.getNutId().equals(eieBeans.get(0).getNutId())) {
					EIEBeanUtils.deleteEIEstimation(eieBeans.get(0));
					EIEBeanUtils.insertEIEstimation(inputEIEBean);
				}

				else {

					double diff = EIEComparison.compareEIEBeans(inputEIEBean,
							eieBeans.get(0));
					if (diff <= CommonConstants.ACCEPTABLEFACTOR) {
						deletePendingEIER(inputEIEBean.getReqId());
					}
				}
			}
			/*
			 * If 3 eieBeans returned, then we have two conflicting eieBeans in
			 * the database, and should compare the third eieBean with existing
			 * two beans If anyone of the existing eieBean aggrees with new
			 * eieBean then we can delete the request from pendingEIERList
			 * Otherwise we have to insert that request in
			 * conflict_resolution_table and email Program Coordinator
			 */
			else if (eieBeans.size() == 3) {
				/*
				 * If a nutritionist submits evaluation again, then we should
				 * delete his old eval and insert new eval
				 */
				for (EIEBean eieBean : eieBeans) {
					if (inputEIEBean.getNutId().equals(eieBean.getNutId())) {
						EIEBeanUtils.deleteEIEstimation(eieBean);
						EIEBeanUtils.insertEIEstimation(inputEIEBean);
						break;
					}
				}
				eieBeans = EIEBeanUtils.retrieveEIEBeansFromDB(reqId);
				boolean flag = true;
				for (EIEBean eieBean : eieBeans) {
					// if (inputEIEBean.getNutId().equals(eieBean.getNutId()))
					// {
					// EIEBeanUtils.deleteEIEstimation(eieBean);
					// EIEBeanUtils.insertEIEstimation(inputEIEBean);
					// break;
					// }
					// else
					// {
					double diff = EIEComparison.compareEIEBeans(inputEIEBean,
							eieBean);
					if (diff <= CommonConstants.ACCEPTABLEFACTOR) {
						flag = false;
						deletePendingEIER(inputEIEBean.getReqId());
						break;
					} else if (flag && eieBeans.size() == 3) {
						ConflictingEIERBean conflictingEIERBean = new ConflictingEIERBean();
						conflictingEIERBean = retrieveEIER(inputEIEBean
								.getReqId());
						ConflictingEIERBeanUtils
								.insertIntoConflictResolutionTable(conflictingEIERBean);
						EmailUtils.sendEmail(
								CommonConstants.EMAIL_SUBJECT
										+ inputEIEBean.getReqId(),
								CommonConstants.EMAIL_TEXT
										+ inputEIEBean.getReqId());
						deletePendingEIER(inputEIEBean.getReqId());
					}
					// }
					// }
				}
			}
		}
	}

	private ConflictingEIERBean retrieveEIER(int reqId) throws Exception {
		ConflictingEIERBean ceierBean = new ConflictingEIERBean();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		synchronized (this) {
			try {
				Class.forName(CommonConstants.JDBC_DRIVER);
				connection = DriverManager.getConnection(
						CommonConstants.DB_URL, CommonConstants.DB_USER,
						CommonConstants.DB_PWD);
				preparedStatement = connection.prepareStatement(EIER_STATEMENT);
				preparedStatement.setInt(1, reqId);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					ceierBean.setReqId(rs.getInt(1));
					ceierBean.setClientId(rs.getString(2));
					ceierBean.setBeforeMealFilePath(rs.getString(3));
					ceierBean.setBeforeMealTimestamp(rs.getTimestamp(4));
					ceierBean.setAfterMealFilePath(rs.getString(5));
					ceierBean.setAfterMealTimestamp(rs.getTimestamp(6));
					return ceierBean;
				} else {
					// System.err.println("rs is empty");
				}
			} catch (SQLException se) {
				// Handle errors for JDBC
				if (connection != null)
					try {
						connection.rollback();
					} catch (SQLException e) {
						// e.printStackTrace();
						throw e;
					}
				throw se;
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				throw e;
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
				} // end finally try
			} // end try
		}
		return null;
	}

	private void deletePendingEIER(int reqId) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		synchronized (this) {
			try {
				// Register JDBC driver
				Class.forName(CommonConstants.JDBC_DRIVER);
				// Open a connection
				connection = DriverManager.getConnection(
						CommonConstants.DB_URL, CommonConstants.DB_USER,
						CommonConstants.DB_PWD);
				connection.setAutoCommit(false);
				preparedStatement1 = connection
						.prepareStatement(DELETE_BEFORE_MEAL_REQUEST);
				preparedStatement2 = connection
						.prepareStatement(DELETE_AFTER_MEAL_REQUEST);
				preparedStatement1.setInt(1, reqId);
				preparedStatement2.setInt(1, reqId);
				int rs1 = preparedStatement1.executeUpdate();
				int rs2 = preparedStatement2.executeUpdate();
				if (rs1 <= 0 && rs2 <= 0) {
					// System.err.println("SaveEIEstimation: Deletion failed");
				} else {
					connection.commit();
					// System.err
					// .println("SaveEIEstimation: Deleted Successfully");
				}
			} catch (SQLException se) {
				// Handle errors for JDBC
				if (connection != null)
					try {
						connection.rollback();
					} catch (SQLException e) {
						// e.printStackTrace();
						throw e;
					}
				throw se;
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				throw e;
			} finally {
				// finally block used to close resources
				try {
					if (preparedStatement1 != null
							&& preparedStatement2 != null)
						preparedStatement1.close();
					preparedStatement2.close();
				} catch (SQLException se2) {
				} // nothing we can do
				try {
					if (connection != null)
						connection.close();
				} catch (SQLException se) {
					se.printStackTrace();
				} // end finally try
			} // end try

		}
	}
}