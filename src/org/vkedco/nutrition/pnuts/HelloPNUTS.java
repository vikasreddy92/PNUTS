package org.vkedco.nutrition.pnuts;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloPNUTS
 */
@WebServlet("/HelloPNUTS")
public class HelloPNUTS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloPNUTS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String un = request.getParameter("username");		
	    String pwd = request.getParameter("password");	
		
	    String title = "Hello from PNUTS";
	    out.println(HtmlUtils.DOCTYPE +
	    	         HtmlUtils.headWithTitle(title) +
	    	         "<body bgcolor=\"#f0f0f0\">\n" +
	    	         "<h1 align=\"center\">" + title + "</h1>\n" +
	    	         "<h1 align=\"center\">" + un + "</h1>\n" +
	    	         "<h1 align=\"center\">" + pwd + "</h1>\n" +
	    	         "<h1 align=\"center\">" + request.getPathInfo() + "</h1>\n" +
	    	         "</body></html>");
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
