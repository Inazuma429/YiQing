package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBUtil;

public class WorldDao {
	public static String search(String date) {
		//����JSONArray�������
		JSONArray jsonarray=new JSONArray();
		Connection con=DBUtil.getConn();
		Statement state=null;
		//ͨ�����ڽ���ģ����ѯ
		String sql="select distinct Country from qmt where Date like '%"+date+"%'";
		String countryStr="";
		ResultSet res=null;
		try {
			//����JSONObject����
			JSONObject jsonob=new JSONObject();
			state=con.createStatement();
			res=state.executeQuery(sql);
			//����echarts�ļ��й����й���ͼ��ʡ������¼�����������ݿ��в�ͬ����ȡ���ݿ���ʡ�����ֵ�ʱ��Ҫ����Ԥ�����������鱣��
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
				if(str[i].equals("�й�")) {
					sql="select sum(Confirmed),sum(Cured),sum(Dead),sum(Yisi),ECountry,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//��ѡ�õ�����put��JSONObject������
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", res.getString(5));
					jsonob.put("Continent", res.getString(6));
					res.close();
					//��JSONObject����д��JSONArray�����У�����json����
					jsonarray.add(jsonob);
				}else if(str[i].equals("����")) {
					sql="select Confirmed,Cured,Dead,Yisi,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//��ѡ�õ�����put��JSONObject������
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", "United States");
					jsonob.put("Continent", res.getString(5));
					res.close();
					//��JSONObject����д��JSONArray�����У�����json����
					jsonarray.add(jsonob);
				}else {
					sql="select Confirmed,Cured,Dead,Yisi,ECountry,Continent from qmt where Country='"+str[i]+"'";
					res=state.executeQuery(sql);
					res.next();
					//��ѡ�õ�����put��JSONObject������
					jsonob.put("Country", str[i]);
					jsonob.put("Confirmed", res.getInt(1));
					jsonob.put("Cured", res.getString(2));
					jsonob.put("Dead", res.getString(3));
					jsonob.put("Yisi", res.getString(4));
					jsonob.put("ECountry", res.getString(5));
					jsonob.put("Continent", res.getString(6));
					res.close();
					//��JSONObject����д��JSONArray�����У�����json����
					jsonarray.add(jsonob);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//��json����ת����String���ͣ�����
		return jsonarray.toString();
	}
}
