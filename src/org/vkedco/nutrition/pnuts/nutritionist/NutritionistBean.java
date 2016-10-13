package org.vkedco.nutrition.pnuts.nutritionist;

/*
 **************************************
 * NutritionistBean.java
 * Author: Vladimir Kulyukin
 * A bean implementation of a Nutritionist.
 ***************************************
 */

public class NutritionistBean {
	private String mId;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mPassword;
	private String mConfirmedPassword;
	private String mRole;
	
	public NutritionistBean() {}
	
	public String getId() { return mId; }
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getFirstName() { return mFirstName; }
	
	public void setFirstName(String fn) {
		mFirstName = fn;
	}
	
	public String getLastName() { return mLastName; }
	
	public void setLastName(String fn) {
		mLastName = fn;
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
	
	public String getRole() {
		return mRole;
	}
	
	public void setRole(String role) {
		mRole = role;
	}

}
