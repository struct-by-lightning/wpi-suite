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
 * @version $Revision: 1.0 $
 * @author friscis
 */
public class Statistics {
	/**
	 * @param m input array
	 * @return mean
	 */
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
	/**
	 * @param m input array
	 * @return median
	 */
	public static double median(double[] m) {
		Arrays.sort(m);
	    final int middle = m.length / 2;
	    if (m.length % 2 == 1) {
	        return m[middle];
	    } else {
	        return (m[middle - 1] + m[middle]) / 2.0;
	    }
	}
	
	/**
	 * @param m input array
	 * @return mode
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
	 * @param m input array
	 * @return standard deviation
	 */
	public static double StdDev(double[] m) {
		final double avg = mean(m);
		double variance = 0;
		for (int i=0; i < m.length; i++)
		{
		    variance += Math.pow(m[i] - avg, 2);
		}
		return Math.sqrt(variance / (m.length - 1));
	}
	
	/**
	 * @param m input array
	 * @return maximum value
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
	 * @param m input array
	 * @return minimum value
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
