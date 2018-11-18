package com.nuaa.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.nuaa.model.UserBean;
import com.nuaa.model.UserBeanCl;

@WebServlet("/MyInfoClServlet")
public class MyInfoClServlet extends HttpServlet {
	
	/**
	 * ������"������Ϣ"�������
	 * ���磺�����ݿ��ж�ȡ������Ϣ��������ҳ��
	 */
	private static final long serialVersionUID = 1L;

	public MyInfoClServlet() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//����֤�û��Ƿ��¼
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//��ȡ��������
		
		//���session�е���ʱ������
		clearOrderListInSession(username,session);
		
		if(username == null) //δ��¼����ת����¼����
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else if(dowhat.equals("updateMyInfo"))
		{				
			
			//�õ��ϴ��ļ��ı���Ŀ¼�����ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
			String savePath = this.getServletContext().getRealPath("/images/avatars");
			File file = new File(savePath);
			
			//�ж��ϴ��ļ��ı���Ŀ¼�Ƿ����
			if (!file.exists() && !file.isDirectory()) 
			{
				System.out.println(savePath+"Ŀ¼�����ڣ���Ҫ����");
				//����Ŀ¼
				file.mkdir();
			}
			
			//��Ϣ��ʾ
			String message = "";
			String uavatar="";
			UserBean updatedUser=null;
			try
			{
				UserBeanCl userBeanCl = new UserBeanCl();
				
				//ʹ��Apache�ļ��ϴ���������ļ��ϴ����裺
				//1������һ��DiskFileItemFactory����
				DiskFileItemFactory factory = new DiskFileItemFactory();
				//2������һ���ļ��ϴ�������
				ServletFileUpload upload = new ServletFileUpload(factory);
				//����ϴ��ļ�������������
				upload.setHeaderEncoding("UTF-8"); 
				//3���ж��ύ�����������Ƿ����ϴ���������
				if(!ServletFileUpload.isMultipartContent(request))
				{
					//���մ�ͳ��ʽ��ȡ����
					return;
				}
				//4��ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form����������
				List<FileItem> list = upload.parseRequest(request);
				for(FileItem item : list)
				{
					//���fileitem�з�װ������ͨ����
					if(item.isFormField())
					{
						String name = item.getFieldName();
						String value = item.getString("utf-8");
						request.setAttribute(name, value);
					}
					else //�ļ�����
					{						
						//���fileitem�з�װ�����ϴ��ļ�
						//����ͷ��ͼƬ�����ƣ�username.jpg��
						uavatar = username + ".jpg";
						
						//��û���ϴ�ͼƬ��������ǰ�Ǹ�������д�뵽�������ļ�������
						String oriFileName = item.getString();					
						if(oriFileName==null || oriFileName.trim().equals(""))
						{
							//��ȡ���ݿ���ͷ�������
							uavatar = userBeanCl.getUavatarByUid(username);

							continue;
						}
						
						//ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺 c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
						//�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
						uavatar = uavatar.substring(uavatar.lastIndexOf("\\")+1);
						//��ȡitem�е��ϴ��ļ���������
						InputStream in = item.getInputStream();
						//����һ���ļ������
						FileOutputStream out = new FileOutputStream(savePath + "\\" + uavatar);
						//����һ��������
						byte buffer[] = new byte[1024];
						//�ж��������е������Ƿ��Ѿ�����ı�ʶ
						int len = 0;
						//ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾin���滹������
						while((len=in.read(buffer))>0)
						{
							//ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\\" + filename)����
							out.write(buffer, 0, len);
						}
						//�ر�������
						in.close();
						//�ر������
						out.close();
						//ɾ�������ļ��ϴ�ʱ���ɵ���ʱ�ļ�
						item.delete();
						message = "�ļ��ϴ��ɹ���";
					}
				}
				
				//��ȡ���ļ�����
				String unickname = (String)request.getAttribute("nickname");
				String urealname = (String)request.getAttribute("realname");
				String usex =(String) request.getAttribute("sex");
				String uaddress = (String)request.getAttribute("address");
				String uphone = (String)request.getAttribute("phone");
				String uemail = (String)request.getAttribute("email");
				String ubirthday = (String)request.getAttribute("birthday");

				updatedUser = new UserBean(username, null, uavatar, unickname, urealname, usex, uaddress, uphone, uemail, ubirthday);
				userBeanCl.updateUser(updatedUser,uavatar);

			}catch (Exception e) {
				
				message= "�ļ��ϴ�ʧ�ܣ�";
				e.printStackTrace();			 
			}
			
			//ת��
			request.setAttribute("userBean", updatedUser);
			request.getRequestDispatcher("myinfo_page.jsp").forward(request, response);
			return;
		}
		else if(dowhat.equals("queryMyInfo"))//�����������Ϣ��ѡ� ��ֱ�ӵ��navi���û�����һ�ν���
		{
			//��ѯusername�û���Ϣ
			UserBeanCl userBeanCl = new UserBeanCl();
			UserBean userBean = userBeanCl.findUserByUid(username);

			//ת��
			request.setAttribute("userBean", userBean);
			request.getRequestDispatcher("myinfo_page.jsp").forward(request, response);
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//ɾ������session�е���ʱ������
	void clearOrderListInSession(String username,HttpSession session)
	{
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //ɾ����ʱ�Ķ�����
	}
}
