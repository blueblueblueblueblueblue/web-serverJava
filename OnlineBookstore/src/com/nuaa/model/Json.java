package com.nuaa.model;
import net.sf.json.JSONObject;

/**
 * ��������Servlet�����Ajax�ܽ��յ���������Json����ȡ��Json�ַ�����ÿ��bean������һ��json
 * @author chicken
 *
 */
public class Json {
	
	protected JSONObject jsonObject=new JSONObject();
	
	public JSONObject getJsonObject()
	{
		return jsonObject;
	}
	
	public void setJsonObject(JSONObject jsonObject)
	{
		this.jsonObject=jsonObject;
	}
}
