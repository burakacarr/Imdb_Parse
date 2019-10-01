package io.opthema.com.egitim;

import java.util.List;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import io.opthema.com.egitim.gui.Gui;

public class Starter {

	public static void main(String[] args) throws ClientProtocolException, IOException {

	}
	
	public StringBuffer StartAcýklamaDownload(String text) throws ClientProtocolException, IOException {
		
		DownloaderViaApache dwn = new DownloaderViaApache();
		String url = text;
		StringBuffer result = dwn.downloadData(url);
		return result;
		
	}

	public String[] startDownload(String text) throws ClientProtocolException, IOException {
		DownloaderViaApache dwn = new DownloaderViaApache();

		String url = "https://www.imdb.com/find?ref_=nv_sr_fn&q=" + text + "&s=all";
		StringBuffer result = dwn.downloadData(url);
		Parse parse = new Parse();
		String[] s= parse.parser(result);
		return s;
		
	}
}