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
	 * �����û��ĵ�ַ��ز���
	 */
	private static final long serialVersionUID = 1L;

	public AddressClServlet() {
        super();
    }
	
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
			if(dowhat.equals("readAddress")) //��ȡusername�û��ĵ�ַ��
			{
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				ArrayList<AddressBean> addressList=addressBeanCl.getAddressByUid(username);
				
				//��bookCommentBeanListת��Ϊjson�ַ��������ͻ���		
				JSONArray jsonArray=new JSONArray();
				for(int i=0;i<addressList.size();i++)
					jsonArray.put(((Json) addressList.get(i)).getJsonObject());
				
				//��д���ͻ��˲鵽��json����
				response.setContentType("text/html;charset=utf-8"); 
				response.setHeader("Pragma","No-cache"); 
				response.setHeader("Cache-Control","no-cache");
				response.setHeader("Expires","0");
				response.getWriter().print(jsonArray.toString());
			}
			else if(dowhat.equals("addAddress")) //����µ�ַ
			{
				//��ȡ��ַ��Ϣ����װ
				String receiver_name=request.getParameter("receiver_name");
				String receive_address=request.getParameter("receive_address");
				String receive_code=request.getParameter("receive_code");
				String receive_phone=request.getParameter("receive_phone");
				String receive_fixphone=request.getParameter("receive_fixphone");
				String loc_province=request.getParameter("loc_province");
				String loc_city=request.getParameter("loc_city");
				String loc_town=request.getParameter("loc_town");
				int check=Integer.parseInt(request.getParameter("check"));
				
				//����µ�ַ�����ݿ�
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				
				//���жϸ��û���ַ�����Ƿ���
				int curAddressNum = addressBeanCl.getAddressCount(username);
				if(curAddressNum < C.maxAddressNum) //δ��
				{
					String maxAid = addressBeanCl.getMaxAidByUid(username); //��ȡ��ǰ����ַ
					String aid = createNewAid(maxAid,username);
					
					AddressBean addressBean=new AddressBean(aid, username ,receiver_name, receive_address, receive_code, receive_phone, receive_fixphone, loc_province, loc_city, loc_town, check);	
					addressBeanCl.addAddress(addressBean); //��ӵ�ַ�������ظ���ӵ�ַ��id�ţ�����֮�����check��
					
					if(addressBean.getAcheck() == C.addressChecked) //��Ϊ��Ĭ�ϵ�ַ
						addressBeanCl.checkAddress(username,aid); //aid��ַѡ��
					
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("result", "SUCCESS");
					jsonObject.put("address", addressBean.getJsonObject());
					
					response.getWriter().print(jsonObject.toString());
					return;
				}
				else //����
				{
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("result", "ADDRESSNUM_OVERFLOW");
					
					response.getWriter().print(jsonObject.toString());
					return;
				}
			}
			else if(dowhat.equals("modifyAddress")) //�޸ĵ�ַ
			{
				//��ȡ��ַ��Ϣ����װ
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
			else if(dowhat.equals("deleteAddress")) //ɾ����ַ
			{
				String aid=request.getParameter("Aid"); //��õ�ַ��id��
				boolean isChecked=Boolean.parseBoolean(request.getParameter("isChecked")); //�Ƿ�Ҫɾ������Ҳ��ѡ�еģ�
				
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				String newCheckedAid=addressBeanCl.deleteAddress(username,aid); //ɾȥaid,����õ�ǰ��һ����ַ��aid
				if(isChecked == true)
					addressBeanCl.checkAddress(username,newCheckedAid); //���µ�aid��ַѡ��
				
				return;
			}
			else if(dowhat.equals("checkAddress")) //��Ĭ�ϵ�ַ
			{
				String aid=request.getParameter("Aid"); //��õ�ַ��id��
				AddressBeanCl addressBeanCl=new AddressBeanCl();
				addressBeanCl.checkAddress(username,aid); //aid��ַѡ��
				
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
		if(maxAid == null) //һ����ַҲû��
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
