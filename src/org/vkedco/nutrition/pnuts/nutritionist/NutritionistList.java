package org.vkedco.nutrition.pnuts.nutritionist;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vkedco.nutrition.pnuts.CommonConstants;
import org.vkedco.nutrition.pnuts.NutritionistTableConstants;
import org.vkedco.nutrition.pnuts.HtmlUtils;

/**
 * Servlet implementation class NutritionistList
 * Author: Vladimir Kulyukin
 * Create an HTML document with a list of all current clients from
 * the nutritionist table of the PNUTS database.
 * 
 * Sample invocation http://localhost:8080/PNUTS/NutritionistList.
 */
@WebServlet("/NutritionistList")
public class NutritionistList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String NUT_LIST_STATEMENT = "select * from nutritionist";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NutritionistList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(HtmlUtils.TEXT_HTML);
		PrintWriter out = response.getWriter();
		final String nut_list = retrieveNutritionistList();
		out.println(HtmlUtils.DOCTYPE +
				HtmlUtils.beginHTML() +
				HtmlUtils.headWithTitle(CommonConstants.NUTRITIONIST_LIST) +
				HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6) +
				HtmlUtils.h1AlignCenter(CommonConstants.NUTRITIONIST_LIST) +
				HtmlUtils.beginCenter() +
				HtmlUtils.beginTableWithBorder(1) +
				HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00) +
				HtmlUtils.TH(NutritionistTableConstants.ID_COL_NAME) + 
				HtmlUtils.TH(NutritionistTableConstants.FIRST_NAME_COL_NAME) +
				HtmlUtils.TH(NutritionistTableConstants.LAST_NAME_COL_NAME) +
				HtmlUtils.TH(NutritionistTableConstants.EMAIL_COL_NAME) +
				HtmlUtils.TH(NutritionistTableConstants.PASSWORD_COL_NAME) +
				HtmlUtils.TH(NutritionistTableConstants.ROLE_COL_NAME) + 
				nut_list +
				HtmlUtils.endTable() + 
				HtmlUtils.endCenter() + 
				HtmlUtils.endBody() +
				HtmlUtils.endHTML()
				);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private String retrieveNutritionistList() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		String result = null;
		try {
			// Register JDBC driver
			Class.forName(CommonConstants.JDBC_DRIVER);
			// Open a connection
			connection = DriverManager
							.getConnection(CommonConstants.DB_URL, 
										   CommonConstants.DB_USER, 
										   CommonConstants.DB_PWD);

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, 
													ResultSet.CONCUR_READ_ONLY);

			rs = statement.executeQuery(NUT_LIST_STATEMENT);
			StringBuffer sb = new StringBuffer();
			
			if ( rs.first() ) {
				while ( !rs.isAfterLast() ) {
					sb.append(HtmlUtils.beginTR());
					for(int i = 1; i <= NutritionistTableConstants.NUM_COLS; i++) {
						sb.append(HtmlUtils.TD(rs.getString(i)));
					}
					sb.append(HtmlUtils.endTR());
					rs.next();
				}
			}
			
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
				if ( rs != null )
					rs.close();
			}
			catch ( Exception ex ) {
				result = "finally " + ex.toString();
			}
		} // end try
		
		return result;
	}

}
