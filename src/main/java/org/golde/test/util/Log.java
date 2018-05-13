package org.golde.test.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Log {
	
	public static void dbg(String msg) {
		log(Colors.WHITE + "[DEBUG] " + msg);
	}

	public static void info(String msg) {
		log(Colors.CYAN + "[INFO] " + msg);
	}
	
	public static void warn(String msg) {
		log(Colors.YELLOW + "[WARNING] " + msg);
	}
	
	public static void err(String msg) {
		log(Colors.RED + "[ERROR] " + msg);
	}
	
	public static void packetIn(String msg) {
		log(Colors.GREEN + "[PACKET-IN] " + msg);
	}
	
	public static void packetOut(String msg) {
		log(Colors.PURPLE + "[PACKET-OUT] " + msg);
	}
	
	private static void log(String msg) {
		System.out.println(msg + Colors.RESET);
	}
	
	public static String toStringObj(Object obj) {
		return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	private static class Colors {
		public static final String RESET = "\u001B[0m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";
		public static final String YELLOW = "\u001B[33m";
		public static final String PURPLE = "\u001B[35m";
		public static final String CYAN = "\u001B[36m";
		public static final String WHITE = "\u001B[37m";
	}
	
}
