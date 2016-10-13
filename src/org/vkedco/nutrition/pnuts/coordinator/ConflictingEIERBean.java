package org.vkedco.nutrition.pnuts.coordinator;

import java.sql.Timestamp;

import org.vkedco.nutrition.pnuts.nutritionist.EIERBean;

public class ConflictingEIERBean extends EIERBean{
	public ConflictingEIERBean() {
		super();
		mReqId = 0;
		mClientId = "";
		beforeMealFilePath = "";
		afterMealFilePath = "";
	}
	
	private String beforeMealFilePath;
	private String afterMealFilePath;
	private Timestamp beforeMealTimestamp;
	private Timestamp afterMealTimestamp;

	public String getBeforeMealFilePath() {
		return beforeMealFilePath;
	}

	public void setBeforeMealFilePath(String beforeMealFilePath) {
		this.beforeMealFilePath = beforeMealFilePath;
	}

	public String getAfterMealFilePath() {
		return afterMealFilePath;
	}

	public void setAfterMealFilePath(String afterMealFilePath) {
		this.afterMealFilePath = afterMealFilePath;
	}

	public Timestamp getBeforeMealTimestamp() {
		return beforeMealTimestamp;
	}

	public void setBeforeMealTimestamp(Timestamp beforeMealTimestamp) {
		this.beforeMealTimestamp = beforeMealTimestamp;
	}

	public Timestamp getAfterMealTimestamp() {
		return afterMealTimestamp;
	}

	public void setAfterMealTimestamp(Timestamp afterMealTimestamp) {
		this.afterMealTimestamp = afterMealTimestamp;
	}
}
