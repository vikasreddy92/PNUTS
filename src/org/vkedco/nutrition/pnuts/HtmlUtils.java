package org.vkedco.nutrition.pnuts;

import javax.servlet.http.HttpServletRequest;

public class HtmlUtils {
	
	public static final String BEGIN_LI = "<LI>";
	public static final String BEGIN_HTML = "<HTML>";
	public static final String BEGIN_H1_ALIGN = "<H1 ALIGN=";
	public static final String BEGIN_BODYBGCOLOR = "<BODY BGCOLOR=";
	public static final String BEGIN_TABLE_BORDER = "<TABLE BORDER=";
	public static final String BEGIN_TRBGCOLOR = "<TR BGCOLOR=";
	public static final String BEGIN_TR = "<TR>";
	public static final String BEGIN_TH = "<TH>";
	public static final String BEGIN_TD = "<TD>";
	public static final String BEGIN_AHREF = "<A HREF=";
	public static final String BEGIN_IFRAME = "<IFRAME ";
	public static final String BEGIN_UL = "<UL>";
	public static final String BEGIN_PARA = "<P>";
	
	public static final String TEXT_HTML = "text/html";
	public static final String CENTER = "CENTER";
	public static final String BEGIN_CENTER = "<CENTER>";
	public static final String END_CENTER = "</CENTER>";
	public static final String END_TABLE = "</TABLE>";
	public static final String BEGIN_FORM_ACTION = "<FORM ACTION=\"";
	public static final String BEGIN_INPUT_TYPE = "<INPUT TYPE=\"";
	
	public static final String END_LI = "</LI>";
	public static final String END_HTML = "</HTML>";
	public static final String END_BODY = "</BODY>";
	public static final String END_FORM = "</FORM>";
	public static final String END_TR = "</TR>";
	public static final String END_TH = "</TH>";
	public static final String END_H1 = "</H1>";
	public static final String END_TD = "</TD>";
	public static final String END_AHREF = "</A>";
	public static final String END_IFRAME = "</IFRAME>";
	public static final String END_UL = "</UL>";
	public static final String END_PARA = "</P>";
	
	public static final String BR = "<BR>";
	public static final String HR = "<HR>";
	public static final String NAME_EQLS = "NAME=\"";
	public static final String VALUE_EQLS = "VALUE=\"";
	public static final String SUBMIT = "SUBMIT";
	public static final String RESET = "RESET";
	public static final String SPACE = " ";
	
	public static final String WIDTH_EQLS = "width=";
	public static final String HEIGHT_EQLS = "height=";
	public static final String SRC_EQLS = "src=";	
	
	public static final String GREATER_THAN = ">";
	public static final String NEWLINE = "\n";
	public static final String ESCAPE_DBLQT = "\"";
	
	public static final String FDF5E6 = "#FDF5E6";
	public static final String FFAD00 = "#FFAD00";
	public static final String DEFAULT_BODY_COLOR = "#FDF5E6";
	
	public static final String DOCTYPE =
			"<!doctype html public \"-//w3c//dtd html 4.0 " +
	    	         "transitional//en\">\n";
	
	public static String headWithTitle(String title) {
		return "<head><title>" + title + "</title></head>\n";
	}
	
	public static String ULLI(String li) {
		return BEGIN_LI + li + END_LI;
	}
	
	public static String beginBodyBgColor(String color) {
		return BEGIN_BODYBGCOLOR + ESCAPE_DBLQT +
				color + ESCAPE_DBLQT + GREATER_THAN + NEWLINE;
	}
	
	public static String beginHTML() {
		return BEGIN_HTML + NEWLINE;
	}
	
	public static String endHTML() {
		return END_HTML + NEWLINE;
	}
	
	public static String endBody() {
		return END_BODY + NEWLINE;
	}
	
	public static String beginH1Align(String align) {
		return BEGIN_H1_ALIGN + ESCAPE_DBLQT + align +
				ESCAPE_DBLQT + GREATER_THAN + NEWLINE;
	}
	
	public static String endH1() {
		return END_H1 + NEWLINE;
	}
	
	public static String h1AlignCenter(String item) {
		return beginH1Align(CENTER) + item + endH1();
	}
	
	public static String endTable() {
		return END_TABLE + NEWLINE;
	}
	
	public static String beginForm(String action_url) {
		return BEGIN_FORM_ACTION + action_url + ESCAPE_DBLQT + GREATER_THAN + NEWLINE;
	}
	
	public static String endForm() {
		return END_FORM + NEWLINE;
	}
	
	public static String beginCenter() {
		return BEGIN_CENTER + NEWLINE;
	}
	
	public static String endCenter() {
		return END_CENTER + NEWLINE;
	}
	
