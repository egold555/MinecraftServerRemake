package org.golde.test;

public class _Main {

	private static SingleInstance instance;
	
	public static void main(String[] args) {
		try {
			instance = new SingleInstance();
			instance.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SingleInstance getInstance() {
		return instance;
	}

}
