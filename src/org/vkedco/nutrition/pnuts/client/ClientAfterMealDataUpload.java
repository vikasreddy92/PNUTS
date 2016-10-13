//package org.vkedco.nutrition.pnuts.client;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.Calendar;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.vkedco.nutrition.pnuts.CommonConstants;
//
///**
//* Servlet implementation class ClientAfterMealDataUpload
// * Author: Vladimir Kulyukin
// * 
// * This servlet places a video into a specific directory on
// * the server. The directory is defined in the String constant
// * CommonConstants.CLIENT_VIDEO_DIRECTORY. An appropriate
// * record is inserted into the pending_eier_after_meal table
// * of the PNUTS database. The schema of that table is as follows.
// * 
// mysql> describe pending_eier_after_meal;
//+----------------+--------------+------+-----+---------+----------------+
//| Field          | Type         | Null | Key | Default | Extra          |
//+----------------+--------------+------+-----+---------+----------------+
//| req_id         | int(11)      | NO   | PRI | NULL    | auto_increment |
//| client_id      | varchar(10)  | NO   |     | NULL    |                |
//| vid_after_meal | varchar(255) | NO   |     | NULL    |                |
//| time           | datetime     | NO   |     | NULL    |                |
//+----------------+--------------+------+-----+---------+----------------+
//4 rows in set (0.00 sec)
//
//mysql> select * from pending_eier_after_meal;
//+--------+-----------+------------------------+---------------------+
//| req_id | client_id | vid_after_meal         | time                |
//+--------+-----------+------------------------+---------------------+
//|      9 | A0001     | VID_AfterBreakfast.mp4 | 2015-01-20 13:31:20 |
//|     10 | A0001     | VID_AfterLunch.mp4     | 2015-01-20 13:32:10 |
//|     11 | A0001     | VID_AfterSupper.mp4    | 2015-01-20 13:33:27 |
//+--------+-----------+------------------------+---------------------+
//3 rows in set (0.00 sec)
//
//
// * The client's requestion is forwarded to /WEB-INF/Message/FileUploadResponse.jsp
// * with its CommonConstants.UPLOAD_MSG_KEY attribute set to an
// * appropriate message.
// */
//@WebServlet("/ClientAfterMealDataUpload")
//public class ClientAfterMealDataUpload extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//	private String mUploadFilePath = "";
//	
//	private static final String INSERT_STATEMENT_PATTERN = "insert into pending_eier_after_meal (client_id, vid_after_meal, time) values (?, ?, ?)";
//	
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public ClientAfterMealDataUpload() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		if ( ServletFileUpload.isMultipartContent(request) ) {
//			try {
//				List<FileItem> multiparts = 
//						new ServletFileUpload(
//								new DiskFileItemFactory()).parseRequest(request);
//				String fileName = null;
//				for(FileItem item : multiparts) {
//					if ( !item.isFormField() ) {
//						fileName = new File(item.getName()).getName();
//						mUploadFilePath = CommonConstants.CLIENT_DATA_DIR + File.separator + fileName;
//						item.write(new File(mUploadFilePath));
//						insertAfterMealData(fileName, request, response);
//					}
//				}
//				request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1, "File uploaded successfully to " + mUploadFilePath);
//				// store the info in pending_eier_before_meal
//			}
//			catch ( Exception ex ) {
//				request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1, "File failed to upload to " + mUploadFilePath + " exception: " + ex.toString());
//			}
//			
//			request
//				.getRequestDispatcher("/WEB-INF/Message/FileUploadResponse.jsp")
//				.forward(request, response);
//		}
//	}
//	
//	private void insertAfterMealData(String fileName, HttpServletRequest request, 
//			HttpServletResponse response) 
//					throws ServletException, IOException, SQLException, ClassNotFoundException, Exception 
//	{
//		synchronized (this) {
//			// forwardRequest returns true if one of the ClientBean's fields
//			// is missing.
//			
//			Connection connection = null;
//			PreparedStatement preparedStatement = null;
//			try {
//				// Register JDBC driver
//				Class.forName(CommonConstants.JDBC_DRIVER);
//				// Open a connection
//				connection = DriverManager
//						.getConnection(CommonConstants.DB_URL, 
//									   CommonConstants.DB_USER, 
//									   CommonConstants.DB_PWD);
//				connection.setAutoCommit(false);
//
//				// Execute SQL query
//				// statement = connection.createStatement();
//				preparedStatement = connection
//						.prepareStatement(INSERT_STATEMENT_PATTERN);
//				HttpSession session = request.getSession();
//				ClientBean cb = (ClientBean) session.getAttribute(CommonConstants.CLIENT_BEAN_KEY);
//				preparedStatement.setString(1, cb.getId());
//				preparedStatement.setString(2, fileName);
//				Calendar calendar = Calendar.getInstance();
//				preparedStatement.setTimestamp(3, 
//										new java.sql.Timestamp(calendar.getTimeInMillis())
//										);
//				// sends the statement to the database server
//				// I need to check if the id already exists
//				int row = preparedStatement.executeUpdate();
//				if ( row > 0 ) {
//					connection.commit();
//					/*
//					RequestDispatcher dispatcher = request
//							.getRequestDispatcher(CommonConstants.THANK_YOU_PAGE);
//					dispatcher.forward(request, response);
//					*/
//				}
//				else {
//					/*
//					RequestDispatcher dispatcher = request
//							.getRequestDispatcher(CommonConstants.REGISTRATION_FAILURE_PAGE);
//					dispatcher.forward(request, response);
//					*/
//					Exception ex = new Exception("Database failure");
//					throw ex;
//				}
//				
//				// Clean-up environment
//				// statement.close();
//				// connection.close();
//			} catch (SQLException se) {
//				// Handle errors for JDBC
//				if (connection != null)
//					try {
//						connection.rollback();
//					} catch (SQLException e) {
//						//e.printStackTrace();
//						throw e;
//					}
//				throw se;
//			} catch (ClassNotFoundException e) {
//				//e.printStackTrace();
//				throw e;
//			} finally {
//				// finally block used to close resources
//				try {
//					if (preparedStatement != null)
//						preparedStatement.close();
//				} catch (SQLException se2) {
//				} // nothing we can do
//				try {
//					if (connection != null)
//						connection.close();
//				} catch (SQLException se) {
//					se.printStackTrace();
//				}// end finally try
//			} // end try
//		}
//	}
//
//}
