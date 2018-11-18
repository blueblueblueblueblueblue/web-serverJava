package com.nuaa.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ����cookie
 * @author chicken
 *
 */
public class CookieUtil {
	
	//���һ��name,value��cookie
	public void addCookie(HttpServletRequest request,HttpServletResponse response, String name,String value)
	{
		Cookie cookie=new Cookie(name, value);
		cookie.setMaxAge(3600*24);
		removeCookie(request,name); //�����ں�nameһ����cookie����ɾ����
		response.addCookie(cookie);
	}
	
	//ɾ��name��Ӧ��cookie
	public void removeCookie(HttpServletRequest request,String name){

		Cookie[] cookies=request.getCookies();
		for(int i=0;i<cookies.length;i++)
		{
			if(cookies[i].getName().equals(name))
			{
				cookies[i].setMaxAge(0);
				return;
			}
		}
	}
	
	//���һ��name��cookie
	public Cookie getCookie(HttpServletRequest request,String name)
	{
		Cookie[] cookies=request.getCookies();
		for(int i=0;i<cookies.length;i++)
		{
			if(cookies[i].getName().equals(name))
				return cookies[i];
		}
		return null;
	}
}
