package io.opthema.com.egitim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class DownloaderViaApache {

	/**
	 * 
	 * @param url
	 * @return download html
	 * @throws ClientProtocolException
	 * @throws IOException
	 * 
	 */
	public static String downloadData(String url) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("User-agent", "USER_AGENT");
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}

	static void DownloadSubData(String url, String idid, String altid, String sidid) throws IOException {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			List<NameValuePair> form = new ArrayList<>();
			form.add(new BasicNameValuePair("idid", idid));
			form.add(new BasicNameValuePair("altid", altid));
			form.add(new BasicNameValuePair("sidid", sidid));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);
			System.out.println("Executing request " + httpPost.getRequestLine());
			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity responseEntity = response.getEntity();
					return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			};
			String responseBody = httpclient.execute(httpPost, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Burak\\Desktop\\test.zip"));
	        out.write(responseBody);   
	        out.close();
		}
	}
}
