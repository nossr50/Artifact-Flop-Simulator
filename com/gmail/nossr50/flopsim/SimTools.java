package com.gmail.nossr50.flopsim;

import java.util.Random;

public class SimTools {
	public static int getRandom(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max + 1) + min;
	}
	
	public static String getShorter(String bigString, int maxLength)
	{
		String editedString = bigString.replace('_', ' '); //Replace underscores
		
		String start = editedString.substring(0, 1).toUpperCase();
		
		String end = "";
		
		if(editedString.length() < maxLength)
			end = editedString.substring(1);
		else
			end = editedString.substring(1, maxLength).toLowerCase();
		
		return start+end;
	}

	public static String getDebugFormat(String debugName, String debugInfo)
	{
		String prefix = " [";
		String suffix = "] ";
		String combined = prefix + debugName + ": " + debugInfo + suffix;
		return combined;
	}
}
