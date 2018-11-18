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

import com.nuaa.model.ShopBean;
import com.nuaa.model.ShopBeanCl;
import com.nuaa.model.UserBean;
import com.nuaa.model.UserBeanCl;
import com.nuaa.util.C;

@WebServlet("/ShopInfoClServlet")
public class ShopInfoClServlet extends HttpServlet {
	
	/**
	 * ������"������Ϣ"�������
	 * 1.�����ݿ��ж�ȡ������Ϣ
	 * 2.д�����ݿ������Ϣ
	 */
	private static final long serialVersionUID = 1L;

	public ShopInfoClServlet() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//����֤�û��Ƿ��¼
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//��ȡ��������
		
		if(username == null) //δ��¼����ת����¼����
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else
		{
			ShopBeanCl shopBeanCl = new ShopBeanCl();
			if(dowhat.equals("openShop")) //���û����󿪵�
			{
				//���п��괦��
				shopBeanCl.addShop(username);
				request.getRequestDispatcher("ShopInfoClServlet?dowhat=queryShopInfo").forward(request, response);
			}
			else //������Ҫ�������̵�����
			{
				//������֤���û���û�п���
				int sid = shopBeanCl.getSidByUid(username);
				request.setAttribute("Sid", sid); //����jspҳ��ʶ���û��Ƿ񿪵� ������ʾ��ͬ����
				
				if(sid == C.OPENED_SHOP_NO) //û������ֱ�ӷ��ؽ�����һ������Ҫ���ꡱ��ť�Ľ���
				{
					request.getRequestDispatcher("shopinfo_page.jsp").forward(request, response);
					return;
				}
				
				//���˵��
				if(dowhat.equals("updateShopInfo"))
				{				
					
					//�õ��ϴ��ļ��ı���Ŀ¼�����ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
					String savePath = this.getServletContext().getRealPath("/images/shopicons");
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
					String sicon="";
					ShopBean updatedShop=null;
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
								sicon = sid + ".jpg";
								
								//��û���ϴ�ͼƬ��������ǰ�Ǹ�������д�뵽�������ļ�������
								String oriFileName = item.getString();					
								if(oriFileName==null || oriFileName.trim().equals(""))
								{
									//��ȡ���ݿ���ͼ�������
									sicon = shopBeanCl.getSiconBySid(sid);

									continue;
								}
								
								//ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺 c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
								//�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
								sicon = sicon.substring(sicon.lastIndexOf("\\")+1);
								//��ȡitem�е��ϴ��ļ���������
								InputStream in = item.getInputStream();
								//����һ���ļ������
								FileOutputStream out = new FileOutputStream(savePath + "\\" + sicon);
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
						String sname = (String)request.getAttribute("shopname");
						float stransprice = Float.parseFloat((String)request.getAttribute("transprice"));
						String ssummary = (String) request.getAttribute("summary");
						String sactivity = (String)request.getAttribute("activity");
						
						//�������ݿ�
						updatedShop = new ShopBean(sid, sname, username, sicon, ssummary, sactivity, stransprice);
						shopBeanCl.updateShop(updatedShop,sicon);
		
					}catch (Exception e) {
						
						message= "�ļ��ϴ�ʧ�ܣ�";
						e.printStackTrace();			 
					}
					
					//ת��
					request.setAttribute("shopBean", updatedShop);
					request.getRequestDispatcher("shopinfo_page.jsp").forward(request, response);
					return;
				}
				else if(dowhat.equals("queryShopInfo"))//�����������Ϣ��ѡ�
				{
					//��ѯusername�û���Ϣ
					ShopBean shopBean = shopBeanCl.findShopBySid(sid);
		
					//ת��
					request.setAttribute("shopBean", shopBean);
					request.getRequestDispatcher("shopinfo_page.jsp").forward(request, response);
					return;
				}
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
