package com.htc.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;

public class ConnNet {

//	private static final String URLVAR = "http://172.19.52.177:8080/LoginRegister/";
	private static final String URLVAR = "http://10.0.2.2:8080/LoginRegister/";
	//��·������Ϊһ���������޸ĵ�ʱ��Ҳ�ø���
	//ͨ��url��ȡ��������  connection 
	
	public HttpURLConnection getConn(String urlpath) {
		
		String  finalurl = URLVAR+urlpath;
		HttpURLConnection 	connection = null;
		try {
			URL url = new URL(finalurl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);//����������
			connection.setDoOutput(true);//���������
			connection.setUseCaches(false);//������ʹ�û���
			connection.setRequestMethod("POST");//����ʽ
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
		
	}
	
	public HttpPost gethttPost(String uripath) {
		
		HttpPost httpPost = new HttpPost(URLVAR+uripath);
		System.out.println(URLVAR+uripath);
		return httpPost;
		
	}
}
