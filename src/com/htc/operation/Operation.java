package com.htc.operation;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.htc.network.ConnNet;

import android.util.Log;


public class Operation 
{

	//��¼
	public String login(String url,String username,String password) 
	{    
		String result = null;
		ConnNet connNet=new ConnNet();
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		try {
			HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
			HttpPost httpPost=connNet.gethttPost(url);
			System.out.println("11--->"+httpPost.toString());
			System.out.println("httpPost---->"+ httpPost.toString());
			
			httpPost.setEntity(entity);
			HttpClient client=new DefaultHttpClient();
			System.out.println("client"+client);
			HttpResponse httpResponse=client.execute(httpPost);
			System.out.println("HttpStatus--->>>"+httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK) 
				
			{
				result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");		
				System.out.println("21");
				
			}
			else
			{
				result="��¼ʧ��";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("result---->>"+result);
		return result;
	}
	//ע��ʱ����û����Ƿ����
	public String checkusername(String url,String username) 
	{
		String result=null;
		ConnNet connNet=new ConnNet();
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		try {
			HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
			HttpPost httpPost=connNet.gethttPost(url);
			
			System.out.println(httpPost.toString());
			
			httpPost.setEntity(entity);
			HttpClient client=new DefaultHttpClient();
			HttpResponse httpResponse=client.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK) 
			{
				result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");	
				System.out.println("resu"+result);
			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (ParseException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return result;  
	}

	//ע��ʱ�ϴ�����
	public String UpData(String uripath,String jsonString)
	{ 
		String result = null;
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		NameValuePair nvp=new BasicNameValuePair("jsonstring", jsonString);
		list.add(nvp);
		ConnNet connNet=new ConnNet();
		HttpPost httpPost=connNet.gethttPost(uripath);
		try {
			HttpEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
			//�˾������Ϸ��򴫵��ͻ��˵����Ľ�������
			httpPost.setEntity(entity);
			HttpClient client=new DefaultHttpClient();
			HttpResponse httpResponse=client.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode()==200)
			{
				result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");	
				System.out.println("resu"+result);
			}
			else {
				result="ע��ʧ��";
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {			
			e.printStackTrace();
		} catch (ParseException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return result;  
	}
	
	
	public String uploadFile(File file,String urlString)
	{
		final String TAG = "uploadFile";
		final int TIME_OUT = 10*1000;   //��ʱʱ��
		final String CHARSET = "utf-8"; //���ñ���
		String result = null;
		String  BOUNDARY =  UUID.randomUUID().toString();  //�߽��ʶ   �������
		String PREFIX = "--" , LINE_END = "\r\n"; 
		String CONTENT_TYPE = "multipart/form-data";   //��������
		System.out.println("uploadFile1.1");
		try {
			ConnNet connNet=new ConnNet();
		    HttpURLConnection conn	=connNet.getConn(urlString);
			conn.setReadTimeout(TIME_OUT);
//			conn.setConnectTimeout(TIME_OUT);
//			conn.setDoInput(true);  //����������
//			conn.setDoOutput(true); //���������
//			conn.setUseCaches(false);  //������ʹ�û���
//			conn.setRequestMethod("POST");  //����ʽ
			conn.setRequestProperty("Charset", CHARSET);  //���ñ���
			conn.setRequestProperty("connection", "keep-alive");   
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 

			if(file!=null)
			{
				/**
				 * ���ļ���Ϊ�գ����ļ���װ�����ϴ�
				 */
				DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * �����ص�ע�⣺
				 * name�����ֵΪ����������Ҫkey   ֻ�����key �ſ��Եõ���Ӧ���ļ�
				 * filename���ļ������֣�������׺����   ����:abc.png  
				 */

				sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\""+LINE_END); 
				sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while((len=is.read(bytes))!=-1)
				{
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * ��ȡ��Ӧ��  200=�ɹ�
				 * ����Ӧ�ɹ�����ȡ��Ӧ����  
				 */
				int res = conn.getResponseCode();  
				Log.i(TAG, "response code:"+res);
				//                if(res==200)
				//                {
				Log.i(TAG, "request success");
				InputStream input =  conn.getInputStream();
				StringBuffer sb1= new StringBuffer();
				int ss ;
				while((ss=input.read())!=-1)
				{
					sb1.append((char)ss);
				}
				result = sb1.toString();
				Log.i(TAG, "result : "+ result);
				//                }
				//                else{
				//                    Log.e(TAG, "request error");
				//                }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
 
}
