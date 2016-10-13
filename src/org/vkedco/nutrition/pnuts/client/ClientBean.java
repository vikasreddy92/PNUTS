package org.vkedco.nutrition.pnuts.client;

/*
 *****************************************
 * ClientBean.java
 * 
 * This is a Bean that implements a client object.
 * Author: Vladimir Kulyukin
 *
 *****************************************
 */

public class ClientBean {
	
	private String mId;
	private String mEmail;
	private String mPassword;
	private String mConfirmedPassword;
	private String mNutritionTipDay;
	private String mNutritionTipTime;
	//private String mCaloricSummaryDay;
	private String mCaloricSummaryTime;
	
	public ClientBean() {}
	
	public String getId() { return mId; }
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getEmail() { return mEmail; }
	
	public void setEmail(String email) {
		mEmail = email;
	}
	
	public String getPassword() { return mPassword; }
	
	public void setPassword(String pwd) {
		mPassword = pwd;
	}
	
	public String getConfirmedPassword() {
		return mConfirmedPassword;
	}
	
	public void setConfirmedPassword(String pwd) {
		mConfirmedPassword = pwd;
	}
	
	public String getNutritionTipDay() {
		return mNutritionTipDay;
	}
	
	public void setNutritionTipDay(String day) {
		mNutritionTipDay = day;
	}
	
	public String getNutritionTipTime() {
		return mNutritionTipTime;
	}
	
	public void setNutritionTipTime(String time) {
		mNutritionTipTime = time; 
	}
	
	/*
	public String getCaloricSummaryDay() {
		return mCaloricSummaryDay;
	}
	
	public void setCaloricSummaryDay(String day) {
		mCaloricSummaryDay = day;
	}
	*/
	
	public String getCaloricSummaryTime() {
		return mCaloricSummaryTime;
	}
	
	public void setCaloricSummaryTime(String time) {
		mCaloricSummaryTime = time; 
	}
}
