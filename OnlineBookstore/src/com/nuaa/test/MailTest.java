package com.nuaa.test;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Test;

import com.nuaa.util.MailUtils;

public class MailTest {

	@Test
	public void sendTest() throws AddressException, MessagingException {
		MailUtils mail = new MailUtils();
		mail.send("1094454100@qq.com","<a href='http://localhost:8080/SearchClServlet?dowhat=searchByRand'>²âÊÔµÄÓÊ¼ş</a>");
	}
	
}
