package Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DBUtil;

public class ChartDao {
	public static String search(String date) {
		//����JSONArray�������
		JSONArray jsonarray=new JSONArray();
		Connection con=DBUtil.getConn();
		Statement state=null;
		//ͨ�����ڽ���ģ����ѯ
		String sql="select distinct Province from info where Date like '%"+date+"%'";
		String provinceStr="";
		ResultSet res=null;
		try {
			//����JSONObject����
			JSONObject jsonob=new JSONObject();
			state=con.createStatement();
			res=state.executeQuery(sql);
			String inputstr="";
			//����echarts�ļ��й����й���ͼ��ʡ������¼�����������ݿ��в�ͬ����ȡ���ݿ���ʡ�����ֵ�ʱ��Ҫ����Ԥ�����������鱣��
			while(res.next()) {
				provinceStr=provinceStr+res.getString("Province")+",";
			}
			res.close();
			String str[]=provinceStr.split(",");
			//�Ը���ʡ�����ƽ��д�����echarts�ļ��е����ֶ�Ӧ��������Ӧ�򹹽��й���ͼʱ�޷�Ⱦɫ��
			for(int i=0;i<str.length;i++) {
				if(str[i].trim().equals("")) {
					continue;
				}
				if(str[i].length()==3) {
					inputstr=str[i].substring(0, 2);
				}else if(str[i].equals("������ʡ")) {
					inputstr="������";
				}else if(str[i].equals("����׳��������")) {
					inputstr="����";
				}else if(str[i].equals("���ɹ�������")) {
					inputstr="���ɹ�";
				}else if(str[i].equals("���Ļ���������")) {
					inputstr="����";
				}else if(str[i].equals("�½�ά���������")) {
					inputstr="�½�";
				}else if(str[i].equals("����������")) {
					inputstr="����";
				}else if(str[i].length()==2) {
					inputstr=str[i];
				}
				//����ʡ��ɸѡ��ȷ�������������ݿ���ʡ�����ֳ����غ����������ʡ�ݶ���Confirmed_num���ݽ�����Ͳ������ó���ȷ������Ϊ��ͼȾɫ���ݣ�
				sql="select sum(Confirmed_num),sum(Cured_num),sum(Dead_num),City from info where Province='"+str[i]+"'";
				res=state.executeQuery(sql);
				res.next();
				//��ѡ�õ�����put��JSONObject������
				jsonob.put("Province", inputstr);
				jsonob.put("Confirmed", res.getInt(1));
				jsonob.put("Cured", res.getString(2));
				jsonob.put("Dead", res.getString(3));
				res.close();
				//��JSONObject����д��JSONArray�����У�����json����
				jsonarray.add(jsonob);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//��json����ת����String���ͣ�����
		return jsonarray.toString();
	}
}
