package com.qtitools.player.client.util;

public class StringUtils {
	public static String reverse(String source) {
		int i, len = source.length();
	    StringBuffer dest = new StringBuffer(len);

	    for (i = (len - 1); i >= 0; i--)
	      dest.append(source.charAt(i));
	    return dest.toString();
	}
}
