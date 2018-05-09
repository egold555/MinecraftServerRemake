package org.golde.test.util;

public class StringUtils {

	public static final String[] splitBySpace(String in) {
		return in.split("\\s+");
	}
	
	public static final String[] nudgeArrayDownByXRemovingFirstToLast(String[] in, int remove) {
		if(remove >= in.length) {
			return new String[0];
		}
		String[] toReturn = new String[in.length - remove];
		for(int i = 0; i < toReturn.length; i++) {
			toReturn[i] = in[i+remove];
		}
		return toReturn;
	}
	
}
