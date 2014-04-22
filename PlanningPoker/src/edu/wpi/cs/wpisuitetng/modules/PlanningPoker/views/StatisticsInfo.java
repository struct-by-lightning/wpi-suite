/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.util.Arrays;



/**
 * @author Legion
 *
 * @version $Revision: 1.0 $
 */
public class StatisticsInfo {
	/**
	 * Method mean.
	 * @param m double[]
	 * @return double
	 */
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	/**
	 * Method median.
	 * @param m double[]
	 * @return double
	 */
	public static double median(double[] m) {
		Arrays.sort(m);
	    int middle = m.length / 2;
	    if (m.length % 2 == 1) {
	        return m[middle];
	    } else {
	        return (m[middle - 1] + m[middle]) / 2.0;
	    }
	}
	
	/**
	 * Method mode.
	 * @param a double[]
	 * @return double
	 */
	public static double mode(double a[]) {
	    int maxCount = 0;
	    double  maxValue = 0;
	    
	    for (int i = 0; i < a.length; ++i) {
	        int count = 0;
	        for (int j = 0; j < a.length; ++j) {
	            if (a[j] == a[i]) ++count;
	        }
	        if (count > maxCount) {
	            maxCount = count;
	            maxValue = a[i];
	        }
	    }

	    return maxValue;
	}
	
	/**
	 * Method StdDev.
	 * @param m double[]
	 * @return double
	 */
	public static double StdDev(double[] m) {
		double avg = mean(m);
		double variance = 0;
		for (int i=0; i < m.length; i++)
		{
		    variance += Math.pow(m[i] - avg, 2);
		}
		return Math.sqrt(variance / (m.length - 1));
	}
	
	/**
	 * Method max.
	 * @param m double[]
	 * @return double
	 */
	public static double max(double[] m) {
		double max = m[0];
		for(int i = 0; i < m.length; i++) {
			if(m[i] > max) {
				max = m[i];
			}
		}
		return max;
	}
	
	/**
	 * Method min.
	 * @param m double[]
	 * @return double
	 */
	public static double min(double[] m) {
		double min = m[0];
		for(int i = 0; i < m.length; i++) {
			if(m[i] < min) {
				min = m[i];
			}
		}
		return min;
	}
}
