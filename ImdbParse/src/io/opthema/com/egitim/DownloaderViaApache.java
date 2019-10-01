package io.opthema.com.egitim;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.*;
public class DownloaderViaApache {
	
	public StringBuffer downloadData(String url) throws ClientProtocolException, IOException {
	
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
		
		
		return result;
	}

}