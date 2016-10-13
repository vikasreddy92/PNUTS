package org.vkedco.nutrition.pnuts.coordinator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.vkedco.nutrition.pnuts.CommonConstants;


@Stateless
@LocalBean
public class EmailUtils {

	private static int port = 465;
	private static String host = "smtp.gmail.com";
	private static String from = "pnuts.usu@gmail.com";
	private static boolean auth = true;
	private static String username = "pnuts.usu@gmail.com";
	private static String password = "Project#2014";
	private static boolean debug = false;
	public static final ArrayList<String> receipients = new ArrayList<String>();


	public static void sendEmail(String subject,
			String body) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try{
			Class.forName(CommonConstants.JDBC_DRIVER);
			connection = DriverManager.getConnection(CommonConstants.DB_URL, CommonConstants.DB_USER, CommonConstants.DB_PWD);
			preparedStatement = connection.prepareStatement("select email from nutritionist where role=\"coordinator\"");
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				receipients.add(rs.getString("email"));
			}
			System.err.println("EmailUtils.sendEmail: retrieved email addresses: " + receipients);
		}catch(SQLException e){
			System.err.println("Error while retrieving email addresses" + e.getMessage());
		}
		finally{
			if(connection != null)
				connection.close();
			if(preparedStatement != null){
				preparedStatement.close();
			}
			
		}
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.ssl.enable", true);

		Authenticator authenticator = null;
		if (auth) {
			props.put("mail.smtp.auth", true);
			authenticator = new Authenticator() {
				private PasswordAuthentication pa = new PasswordAuthentication(
						username, password);

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return pa;
				}
			};
		}

		Session session = Session.getInstance(props, authenticator);
		session.setDebug(debug);

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setText(body);
			for (String receipient : receipients) {
				InternetAddress[] address = { new InternetAddress(receipient) };
				message.setRecipients(Message.RecipientType.TO, address);
				// Multipart multipart = new MimeMultipart("alternative");
				//
				// MimeBodyPart textPart = new MimeBodyPart();
				// String textContent = "Hi, Nice to meet you!";
				// textPart.setText(textContent);
				//
				// MimeBodyPart htmlPart = new MimeBodyPart();
				// String htmlContent =
				// "<html><h1>Hi</h1><p>Nice to meet you!</p></html>";
				// htmlPart.setContent(htmlContent, "text/html");
				//
				// multipart.addBodyPart(textPart);
				// multipart.addBodyPart(htmlPart);
				// message.setContent(multipart);
				Transport.send(message);
			}
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}
}
