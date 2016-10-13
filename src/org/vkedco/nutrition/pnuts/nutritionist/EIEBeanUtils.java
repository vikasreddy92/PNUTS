package org.vkedco.nutrition.pnuts.nutritionist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.EIEstimationTableConstants;

public class EIEBeanUtils {
	
	private static final String INSERT_EIE_PATTERN = "insert into ei_estimation (req_id, nut_id, eie_file, cals, time) values (?, ?, ?, ?, ?)";
	private static final String EIE_SELECT_STATEMENT = 
			"select req_id, nut_id, eie_file, cals, time from ei_estimation where req_id=?";
	private static final String DELETE_EIESTIMATION = "delete from ei_estimation where req_id = ? and nut_id = ?";

	// get all eie beans from the database. each estimate must have
	// the same id.
	public static ArrayList<EIEBean> retrieveEIEBeansFromDB(int req_id) {
		ArrayList<EIEBean> eieBeans = new ArrayList<EIEBean>();
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager
					.getConnection(CommonConstants.DB_URL, 
								   CommonConstants.DB_USER, 
								   CommonConstants.DB_PWD);
			preparedStatement = connection
					.prepareStatement(EIE_SELECT_STATEMENT);
			preparedStatement.setInt(1, req_id);
			ResultSet rs = preparedStatement.executeQuery();
			while ( rs.next() ) { 
				int reqId = rs.getInt(EIEstimationTableConstants.REQ_ID_COL_NUM);
				String nutId = rs.getString(EIEstimationTableConstants.NUT_ID_COL_NUM);
				String eieFile = rs.getString(EIEstimationTableConstants.EIE_FILE_COL_NUM);
				int cals = rs.getInt(EIEstimationTableConstants.CALS_COL_NUM);
				java.sql.Timestamp ts = rs.getTimestamp(EIEstimationTableConstants.TIME_COL_NUM);
				EIEBean eb = new EIEBean();
				eb.setReqId(reqId);
				eb.setNutId(nutId);
				eb.setFileName(eieFile);
				eb.setCals(cals);
				eb.setTimestamp(ts);
				eieBeans.add(eb);
			}
			
			return eieBeans;
		}
		catch (SQLException se) {
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
		return eieBeans;
	}
	
	// create an ei estimation bean
	public static EIEBean createEIEBean(int req_id, String nut_id, 
			HashMap<Integer, IngredientBean> ingredientTable, java.sql.Timestamp ts) {
		EIEBean eb = new EIEBean();
		eb.setReqId(req_id);
		eb.setNutId(nut_id);
		eb.setFileName(createIngredientsFileName(req_id, nut_id));
		int cals = 0;
		for(Entry<Integer, IngredientBean> entry: ingredientTable.entrySet()) {
			cals += entry.getValue().getCals();
		}
		eb.setCals(cals);
		eb.setTimestamp(ts);
//		System.err.println("In EIEBeanUtils Creating EIEBean
//		FileName: "+eb.getFileName()+ "\nCalories: "+eb.getCals());
		return eb;	
	}
	public static EIEBean createEIEBean(int req_id, String nut_id, 
			HashMap<Integer, IngredientBean> ingredientTable) {
//		System.err.println("EIEBeanUtils: Creating EIEBean:");
		EIEBean eb = new EIEBean();
		eb.setReqId(req_id);
		eb.setNutId(nut_id);
		eb.setFileName(createIngredientsFileName(req_id, nut_id));
		eb.setIngredientTable(ingredientTable);
		int cals = 0;
		for(Entry<Integer, IngredientBean> entry: ingredientTable.entrySet()) {
			cals += entry.getValue().getCals();
		}
		eb.setCals(cals);
		Calendar calendar = Calendar.getInstance();
		eb.setTimestamp(new java.sql.Timestamp(calendar.getTimeInMillis()));
//		System.err.println("EIERBeanUtils: EIERBean data: \nreqId: "+eb.getReqId()+"\nNutId: "+eb.getNutId()+"\nFileName: "+eb.getFileName()+"\nCalories: "+eb.getCals());
		return eb;
	}
	
