package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBUtil;

public class CityDao {
	public static String searchCity(String date) {
		JSONArray jsonarrayC=new JSONArray();
		Connection con=DBUtil.getConn();
		Statement state=null;
		//��ȡ�˲�ͬ�����ݿ�
		String sql="select City from hebei_city_info where Date like '%"+date+"%'";
		String CityStr="";
		ResultSet res=null;
		try {
			JSONObject jsonobC=new JSONObject();
			state=con.createStatement();
			res=state.executeQuery(sql);
			while(res.next()) {
				CityStr=CityStr+res.getString("City")+",";
			}
			res.close();
			String str[]=CityStr.split(",");
			for(int i=0;i<str.length;i++) {
				if(str[i].trim().equals("")) {
					continue;
				}
				//��ȡ��Ϣ
				sql="select Confirmed_num from hebei_city_info where City='"+str[i]+"'";
				res=state.executeQuery(sql);
				res.next();
				String CNum=res.getString("Confirmed_num");
				CNum=CNum.substring(0, CNum.length()-1);
				jsonobC.put("City", str[i]);
				jsonobC.put("CityConfirmed", CNum);
				res.close();
				//д��JSONArray����
				jsonarrayC.add(jsonobC);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//����ת��������
		return jsonarrayC.toString();
	}
}
