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
import org.omg.CORBA.ServerRequest;

import com.nuaa.model.BookBeanCl;
import com.nuaa.model.BookDetailBean;
import com.nuaa.model.ShopBean;
import com.nuaa.model.ShopBeanCl;
import com.nuaa.util.C;
import com.nuaa.util.CookieUtil;

@WebServlet("/ShopUploadClServlet")
public class ShopUploadClServlet extends HttpServlet {
	
	/**
	 * �������ϴ��������
	 * �����������
	 */
	private static final long serialVersionUID = 1L;

	public ShopUploadClServlet() {
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
			//����֤���û���û�п���
			ShopBeanCl shopBeanCl = new ShopBeanCl();
			int sid = shopBeanCl.getSidByUid(username);
			request.setAttribute("Sid", sid); //����jspҳ��ʶ���û��Ƿ񿪵� ������ʾ��ͬ����
			
			if(sid == C.OPENED_SHOP_NO) //û������ֱ�ӷ��ؽ�����һ������Ҫ���ꡱ��ť�Ľ���
			{
				request.getRequestDispatcher("shopupload_page.jsp").forward(request, response);
				return;
			}
			
			//���˵�ģ�������������顱��
			if(dowhat.equals("firstIn"))
			{
				request.getRequestDispatcher("shopupload_page.jsp").forward(request, response);
				return;	
			}
			
			//���˵�ģ��������������
			if(dowhat.equals("uploadBook") && session.getAttribute(C.SHOPUPLOAD_TOKEN) != null) //������棬�ϴ��鼮
			{
			
				session.removeAttribute(C.SHOPUPLOAD_TOKEN);
				
				//�õ��ϴ��ļ��ı���Ŀ¼�����ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
				String savePath = this.getServletContext().getRealPath("");
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
				String bid="";
				String bimage="";
				try
				{
					BookBeanCl bookBeanCl = new BookBeanCl(); 
					
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
							//�����鼮�š�ͼƬ���ƣ�username_���.jpg��		
							String maxBid = bookBeanCl.getMaxBidBySid(sid); //��ȡusername���������ѷ������鼮����
							bid = createNewBid(maxBid, username);
							bimage = bid + ".jpg";
							
							if(bimage==null || bimage.trim().equals(""))
							{
								continue;
							}
							
							//ע�⣺��ͬ��������ύ���ļ����ǲ�һ���ģ���Щ������ύ�������ļ����Ǵ���·���ģ��磺 c:\a\b\1.txt������Щֻ�ǵ������ļ������磺1.txt
							//�����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
							bimage = bimage.substring(bimage.lastIndexOf("\\")+1);
							//��ȡitem�е��ϴ��ļ���������
							InputStream in = item.getInputStream();
							//����һ���ļ������
							FileOutputStream out = new FileOutputStream(savePath + "\\" + bimage);
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
					String bname = (String)request.getAttribute("book_name");
					String bauthor = (String)request.getAttribute("book_author");
					String bpublisher = (String)request.getAttribute("book_publisher");
					String bpublishdate = (String)request.getAttribute("book_publishdate");
					String btype = request.getAttribute("book_parent_type") + "_" + request.getAttribute("book_child_type");
					float  boriprice = Float.parseFloat((String)request.getAttribute("book_oriprice"));
					float  bprice = Float.parseFloat((String)request.getAttribute("book_price"));
					int    bstocknum = Integer.parseInt((String)request.getAttribute("book_stocknum"));
					int    bversion = Integer.parseInt((String)request.getAttribute("book_version"));
					int    bpagenum = Integer.parseInt((String)request.getAttribute("book_pagenum"));
					int    bwordnum = Integer.parseInt((String)request.getAttribute("book_wordnum"));
					String bprintdate = (String)request.getAttribute("book_printdate");
					int    bsize = Integer.parseInt((String)request.getAttribute("book_size"));
					String bpaperstyle = (String)request.getAttribute("book_paperstyle");
					int    bprintnum = Integer.parseInt((String)request.getAttribute("book_printnum"));
					String bpackage = (String)request.getAttribute("book_package");
					String bisbn =(String) request.getAttribute("book_isbn");
					String bcontentsummary = (String)request.getAttribute("book_contentsummary");
					String bauthorsummary = (String)request.getAttribute("book_authorsummary");
					String bmediacomment = (String)request.getAttribute("book_mediacomment");
					String btastecontent = (String)request.getAttribute("book_tastecontent");
					
					BookDetailBean newBook = new BookDetailBean(bid, bimage, bprice, bname, sid, "", 0,0, bauthor, bpublisher, bpublishdate, boriprice, C.RAND_NUM, "", 0, bversion, bpagenum, bwordnum, bprintdate, bsize, bpaperstyle, bprintnum, bpackage, bisbn, bcontentsummary, bauthorsummary, bmediacomment, btastecontent, "",bstocknum, "",btype);
					newBook.setBimage(bimage);
					bookBeanCl.addBook(newBook);
	
				}catch (Exception e) {
					
					message= "�ļ��ϴ�ʧ�ܣ�";
					e.printStackTrace();			 
				}
				
				request.getRequestDispatcher("DetailClServlet?dowhat=findDetail&Bid="+bid).forward(request, response);
				return;
			}
			else 
			{
				//��ת��֮ǰ�Ǹ�ҳ��ȥ
				String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue();
				request.getRequestDispatcher(curPageUrl).forward(request, response);
				return;
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//�����鼮������
	public String createNewBid(String maxBid, String username)
	{
		int num;
		if(maxBid == null) //һ����ַҲû��
		{
			num = 0;
		}
		else
		{
			num = Integer.parseInt(maxBid.substring(maxBid.lastIndexOf("_")+1));
			num++;
		}
		
		return username + "_" + String.format("%05d", num);
	}
}
