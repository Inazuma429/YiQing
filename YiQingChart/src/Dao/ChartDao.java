package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBUtil;

public class ChartDao {
	public static String search(String date) {
		//定义JSONArray数组对象
		JSONArray jsonarray=new JSONArray();
		Connection con=DBUtil.getConn();
		Statement state=null;
		//通过日期进行模糊查询
		String sql="select distinct Province from info where Date like '%"+date+"%'";
		String provinceStr="";
		ResultSet res=null;
		try {
			//定义JSONObject对象
			JSONObject jsonob=new JSONObject();
			state=con.createStatement();
			res=state.executeQuery(sql);
			String inputstr="";
			//由于echarts文件中关于中国地图的省份所记录的名字与数据库中不同，提取数据库中省份名字的时候要进行预处理，先用数组保存
			while(res.next()) {
				provinceStr=provinceStr+res.getString("Province")+",";
			}
			res.close();
			String str[]=provinceStr.split(",");
			//对各个省份名称进行处理，与echarts文件中的名字对应（若不对应则构建中国地图时无法染色）
			for(int i=0;i<str.length;i++) {
				if(str[i].trim().equals("")) {
					continue;
				}
				if(str[i].length()==3) {
					inputstr=str[i].substring(0, 2);
				}else if(str[i].equals("黑龙江省")) {
					inputstr="黑龙江";
				}else if(str[i].equals("广西壮族自治区")) {
					inputstr="广西";
				}else if(str[i].equals("内蒙古自治区")) {
					inputstr="内蒙古";
				}else if(str[i].equals("宁夏回族自治区")) {
					inputstr="宁夏";
				}else if(str[i].equals("新疆维吾尔自治区")) {
					inputstr="新疆";
				}else if(str[i].equals("西藏自治区")) {
					inputstr="西藏";
				}else if(str[i].length()==2) {
					inputstr=str[i];
				}
				//根据省份筛选“确诊人数”，数据库中省份名字出现重合情况，根据省份对其Confirmed_num数据进行求和操作再拿出（确诊人数为地图染色依据）
				sql="select sum(Confirmed_num),sum(Cured_num),sum(Dead_num),City from info where Province='"+str[i]+"'";
				res=state.executeQuery(sql);
				res.next();
				//将选好的数据put进JSONObject对象中
				jsonob.put("Province", inputstr);
				jsonob.put("Confirmed", res.getInt(1));
				jsonob.put("Cured", res.getString(2));
				jsonob.put("Dead", res.getString(3));
				res.close();
				//将JSONObject对象写入JSONArray数组中，构成json数组
				jsonarray.add(jsonob);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//将json数组转化成String类型，返回
		return jsonarray.toString();
	}
}
