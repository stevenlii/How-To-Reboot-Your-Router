package com.paymoon.rebootroute;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RebootUrRoute implements Runnable {
	private static Logger logger = LogManager.getLogger(RebootUrRoute.class);

	/**
	 * post方式提交表单（模拟用户登录请求）
	 */
	public static void postForm() {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		// "http://api.110monitor.com/ucid/api/authorize?user=helloworld&password=helloworld"

		// HttpPost httppost = new
		// HttpPost("http://118.85.194.45:8080/ucid/api/authorize?user=helloworld&password=helloworld");
		HttpPost httppost = new HttpPost("http://192.168.0.1/goform/SysToolReboot");
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("CMD", "SYS_CONF"));
		formparams.add(new BasicNameValuePair("GO", "system_reboot.asp"));
		formparams.add(new BasicNameValuePair("CCMD", "0"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			System.out.println("back....");
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void reboot(boolean isStop) {
		if (isStop) {
			System.exit(0);
		} else {
			postForm();
		}
	}

	public static void main(String[] args) {

		int rebootTime = 20 * 1000;
		int rebootCount = 3;
		while (rebootCount > 0) {
			logger.info("reboot begin. rebootCount is {}", rebootCount);
			RebootUrRoute route = new RebootUrRoute();
			Thread t = new Thread(route);
			t.start();
			try {
				logger.info("rebootTime begin. rebootTime is {}", rebootCount);
				t.sleep(rebootTime);
				logger.info("rebootTime end. rebootTime is {}", rebootCount);
				route.setStop();
				t.stop();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				rebootCount--;
			}

		}

	}
	public void setStop(){
        this.isStop=false;
    }

	@Override
	public void run() {
		while (isStop){
			
			postForm();
		}
	}
	public volatile boolean isStop = true;


}
// </namevaluepair></namevaluepair></namevaluepair></namevaluepair>