	public static String beginTableWithBorder(int border) {
		return BEGIN_TABLE_BORDER + border + GREATER_THAN + NEWLINE;
	}
	
	public static String beginTRBgColor(String color) {
		return BEGIN_TRBGCOLOR + ESCAPE_DBLQT + color + ESCAPE_DBLQT 
				+ GREATER_THAN + NEWLINE;
	}
	
	public static String beginTR() {
		return BEGIN_TR + NEWLINE;
	}
	
	public static String endTR() {
		return END_TR + NEWLINE;
	}
	
	public static String TD(String contents) {
		return BEGIN_TD + contents + END_TD + NEWLINE;
	}
	
	public static String TH(String contents) {
		return BEGIN_TH + contents + END_TH + NEWLINE;
	}
	
	public static String AHREF(String link, String link_name) {
		return BEGIN_AHREF + 
				ESCAPE_DBLQT + link + ESCAPE_DBLQT +
				GREATER_THAN + link_name + END_AHREF;
	}
	
	
	// <input type="text" name="newItem" value="Yacht"><p>
	// <input type="submit" value="Order and Show All Purchases">
	public static String emptyInputRow(int num_cols) {
		StringBuffer sb = new StringBuffer();
		sb.append(HtmlUtils.beginTR());
		for(int i = 0; i < num_cols; i++) {
			sb.append(HtmlUtils.TD("<input type=\"text\" name=\"" +
								   "ingredient" + i +  "value\"\">"));
		}
		sb.append(HtmlUtils.endTR());
		return sb.toString();
	}
	
	public static String emptyInputRows(int num_rows, int num_cols) {
		StringBuffer sb = new StringBuffer();
		for(int r = 0; r < num_rows; r++) {
			sb.append(emptyInputRow(num_cols));
		}
		return sb.toString();
	}
	
	public static String emptyInputRows1(int num_rows, int num_cols){
		StringBuffer sb = new StringBuffer();
		for( int r = 0; r < num_rows; r++){
			StringBuffer sbnew = new StringBuffer();
			sbnew.append(HtmlUtils.beginTR());
			for( int c = 0; c < num_cols; c++ ){
				sbnew.append(HtmlUtils.TD("<input type=\"text\" name=\"" +
						   "ingredient" +"_"+ r + "_" + c +  "value\"\">"));			
			}
			sbnew.append(HtmlUtils.endTR());
			sb.append(sbnew.toString());
		}
		return sb.toString();
	}
	
	
	//"First Name:\n" +
	//" <INPUT TYPE=\"TEXT\" NAME=\"firstName\" " +
	//" VALUE=\"" + FIRST_NAME + "\"><BR>\n"
	public static String typedInput(String text_label, 
			String type, String name, String value) {
		return text_label + NEWLINE + 
				BEGIN_INPUT_TYPE + type + ESCAPE_DBLQT +
				NAME_EQLS + name + ESCAPE_DBLQT +
				VALUE_EQLS + value + ESCAPE_DBLQT + GREATER_THAN 
				+ BR + NEWLINE;
	}
	
	public static String inputSubmit(String value) {
		return BEGIN_INPUT_TYPE + SUBMIT + ESCAPE_DBLQT + 
				SPACE +
				VALUE_EQLS + value + ESCAPE_DBLQT +
				SPACE +
				GREATER_THAN + NEWLINE;
	}
	
	public static String inputReset() {
		return BEGIN_INPUT_TYPE + RESET + ESCAPE_DBLQT + 
			   GREATER_THAN + NEWLINE;
	}
	
	private static boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ( (input != null) && (input.length() > 0) ) {
			char c;
			for(int i = 0; i < input.length(); i++) {
				c = input.charAt(i);
				switch ( c ) {
				case '<': flag = true; break;
				case '>': flag = true; break;
				case '"': flag = true; break;
				case '&': flag = true; break;
				}
			}
		}
		return flag;
	}
	
	public static String filter(String input) {
		if ( !hasSpecialChars(input) ) 
			return input;
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for(int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch ( c ) {
			case '<': filtered.append("&lt;"); break;
			case '>': filtered.append("&gt;"); break;
			case '"': filtered.append("&quot;"); break;
			case '&': filtered.append("&amp;"); break;
			default: filtered.append(c);
			}
		}
		return filtered.toString();
	}
	
	public static int getIntParameter(HttpServletRequest request, 
			String paramName, int defaultValue) {
		String paramString = request.getParameter(paramName);
		int paramValue;
		try {
			paramValue = Integer.parseInt(paramString);
		}
		catch ( NumberFormatException nfe ) {
			paramValue = defaultValue;
		}
		return paramValue;
	}
			

}
