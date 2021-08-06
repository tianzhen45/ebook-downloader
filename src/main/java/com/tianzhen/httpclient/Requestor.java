package com.tianzhen.httpclient;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Component
@Slf4j
public class Requestor {
	@Value("${yuedu.defaultBear}")
	protected  String USER_AGENT;

	@Value("${yuedu.Accept}")
	protected String Accept;

	@Value("${yuedu.Accept-Language}")
	protected String AcceptLanguage;

	@Value("${yuedu.Referer}")
	protected String Referer;

	@Value("${yuedu.defaultBear}")
	protected String Authorization;

	protected CookieStore cookieStore;
	protected HttpClientContext context;
	protected CloseableHttpClient client;

	public Requestor() {
		client = HttpClients.createDefault();
	}

	@Override
	public String toString() {
		return "Requestor{" +
				"USER_AGENT='" + USER_AGENT + '\'' +
				", Accept='" + Accept + '\'' +
				", AcceptLanguage='" + AcceptLanguage + '\'' +
				", Referer='" + Referer + '\'' +
				", Authorization='" + Authorization + '\'' +
				'}';
	}

	public void doLogin(String loginUrl, Map<String, String> params) {
		HttpResponse httpResponse = doPost(loginUrl, params);
		processLoginResponse(httpResponse);
		setCookieStore(httpResponse);
	}

	public void processLoginResponse(HttpResponse httpResponse) {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("login resonse status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString;
			try {
				responseString = EntityUtils.toString(entity);
				setToken(responseString);
				System.out.println("response length:" + responseString.length());
				System.out.println("response content:" + responseString.replace("\r\n", ""));
			} catch (org.apache.http.ParseException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setToken(String jsonStr){
		try {
			JSONObject jsonObject = JSONObject.parseObject(jsonStr);
			String access_token = jsonObject.get("access_token").toString();
			Authorization = "Bearer "+access_token;
			System.out.println("token set success!");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setContext() {
		context = HttpClientContext.create();
		Registry<CookieSpecProvider> registry = RegistryBuilder.<CookieSpecProvider> create()
				.register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider()).register(CookieSpecs.DEFAULT, new DefaultCookieSpecProvider())
				.build();
		context.setCookieSpecRegistry(registry);
		context.setCookieStore(cookieStore);
	}

	public void setCookieStore(HttpResponse httpResponse) {
		cookieStore = new BasicCookieStore();
		Header[] headers = httpResponse.getHeaders("Set-Cookie");

		String cookieValue = null;
		for (Header header : headers) {
			System.out.println(header.getName() + ":" + header.getValue());
				cookieValue = header.getValue();
		}
		// 新建一个Cookie
		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", cookieValue);
		cookie.setDomain("cloud.yuedu.pro");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
		System.out.println("set cookie success!");
	}

	public List<NameValuePair> getParam(Map<String, String> parameterMap) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Set<String> keySet = parameterMap.keySet();
		for (String key : keySet) {
			nameValuePairs.add(new BasicNameValuePair(key, parameterMap.get(key)));
		}
		return nameValuePairs;
	}

	public HttpResponse doGet(String url) {
		HttpResponse result = null;
		HttpGet httpGet = new HttpGet(url);
		config(httpGet);
		try {
			result = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public HttpResponse doGet(String url,Map<String,String> param) {
		HttpResponse result = null;
		StringBuilder urlWithParm = new StringBuilder(url+="?");
		for (String key : param.keySet()) {
			urlWithParm.append(key).append("=").append(param.get(key)).append("&");
		}
		urlWithParm.deleteCharAt(urlWithParm.lastIndexOf("&"));
		url = urlWithParm.toString();
		HttpGet httpGet = new HttpGet(url);
		config(httpGet);
		try {
			result = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public HttpResponse doPost(String url, Map<String, String> params) {
		HttpResponse result = null;
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(getParam(params), "UTF-8");
			httpPost.setEntity(postEntity);
			config(httpPost);
			result = client.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected void config(HttpRequestBase httpRequestBase) {
		httpRequestBase.setHeader("User-Agent", USER_AGENT);
		httpRequestBase.setHeader("Accept", Accept);
		httpRequestBase.setHeader("Accept-Language", AcceptLanguage);
		httpRequestBase.setHeader("Referer", Referer);
		httpRequestBase.setHeader("Authorization", Authorization);
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000).build();
		httpRequestBase.setConfig(requestConfig);
	}

}
