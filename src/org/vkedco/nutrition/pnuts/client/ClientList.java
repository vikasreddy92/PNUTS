package org.vkedco.nutrition.pnuts.client;

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

import org.vkedco.nutrition.pnuts.*;

/**
 * Servlet implementation class ClientList
 * Author: Vladimir Kulyukin
 * Create an HTML document with a list of all current clients from
 * the client table of the PNUTS database.
 * 
 * Sample invocation http://localhost:8080/PNUTS/ClientList.
 */
@WebServlet("/ClientList")
public class ClientList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String CLIENT_LIST_STATEMENT = "select * from client";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(HtmlUtils.TEXT_HTML);
		PrintWriter out = response.getWriter();
		final String client_list = retrieveClientList();
		out.println(HtmlUtils.DOCTYPE +
				HtmlUtils.beginHTML() +
				HtmlUtils.headWithTitle(CommonConstants.CLIENT_LIST) +
				HtmlUtils.beginBodyBgColor(HtmlUtils.FDF5E6) +
				HtmlUtils.h1AlignCenter(CommonConstants.CLIENT_LIST) +
				HtmlUtils.beginCenter() +
				HtmlUtils.beginTableWithBorder(1) +
				HtmlUtils.beginTRBgColor(HtmlUtils.FFAD00) +
				HtmlUtils.TH(ClientTableConstants.ID_COL_NAME) + 
				HtmlUtils.TH(ClientTableConstants.EMAIL_COL_NAME) +
				HtmlUtils.TH(ClientTableConstants.PASSWORD_COL_NAME) +
				HtmlUtils.TH(ClientTableConstants.TIP_DAY_COL_NAME) + 
				HtmlUtils.TH(ClientTableConstants.TIP_TIME_COL_NAME) + 
				HtmlUtils.TH(ClientTableConstants.SUMMARY_TIME_COL_NAME) + 
				client_list +
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
	
	private String retrieveClientList() {
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

			rs = statement.executeQuery(CLIENT_LIST_STATEMENT);
			StringBuffer sb = new StringBuffer();
			
			if ( rs.first() ) {
				while ( !rs.isAfterLast() ) {
					sb.append(HtmlUtils.beginTR());
					for(int i = 1; i <= ClientTableConstants.NUM_COLS; i++) {
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
