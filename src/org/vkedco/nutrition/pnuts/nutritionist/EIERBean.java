package org.vkedco.nutrition.pnuts.nutritionist;

/*
 ************************************************
 * EIERBean.java
 * Author: Vladimir Kulyukin
 * An Energy Intake Estimation Request Bean.
 ************************************************
 */

public class EIERBean {
	
	protected int mReqId;
	protected String mClientId;
	
	public EIERBean() { mReqId = 0; mClientId = ""; }
	
	public int getReqId() {
		return mReqId;
	}
	
	public void setReqId(int req_id) {
		mReqId = req_id;
	}
	
	public String getClientId() {
		return mClientId;
	}
	
	public void setClientId(String client_id) {
		mClientId = client_id;
	}

}
