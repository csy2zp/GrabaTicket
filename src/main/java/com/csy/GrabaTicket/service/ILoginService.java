package com.csy.GrabaTicket.service;

public interface ILoginService {
	
	String init();
	
	boolean checkImg(String imgLocation);
	
	boolean login(String username,String password);

	void loginOut();
}
