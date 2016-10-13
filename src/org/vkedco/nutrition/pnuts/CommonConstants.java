package org.vkedco.nutrition.pnuts;

import java.io.File;

public class CommonConstants {
	
	public static final String CLIENT_REQUEST_KEY 	 = "pnuts_client";	
	public static final String CLIENT_ID_KEY		 = "client_id";
	public static final String CLIENT_PWD_KEY 		 = "client_pwd";
	public static final String CLIENT_BEAN_KEY 		 = "client_bean";
	
	public static final String NUT_REQUEST_KEY = "pnuts_nutritionist";
	public static final String NUT_ID_KEY 	   = "nut_id";
	public static final String NUT_PWD_KEY 	   = "nut_pwd";
	public static final String NUT_BEAN_KEY    ="nut_bean";
	
	public static final String PC_ID_KEY = "program_coordinator_id";
	public static final String PC_PWD_KEY = "program_coordinator_pwd";
	
	public static final String REQ_ID_KEY = "req_id";
	public static final String UPLOAD_MSG_KEY1 = "upload_message1";
	public static final String UPLOAD_MSG_KEY2 = "upload_message2";
	public static final String DOWNLOAD_MSG_KEY = "download_message";
	public static final String EIE_BEAN_KEY = "eie_bean";
	public static final String ERROR_MSG_KEY = "error_message";
	public static final String THANK_YOU_KEY = "thank_you_message";
	
	public static final String DB_URL 	= "jdbc:mysql://localhost/pnuts";
	public static final String DB_USER 	= "root";
	public static final String DB_PWD 	= "PNUTS2014";
	
	public static final String BEFORE_MEAL_FILE_PATH = "beforeMealFilePath";
	public static final String AFTER_MEAL_FILE_PATH  = "afterMealFilePath";

	public static final String TEST_CLIENT_PAGE 			= "/WEB-INF/Test/ClientEcho.jsp";
	public static final String TEST_NUTRITIONIST_PAGE 		= "/WEB-INF/Test/NutritionistEcho.jsp";
	public static final String TEST_EIE_PAGE 				= "/WEB-INF/Test/EIEstimationEcho.jsp";

	
	public static final String THANK_YOU_PAGE 				= "/WEB-INF/Message/ThankYou.jsp";
	public static final String GENERIC_THANK_YOU_PAGE 		= "/WEB-INF/Message/GenericThankYou.jsp";
	public static final String REGISTRATION_FAILURE_PAGE 	= "/WEB-INF/Error/RegistrationFailure.jsp";
	public static final String ID_EXISTS_PAGE 				= "/WEB-INF/Error/IDExists.jsp";
	public static final String MISSING_ID_PAGE 				= "/WEB-INF/Error/MissingID.jsp";
	public static final String MISSING_PASSWORD_PAGE 		= "/WEB-INF/Error/MissingPassword.jsp";
	public static final String MISSING_EMAIL_PAGE 			= "/WEB-INF/Error/MissingEmail.jsp";
	public static final String PASSWORD_MISMATCH_PAGE 		= "/WEB-INF/Error/PasswordMismatch.jsp";
	public static final String MISSING_FIRST_NAME			= "/WEB-INF/Error/MissingFirstName.jsp";
	public static final String MISSING_LAST_NAME			= "/WEB-INF/Error/MissingLastName.jsp";
	public static final String INCORRECT_LOGIN_INFO			= "/WEB-INF/Error/IncorrectLoginInfo.jsp";
	public static final String INCORRECT_CREDENTIALS_INFO	= "/WEB-INF/Error/IncorrectCredentialsInfo.jsp";
	public static final String CLIENT_BEFORE_MEAL_DATA_UPLOAD = "/WEB-INF/Client/ClientBeforeMealDataUpload.jsp";
	public static final String CLIENT_AFTER_MEAL_DATA_UPLOAD = "/WEB-INF/Client/ClientAfterMealDataUpload.jsp";
	public static final String CLIENT_MEAL_DATA_UPLOAD 		= "/WEB-INF/Client/ClientMealDataUpload.jsp";
	public static final String ILLEGAL_CLIENT_ACCESS        = "/WEB-INF/Error/IlleagalClientAccess.jsp";
	public static final String ERROR_PAGE 					= "/WEB-INF/Error/Error.jsp";
	public static final String INCORRECT_NUT_LOGIN_INFO 	= "/WEB-INF/Error/IncorrectNutritionistLoginInfo.jsp";
	public static final String FILE_DOWNLOAD_ERROR_PAGE = "/WEB-INF/Error/FileDownloadError.jsp";
	public static final String FILE_UPLOAD_RESPONSE_PAGE    = "/WEB-INF/Message/FileUploadResponse.jsp";
	
	
	
	public static final String CLIENT_DATA_DIR 	= System.getProperty("user.home")+File.separator+"Clients";
	public static final String NUT_DATA_DIR 	= System.getProperty("user.home")+File.separator+"Nuts";
	
//	public static final String CLIENT_DATA_DIR 	= "file:///C:/PNUTS/Clients";
//	public static final String NUT_DATA_DIR 	= "C:\\PNUTS\\Nuts";
	
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	//public static final String CLIENT_VIDEO_DIRECTORY = "/home/jboss_csatlnode02/PNUTS/ClientVideos";
	
	public static final String CLIENT_LIST = "Clients";
	public static final String NUTRITIONIST_LIST = "Nutritionists";
	
	public static final String USDA_SITE = "http://ndb.nal.usda.gov";
	public static final String DOWNLOAD_FILE_SERVLET = "DownloadFile";
	public static final String EVAL_EIER_SERVLET = "EvaluateEIER";
	public static final String SAVE_EIER_EVAL_SERVLET = "SaveEIEstimation";	
	public static final String PendingEIERList = "PendingEIERList";
	public static final String CONFLICTING_ESTIMATIONS_SERVLET = "ConflictingEstimations";
	
	public static final int EIE_NUM_ROWS = 10;
	public static final int EIE_NUM_COLS = 4;
	public static final double ACCEPTABLEFACTOR = 10;
	public static final String EIER_BEAN_KEY = "eier_bean";
	public static final String PC_BEAN_KEY = "pc_bean";
	
	//public static final String[] receipients = {"vladimir.kulyukin@aggiemail.usu.edu", "jen.day@aggiemail.usu.edu", "s.vikasreddy2009@gmail.com"};
	public static final String EMAIL_SUBJECT = "PNUTS: Conflicting estimations - Request ID: ";
	public static final String EMAIL_TEXT = "There was a conflict among nutritionists' estimations, who's request ID is: ";
	
	
	public static final String INVALID_INFO_CREDENTIALS_MSG = "Invalid information/credentials";
	public static final String FILE_NAME_CANNOT_BE_EMPTY_MSG = "file name cannot be empty or null";
	public static final String FILE_NAME_DOES_NOT_EXIST_MSG = "file does not exist on server";
	public static final String FILE_NAME_PARAM = "fileName";
	public static final String DATA_LINK_NAME = "data";
	public static final String CLIENT_SERVLET = "ClientInformation";
	public static final String DOWNLOAD_CLIENT = "download_client";
	public static final String DOWNLOAD_NUT    = "download_nuts";
	
	
}

