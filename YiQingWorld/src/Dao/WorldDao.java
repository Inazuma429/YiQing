package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBUtil;

public class WorldDao {
	public static String search(String date) {
		//定义JSONArray数组对象
		JSONArray jsonarray=new JSONArray();
		Connection con=DBUtil.getConn();
		Statement state=null;
		//通过日期进行模糊查询
		String sql="select distinct Country from qmt where Date like '%"+date+"%'";
		String countryStr="";
		ResultSet res=null;
		try {
			//定义JSONObject对象
			JSONObject jsonob=new JSONObject();
			state=con.createStatement();
			res=state.executeQuery(sql);
			//由于echarts文件中关于中国地图的省份所记录的名字与数据库中不同，提取数据库中省份名字的时候要进行预处理，先用数组保存
			while(res.next()) {
				countryStr=countryStr+res.getString("Country")+",";
			}
			res.close();
			sql="select sum(Confirmed),sum(Cured),sum(Dead) from qmt where Date like '%"+date+"%'";
			res=state.executeQuery(sql);
			res.next();
			jsonob.put("aconfirmed", res.getInt(1));
			jsonob.put("acured", res.getInt(2));
			jsonob.put("adead", res.getInt(3));
			res.close();
			jsonarray.add(jsonob);
			String str[]=countryStr.split(",");
			for(int i=0;i<str.length;i++) {
				if(str[i].trim().equals("")) {
					continue;
				}
				if(str[i].equals("中国")) {
					sql="select sum(Confirmed),sum(Cured),sum(Dead),sum(Yisi),ECountry,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//将选好的数据put进JSONObject对象中
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", res.getString(5));
					jsonob.put("Continent", res.getString(6));
					res.close();
					//将JSONObject对象写入JSONArray数组中，构成json数组
					jsonarray.add(jsonob);
				}else if(str[i].equals("美国")) {
					sql="select Confirmed,Cured,Dead,Yisi,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//将选好的数据put进JSONObject对象中
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", "United States");
					jsonob.put("Continent", res.getString(5));
					res.close();
					//将JSONObject对象写入JSONArray数组中，构成json数组
					jsonarray.add(jsonob);
				}else {
					sql="select Confirmed,Cured,Dead,Yisi,ECountry,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//将选好的数据put进JSONObject对象中
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", res.getString(5));
					jsonob.put("Continent", res.getString(6));
					res.close();
					//将JSONObject对象写入JSONArray数组中，构成json数组
					jsonarray.add(jsonob);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//将json数组转化成String类型，返回
		return jsonarray.toString();
	}
}
