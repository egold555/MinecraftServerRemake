package org.golde.test;

public class StringUtils {

	public static String[] splitBySpace(String in) {
		return in.split("\\s+");
	}
	
	public static String[] nudgeArrayDownByXRemovingFirstToLast(String[] in, int remove) {
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
