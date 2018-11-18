package com.nuaa.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.AddressBean;
import com.nuaa.model.AddressBeanCl;
import com.nuaa.model.Json;
import com.nuaa.util.C;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@WebServlet("/AddressClServlet")
public class AddressClServlet extends HttpServlet{

	/**
	 * 处理用户的地址相关操作
	 */
	private static final long serialVersionUID = 1L;

	public AddressClServlet() {
        super();
    }
	
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
			if(dowhat.equals("readAddress")) //读取username用户的地址表
			{
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				ArrayList<AddressBean> addressList=addressBeanCl.getAddressByUid(username);
				
				//将bookCommentBeanList转换为json字符串传给客户端		
				JSONArray jsonArray=new JSONArray();
				for(int i=0;i<addressList.size();i++)
					jsonArray.put(((Json) addressList.get(i)).getJsonObject());
				
				//回写给客户端查到的json数据
				response.setContentType("text/html;charset=utf-8"); 
				response.setHeader("Pragma","No-cache"); 
				response.setHeader("Cache-Control","no-cache");
				response.setHeader("Expires","0");
				response.getWriter().print(jsonArray.toString());
			}
			else if(dowhat.equals("addAddress")) //添加新地址
			{
				//获取地址信息并封装
				String receiver_name=request.getParameter("receiver_name");
				String receive_address=request.getParameter("receive_address");
				String receive_code=request.getParameter("receive_code");
				String receive_phone=request.getParameter("receive_phone");
				String receive_fixphone=request.getParameter("receive_fixphone");
				String loc_province=request.getParameter("loc_province");
				String loc_city=request.getParameter("loc_city");
				String loc_town=request.getParameter("loc_town");
				int check=Integer.parseInt(request.getParameter("check"));
				
				//添加新地址至数据库
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				
				//先判断该用户地址数量是否满
				int curAddressNum = addressBeanCl.getAddressCount(username);
				if(curAddressNum < C.maxAddressNum) //未满
				{
					String maxAid = addressBeanCl.getMaxAidByUid(username); //获取当前最大地址
					String aid = createNewAid(maxAid,username);
					
					AddressBean addressBean=new AddressBean(aid, username ,receiver_name, receive_address, receive_code, receive_phone, receive_fixphone, loc_province, loc_city, loc_town, check);	
					addressBeanCl.addAddress(addressBean); //添加地址，并返回刚添加地址的id号，方便之后更新check用
					
					if(addressBean.getAcheck() == C.addressChecked) //设为了默认地址
						addressBeanCl.checkAddress(username,aid); //aid地址选中
					
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("result", "SUCCESS");
					jsonObject.put("address", addressBean.getJsonObject());
					
					response.getWriter().print(jsonObject.toString());
					return;
				}
				else //满了
				{
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("result", "ADDRESSNUM_OVERFLOW");
					
					response.getWriter().print(jsonObject.toString());
					return;
				}
			}
			else if(dowhat.equals("modifyAddress")) //修改地址
			{
				//获取地址信息并封装
				String aid=request.getParameter("aid");
				String receiver_name=request.getParameter("receiver_name");
				String receive_address=request.getParameter("receive_address");
				String receive_code=request.getParameter("receive_code");
				String receive_phone=request.getParameter("receive_phone");
				String receive_fixphone=request.getParameter("receive_fixphone");
				String loc_province=request.getParameter("loc_province");
				String loc_city=request.getParameter("loc_city");
				String loc_town=request.getParameter("loc_town");
				AddressBean addressBean=new AddressBean(aid, username ,receiver_name, receive_address, receive_code, receive_phone, receive_fixphone, loc_province, loc_city, loc_town, C.addressUnchecked);
				
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				int index=addressBeanCl.modifyAddress(addressBean);
				
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("index", index);
				jsonObject.put("address", addressBean.getJsonObject());
				
				response.getWriter().print(jsonObject.toString());
				return;
			}
			else if(dowhat.equals("deleteAddress")) //删除地址
			{
				String aid=request.getParameter("Aid"); //获得地址的id号
				boolean isChecked=Boolean.parseBoolean(request.getParameter("isChecked")); //是否要删的正好也是选中的？
				
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				String newCheckedAid=addressBeanCl.deleteAddress(username,aid); //删去aid,并获得当前第一个地址的aid
				if(isChecked == true)
					addressBeanCl.checkAddress(username,newCheckedAid); //让新的aid地址选中
				
				return;
			}
			else if(dowhat.equals("checkAddress")) //设默认地址
			{
				String aid=request.getParameter("Aid"); //获得地址的id号
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				addressBeanCl.checkAddress(username,aid); //aid地址选中
				
				return;
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public String createNewAid(String maxAid,String username)
	{
		int num;
		if(maxAid == null) //一个地址也没有
		{
			num = 0;
		}
		else
		{
			num = Integer.parseInt(maxAid.substring(maxAid.lastIndexOf("_")+1));
			num++;
		}
		
		return username + "_" + String.format("%04d", num);
	}
}
