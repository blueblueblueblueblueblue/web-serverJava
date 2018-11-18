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
	 * 处理与上传相关请求
	 * 点击“发布”
	 */
	private static final long serialVersionUID = 1L;

	public ShopUploadClServlet() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//先验证用户是否登录
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//获取操作类型
		
		if(username == null) //未登录，跳转至登录界面
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else
		{	
			//先验证该用户有没有开店
			ShopBeanCl shopBeanCl = new ShopBeanCl();
			int sid = shopBeanCl.getSidByUid(username);
			request.setAttribute("Sid", sid); //方便jsp页面识别用户是否开店 进而显示不同内容
			
			if(sid == C.OPENED_SHOP_NO) //没开店则直接返回仅包含一个“我要开店”按钮的界面
			{
				request.getRequestDispatcher("shopupload_page.jsp").forward(request, response);
				return;
			}
			
			//开了店的（点击“店铺详情”）
			if(dowhat.equals("firstIn"))
			{
				request.getRequestDispatcher("shopupload_page.jsp").forward(request, response);
				return;	
			}
			
			//开了店的（点击“发布”）
			if(dowhat.equals("uploadBook") && session.getAttribute(C.SHOPUPLOAD_TOKEN) != null) //点击保存，上传书籍
			{
			
				session.removeAttribute(C.SHOPUPLOAD_TOKEN);
				
				//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
				String savePath = this.getServletContext().getRealPath("");
				File file = new File(savePath);
				
				//判断上传文件的保存目录是否存在
				if (!file.exists() && !file.isDirectory()) 
				{
					System.out.println(savePath+"目录不存在，需要创建");
					//创建目录
					file.mkdir();
				}
				
				//消息提示
				String message = "";
				String bid="";
				String bimage="";
				try
				{
					BookBeanCl bookBeanCl = new BookBeanCl(); 
					
					//使用Apache文件上传组件处理文件上传步骤：
					//1、创建一个DiskFileItemFactory工厂
					DiskFileItemFactory factory = new DiskFileItemFactory();
					//2、创建一个文件上传解析器
					ServletFileUpload upload = new ServletFileUpload(factory);
					//解决上传文件名的中文乱码
					upload.setHeaderEncoding("UTF-8"); 
					//3、判断提交上来的数据是否是上传表单的数据
					if(!ServletFileUpload.isMultipartContent(request))
					{
						//按照传统方式获取数据
						return;
					}
					//4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
					List<FileItem> list = upload.parseRequest(request);
					for(FileItem item : list)
					{
						//如果fileitem中封装的是普通数据
						if(item.isFormField())
						{
							String name = item.getFieldName();
							String value = item.getString("utf-8");
							request.setAttribute(name, value);
						}
						else //文件数据
						{
							//如果fileitem中封装的是上传文件
							//生成书籍号、图片名称（username_编号.jpg）		
							String maxBid = bookBeanCl.getMaxBidBySid(sid); //获取username所开店铺已发布的书籍总数
							bid = createNewBid(maxBid, username);
							bimage = bid + ".jpg";
							
							if(bimage==null || bimage.trim().equals(""))
							{
								continue;
							}
							
							//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如： c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
							//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
							bimage = bimage.substring(bimage.lastIndexOf("\\")+1);
							//获取item中的上传文件的输入流
							InputStream in = item.getInputStream();
							//创建一个文件输出流
							FileOutputStream out = new FileOutputStream(savePath + "\\" + bimage);
							//创建一个缓冲区
							byte buffer[] = new byte[1024];
							//判断输入流中的数据是否已经读完的标识
							int len = 0;
							//循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
							while((len=in.read(buffer))>0)
							{
								//使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
								out.write(buffer, 0, len);
							}
							//关闭输入流
							in.close();
							//关闭输出流
							out.close();
							//删除处理文件上传时生成的临时文件
							item.delete();
							message = "文件上传成功！";
						}
					}
					
					//获取非文件数据
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
					
					message= "文件上传失败！";
					e.printStackTrace();			 
				}
				
				request.getRequestDispatcher("DetailClServlet?dowhat=findDetail&Bid="+bid).forward(request, response);
				return;
			}
			else 
			{
				//跳转到之前那个页面去
				String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue();
				request.getRequestDispatcher(curPageUrl).forward(request, response);
				return;
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//生成书籍的名称
	public String createNewBid(String maxBid, String username)
	{
		int num;
		if(maxBid == null) //一个地址也没有
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
