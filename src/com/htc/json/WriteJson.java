package com.htc.json;

import java.util.List;

import com.google.gson.Gson;

public class WriteJson {

	/*
	 * ͨ������gson jar�� д�� json ����
	 */
	public String getJsonData(List<?> list) {
		// �˴�Ҫע�⣬ʱ�������˵�Ҳ���Gson����������ʱ����ֻ��Ҫ������İ���ϵͳ�ṩ����˳�������
		Gson gson = new Gson();// ����google�ṩ��gson��һ��list����д��json��ʽ���ַ���
		String jsonsString = gson.toJson(list);

		return jsonsString;
	}

	/*
	 * ��Ȼ�������gsonҲ�����ô�ͳ�ķ�ʽ����д��json���ݻ�������StringBufferƴ�ַ��� д��json�ַ�����ʽ
	 */

}
