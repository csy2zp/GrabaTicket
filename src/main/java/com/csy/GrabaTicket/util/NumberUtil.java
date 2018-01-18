package com.csy.GrabaTicket.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

	public static Boolean isValid(String phone)
	{ 	
		if(phone == null) {
			return false;
		}
		String regExp = "^[1-8]{1,4}$";
		Pattern p = Pattern.compile(regExp); 
		Matcher m = p.matcher(phone);
		return m.find();//boolean  
	}
	
	public static void main(String[] args) {
		System.out.println("11|ss|ss".split("\\|")[1]);
	}
}
