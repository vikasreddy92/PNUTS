package org.vkedco.nutrition.pnuts.nutritionist;

public class IngredientBean {
	
	private String mCode;
	private String mName;
	private int mGrams;
	private int mCals;
	
	public IngredientBean() {
		mCode = null;
		mName = null;
		mGrams = 0;
		mCals = 0;
	}
	
	public String getCode() {
		return mCode;
	}
	
	public void setCode(String code) {
		mCode = code;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public int getGrams() {
		return mGrams;
	}
	
	public void setGrams(int grams) {
		mGrams = grams;
	}
	
	public int getCals() {
		return mCals;
	}
	
	public void setCals(int cals) {
		mCals = cals;
	}
	
	public String toString() {
		return mName + "\t" + mGrams + "\t" + mCals;
	}
	
	public boolean isValid() {
		return mName != null;
	}
	

}