	// save an ei estimation bean in the database.
	public static void saveEIEBeanToDB(EIEBean eieBean, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = 
				saveIngredientsToFile(Integer.toString(eieBean.getReqId()), 
						eieBean.getNutId(), 
						eieBean.getIngredientTable());
//		System.err.println("EIEBeanUtils: " + fileName);
		
		if ( fileName == null ) {
//			System.err.println("EIEBeanUtils: EIE file was not saved");
			request.setAttribute(CommonConstants.ERROR_MSG_KEY, "EIE file was not saved");
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher(CommonConstants.INCORRECT_CREDENTIALS_INFO);
			dispatcher.forward(request, response);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager
					.getConnection(CommonConstants.DB_URL, 
								   CommonConstants.DB_USER, 
								   CommonConstants.DB_PWD);
			connection.setAutoCommit(false);

			// Execute SQL query
			// statement = connection.createStatement();
			preparedStatement = connection
					.prepareStatement(INSERT_EIE_PATTERN);
			// Get the nut_id from session.
			// Generate the file from ingredientTable,
			// Save it inside a specific directory
			// save the record 
			preparedStatement.setInt(1, eieBean.getReqId());
			preparedStatement.setString(2, eieBean.getNutId());
			preparedStatement.setString(3, eieBean.getFileName());
			preparedStatement.setInt(4, eieBean.getCals());
			preparedStatement.setTimestamp(5, eieBean.getTimestamp());
			
			int row = preparedStatement.executeUpdate();
			if ( row > 0 ) {
				connection.commit();
				request.setAttribute(CommonConstants.THANK_YOU_KEY, "thank you for your evaluation");
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(CommonConstants.GENERIC_THANK_YOU_PAGE);
				dispatcher.forward(request, response);
			}
			else {
				request.setAttribute(CommonConstants.ERROR_MSG_KEY, "Database failure");
				RequestDispatcher dispatcher = request
						.getRequestDispatcher(CommonConstants.ERROR_PAGE);
				dispatcher.forward(request, response);
				//Exception ex = new Exception("Database failure");
				//throw ex;
			}
			// Clean-up environment
			// statement.close();
			// connection.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			if (connection != null)
				try {
					connection.rollback();
				} catch (SQLException e) {
					//e.printStackTrace();
					throw e;
				}
			throw se;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
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
	
	
	public static int computeTotalCalories(HashMap<Integer, IngredientBean> ingredientTable) {
		int totalCalories = 0;
		for(Entry<Integer, IngredientBean> entry : ingredientTable.entrySet()) {
			totalCalories += entry.getValue().getCals();
		}
		return totalCalories;
	}
	
	public static String saveIngredientsToFile(String req_id, String nut_id, HashMap<Integer, IngredientBean> ingredientTable) {
		BufferedWriter out = null;
		final String fileName = createIngredientsFileName(req_id, nut_id);
		final String path = CommonConstants.NUT_DATA_DIR + File.separator + 
				fileName;
		try {
			FileWriter fstream = new FileWriter(path);
			out = new BufferedWriter(fstream);
			for(Entry<Integer, IngredientBean> entry : ingredientTable.entrySet()) {
				out.write(entry.getValue().toString() + "\n");
			}
			out.flush();
			fstream.close();
			out.close();
		}
		catch ( IOException ex ) {
			return null;
		}
		finally {
			if ( out != null ) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}	
		return fileName;
	}
	
	/*
	 * inserts only one EIEBean
	 */
	public static void insertEIEstimation(EIEBean eieBean) throws Exception{
		System.err.println("SaveEIEstimation.insertEIEstimation: starting insert method.");
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
				preparedStatement = connection
						.prepareStatement(INSERT_EIE_PATTERN);				
				preparedStatement.setInt(1, eieBean.getReqId());
				preparedStatement.setString(2, eieBean.getNutId());
				preparedStatement.setString(3, eieBean.getFileName());
				preparedStatement.setInt(4, eieBean.getCals());
				preparedStatement.setTimestamp(5, eieBean.getTimestamp());
				int r = preparedStatement.executeUpdate();
				if( r > 0)
				{
					connection.commit();
				}
				System.err.println("SaveEIEstimation.insertEIEstimation: No of rows affected: "+r);
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

	public static void deleteEIEstimation(EIEBean eieBean) throws Exception {
		System.err.println("SaveEIEstimation.deleteEIEstimation: starting delete method.");
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
				preparedStatement = connection
						.prepareStatement(DELETE_EIESTIMATION);				
				preparedStatement.setInt(1, eieBean.getReqId());
				preparedStatement.setString(2, eieBean.getNutId());
				int r = preparedStatement.executeUpdate();
				if( r > 0 ){
					connection.commit();
				}
				System.err.println("SaveEIEstimation.deleteEIEstimation: No of rows deleted: "+r);
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

	public static String createIngredientsFileName(String req_id, String nut_id) {
		return req_id + "_" + nut_id + ".txt";
	}
	
	public static String createIngredientsFileName(int req_id, String nut_id) {
		return req_id + "_" + nut_id + ".txt";
	}

}
