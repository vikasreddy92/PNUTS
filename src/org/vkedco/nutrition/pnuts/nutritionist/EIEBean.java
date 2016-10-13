package org.vkedco.nutrition.pnuts.nutritionist;

import java.util.HashMap;
import java.util.Map.Entry;

public class EIEBean {
	
	private int mReqId;
	private String mNutId;
	private String mFileName;
	private int mCals;
	private java.sql.Timestamp mTimestamp;
	private HashMap<Integer, IngredientBean> mIngredientTable; 
	
	public EIEBean() {
		mReqId = 0;
		mNutId = "";
		mFileName = "";
		mCals = 0;
		mTimestamp = null;
	}
	
	public int getReqId() {
		return mReqId;
	}
	
	public void setReqId(int req_id) {
		mReqId = req_id;
	}
	
	public String getNutId() {
		return mNutId;
	}
	
	public void setNutId(String nut_id) {
		mNutId = nut_id;
	}
	
	public String getFileName() {
		return mFileName;
	}
	
	public void setFileName(String fn) {
		mFileName = fn;
	}
	
	public java.sql.Timestamp getTimestamp() {
		return mTimestamp;
	}
	
	public void setTimestamp(java.sql.Timestamp st) {
		mTimestamp = st;
	}
	
	public int getCals() {
		return mCals;
	}
	
	public void setCals(int cals) {
		mCals = cals;
	}
	
	public HashMap<Integer, IngredientBean> getIngredientTable() {
		return mIngredientTable;
	}
	
	public void setIngredientTable(HashMap<Integer, IngredientBean> table) {
		mIngredientTable = table;
	}
	
	public String getIngredientTableString() {
		if ( mIngredientTable == null ) return "NULL";
		StringBuffer sb = new StringBuffer();
		for(Entry<Integer, IngredientBean> entry: mIngredientTable.entrySet()) {
			sb.append(entry.getValue().toString() + "\n");
		}
		return sb.toString();
		
	}
	
	public void setIngredientTableString() {
		// has no effect
	}
	
	public boolean isValid() {
		boolean rslt = ( mReqId > 0 &&
					!"".equals(mNutId) &&
					!"".equals(mFileName) &&
					mTimestamp != null && 
					mIngredientTable != null );
		if ( !rslt ) return false;
		if ( mIngredientTable.isEmpty() ) return false;
		for(Entry<Integer, IngredientBean> entry : mIngredientTable.entrySet()) {
			if ( !entry.getValue().isValid() )
				return false;
		}
		System.err.println("EIEBean: isValid(): "+rslt);
		return true;
	}
}