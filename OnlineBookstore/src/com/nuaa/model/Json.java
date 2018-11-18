package com.nuaa.model;
import net.sf.json.JSONObject;

/**
 * 服务器端Servlet处理成Ajax能接收的数据类型Json，以取得Json字符串，每个bean均持有一个json
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
