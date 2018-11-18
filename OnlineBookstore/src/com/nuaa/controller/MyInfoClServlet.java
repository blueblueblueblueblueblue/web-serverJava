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
	 * 处理与"个人信息"相关请求
	 * 比如：从数据库中读取个人信息，并返回页面
	 */
	private static final long serialVersionUID = 1L;

	public MyInfoClServlet() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//先验证用户是否登录
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//获取操作类型
		
		//清空session中的临时订单表
		clearOrderListInSession(username,session);
		
		if(username == null) //未登录，跳转至登录界面
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else if(dowhat.equals("updateMyInfo"))
		{				
			
			//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
			String savePath = this.getServletContext().getRealPath("/images/avatars");
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
			String uavatar="";
			UserBean updatedUser=null;
			try
			{
				UserBeanCl userBeanCl = new UserBeanCl();
				
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
						//生成头像图片的名称（username.jpg）
						uavatar = username + ".jpg";
						
						//若没有上传图片，则用以前那个，不再写入到服务器文件夹下了
						String oriFileName = item.getString();					
						if(oriFileName==null || oriFileName.trim().equals(""))
						{
							//获取数据库中头像的名称
							uavatar = userBeanCl.getUavatarByUid(username);

							continue;
						}
						
						//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如： c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
						//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
						uavatar = uavatar.substring(uavatar.lastIndexOf("\\")+1);
						//获取item中的上传文件的输入流
						InputStream in = item.getInputStream();
						//创建一个文件输出流
						FileOutputStream out = new FileOutputStream(savePath + "\\" + uavatar);
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
				
				message= "文件上传失败！";
				e.printStackTrace();			 
			}
			
			//转发
			request.setAttribute("userBean", updatedUser);
			request.getRequestDispatcher("myinfo_page.jsp").forward(request, response);
			return;
		}
		else if(dowhat.equals("queryMyInfo"))//点击“个人信息”选项卡 或直接点击navi的用户名第一次进入
		{
			//查询username用户信息
			UserBeanCl userBeanCl = new UserBeanCl();
			UserBean userBean = userBeanCl.findUserByUid(username);

			//转发
			request.setAttribute("userBean", userBean);
			request.getRequestDispatcher("myinfo_page.jsp").forward(request, response);
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//删除存在session中的临时订单表
	void clearOrderListInSession(String username,HttpSession session)
	{
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //删除临时的订单表
	}
}
