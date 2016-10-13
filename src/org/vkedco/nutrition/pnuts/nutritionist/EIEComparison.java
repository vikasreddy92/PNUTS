package org.vkedco.nutrition.pnuts.nutritionist;

import org.vkedco.nutrition.pnuts.nutritionist.EIEBean;

// This class contains a few methods for comparing 
// the Energy Intake Estimates from two different nutritionists
public class EIEComparison {

	// x and y are the ei estimates
	public static double relativeDiff(double x, double y) {
		if (x == 0 || y == 0)
			return Double.MAX_VALUE;

		return Math.abs(x - y) / Math.max(Math.abs(x), Math.abs(y));
	}

	// x and y are the ei estimates
	public static double relativeDiffPercent(double x, double y) {
		if (x == 0 || y == 0)
			return Double.MAX_VALUE;

		return Math.abs(x - y) / Math.max(Math.abs(x), Math.abs(y)) * 100.0;
	}

	// x and y are the ei estimates
	public static double relativeDiff(int x, int y) {
		return relativeDiff((double) x, (double) y);
	}

	// x and y are the ei estimates
	public static double relativeDiffPercent(int x, int y) {
		return relativeDiffPercent((double) x, (double) y);
	}
	
	public static double compareEIEBeans(EIEBean eieBean1, EIEBean eieBean2)
	{
		if(eieBean1.getReqId() == eieBean2.getReqId())
		{
			double difference = relativeDiffPercent(eieBean1.getCals(), eieBean2.getCals());
			return difference;
		}
		return Double.MAX_VALUE;
	}
}
