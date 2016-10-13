package org.vkedco.nutrition.pnuts;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class of UploadFile
 * Author: Vladimir Kulyukin
 * Servlet that uploads files into UPLOAD_DIRECTORY on the server.
 */
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "/home/jboss_csatlnode05/PNUTS/ClientVideos";
	private String mUploadFilePath = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ( ServletFileUpload.isMultipartContent(request) ) {
			
			
			try {
				List<FileItem> multiparts = 
						new ServletFileUpload(
								new DiskFileItemFactory()).parseRequest(request);
				for(FileItem item : multiparts) {
					if ( !item.isFormField() ) {
						String name = new File(item.getName()).getName();
						mUploadFilePath = UPLOAD_DIRECTORY + File.separator + name;
						item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
					}
				}
				request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1, "File Uploaded Successfully to " + mUploadFilePath);
			}
			catch ( Exception ex ) {
				request.setAttribute(CommonConstants.UPLOAD_MSG_KEY1, "File failed to upload to " + mUploadFilePath + " exception: " + ex.toString());
			}
			
			request
				.getRequestDispatcher("/WEB-INF/Message/FileUploadResponse.jsp")
				.forward(request, response);
		}
	}

}
