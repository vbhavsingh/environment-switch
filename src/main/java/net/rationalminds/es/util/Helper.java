package net.rationalminds.es.util;

import java.util.Arrays;
import java.util.List;

public class Helper {
	private static boolean status = true;
	
	public synchronized static void print() {
		if (status && isEnvSwitchEnabled()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(System.getProperty("line.separator"));
			buffer.append("*****************************************************************");
			buffer.append(System.getProperty("line.separator"));
			buffer.append("ENVIRONMENT SWITCHING ENABLED. CURRENT ENV IS ");
			buffer.append(getEnvironemnt());
			buffer.append(System.getProperty("line.separator"));
			buffer.append("*****************************************************************");
			System.out.println(buffer.toString());
			
			//ControllerClient cc = new ControllerClient();
			//cc.enableListner();
			status = false;
		}
	}
	/**
	 * 
	 * @return
	 */
	public static String getEnvironemnt() {
		String env = System.getProperty("env.switch");
		if(allowedEnvs.contains(env)) {
			return env.toUpperCase();
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isEnvSwitchEnabled() {
	   if("".equals(getEnvironemnt())) {
		   return false;
	   }
	   return true;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean isdevEnvironment() {
		  if(isEnvSwitchEnabled()) {
			  String env = System.getProperty("env.switch");
			  if(env.equals("dev") || env.equals("development")) {
				  return true;
			  }
		  }
		  return false;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean isTestEnvironment() {
		  if(isEnvSwitchEnabled()) {
			  String env = System.getProperty("env.switch");
			  if(env.equals("test") || env.equals("testing")) {
				  return true;
			  }
		  }
		  return false;
	}
	
	private static List<String> allowedEnvs= Arrays.asList("dev","development","test","testing");

}
