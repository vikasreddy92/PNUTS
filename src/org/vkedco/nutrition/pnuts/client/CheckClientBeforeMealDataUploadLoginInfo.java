//package org.vkedco.nutrition.pnuts.client;
//
//import java.io.IOException;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.vkedco.nutrition.pnuts.CommonConstants;
//import org.vkedco.nutrition.pnuts.CredentialsValidator;
//
///**
// ***********************************************************
// * Servlet implementation class CheckClientBeforeMealDataUploadLoginInfo
// * Author: Vladimir Kulyukin
// * 
// * This servlet is invoked by the POST method of 
// * WebContent/ClientBeforeMealDataUploadLogin.html.
// * If the client's credentials are verified, the client can
// * upload the before-meal video and the request is dispatched
// * to /WEB-INF/Client/ClientBeforeMealDataUpload.jsp.
// * If the client's credentials are not verified, the client's
// * request is transferred to /WEB-INF/Client/ClientBeforeMealDataUpload.jsp.
// ***********************************************************
// */
//@WebServlet("/CheckClientBeforeMealDataUploadLoginInfo")
//public class CheckClientBeforeMealDataUploadLoginInfo extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public CheckClientBeforeMealDataUploadLoginInfo() {
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
//		
//		
//		
//		String id  = request.getParameter(CommonConstants.CLIENT_ID_KEY);
//		String pwd = request.getParameter(CommonConstants.CLIENT_PWD_KEY);
//		ClientBean cb = new ClientBean();
//		cb.setId(id);
//		cb.setPassword(pwd);
//		// We need to implement a separate class for validating ids and pwds.
//		if ( !CredentialsValidator.validateClient(cb) ) {
//			RequestDispatcher dispatcher =
//					request.getRequestDispatcher(CommonConstants.INCORRECT_LOGIN_INFO);
//			dispatcher.forward(request, response);
//		}
//		else {
//			//request.setAttribute(CommonConstants.CLIENT_ID_KEY, id);
//			//request.setAttribute(CommonConstants.CLIENT_PWD_KEY, pwd);
//			HttpSession session = request.getSession();
//			session.setAttribute(CommonConstants.CLIENT_BEAN_KEY, cb);
//			RequestDispatcher dispatcher =
//					request.getRequestDispatcher(CommonConstants.CLIENT_BEFORE_MEAL_DATA_UPLOAD);
//			dispatcher.forward(request, response);
//		}
//	}
//	
//	/*
//	private boolean isLoginInfoValid(String id, String pwd, HttpServletRequest request, 
//			HttpServletResponse response) throws ServletException, IOException {
//		if ( id == null || pwd == null ) {
//			return false;
//		}
//		if ( "".equals(id) || "".equals(pwd) ) {
//			return false;
//		}
//		// check the database for the existence of the id and pwd.
//		
//		return true;
//	}
//	*/
//
//}
