package org.vkedco.nutrition.pnuts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadFile Author: Vladimir Kulyukin Servlet
 * that downloads files from UPLOAD_DIRECTORY on the server.
 */
@WebServlet("/DownloadFile")
public class DownloadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String DOWNLOAD_DIRECTORY;
	private String mDownloadFilePath = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadFile() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		String downloadKey = request.getParameter("downloadKey");
		if (fileName == null || fileName.equals("")) {
			request.setAttribute(CommonConstants.DOWNLOAD_MSG_KEY,
					CommonConstants.FILE_NAME_CANNOT_BE_EMPTY_MSG);
			request.getRequestDispatcher(
					CommonConstants.FILE_DOWNLOAD_ERROR_PAGE).forward(request,
					response);
			return;
		}
		if (downloadKey == CommonConstants.DOWNLOAD_CLIENT) {
			DOWNLOAD_DIRECTORY = "C:"+File.separator+"PNUTS"+File.separator+"Clients";
		} else if (downloadKey == CommonConstants.DOWNLOAD_NUT) {
			DOWNLOAD_DIRECTORY = "C:"+File.separator+"PNUTS"+File.separator+"Nuts";
		}
		mDownloadFilePath = DOWNLOAD_DIRECTORY + File.separator + fileName;
		System.err.println("DownloadFile.doGet: " + mDownloadFilePath);
		File file = new File(mDownloadFilePath);
		if (!file.exists()) {
			request.setAttribute(CommonConstants.DOWNLOAD_MSG_KEY,
					CommonConstants.FILE_NAME_DOES_NOT_EXIST_MSG);
			request.getRequestDispatcher(
					CommonConstants.FILE_DOWNLOAD_ERROR_PAGE).forward(request,
					response);
			return;
		}
		ServletContext sctx = getServletContext();
		InputStream fis = new FileInputStream(file);
		String mimeType = sctx.getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null ? mimeType
				: "application/octet-stream");
		response.setContentLength((int) file.lastModified());
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ fileName + "\"");

		ServletOutputStream os = response.getOutputStream();
		byte[] dataBuffer = new byte[4096];
		int read = 0;
		while ((read = fis.read(dataBuffer)) != -1) {
			os.write(dataBuffer, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
