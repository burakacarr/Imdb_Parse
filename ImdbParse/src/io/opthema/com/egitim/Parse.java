package io.opthema.com.egitim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import io.opthema.com.egitim.gui.Gui;

public  class Parse {

	
	List<Film> Filmler ;
	public void EkranaBas(List<Film> Filmler) {
		for (Film i : Filmler) {
			System.out.println("Name= " + i.getName() + " Link= " + i.getLink() + " Date= " + i.getDate());
		}
	}
	
	 public String LinkeGit(int i) throws ClientProtocolException, IOException {
		String link = Film.Filmler.get(i).getLink();
		Starter str = new Starter();
		StringBuffer bfr = str.StartAcýklamaDownload(link);
		String kontrol= bfr.toString();
		String kontrolKelimesi="<div class=\"summary_text\">";
		String sonlandýrma = kontrol.substring(kontrol.indexOf(kontrolKelimesi)+46,kontrol.length());
		
		System.out.println(sonlandýrma.substring(0,sonlandýrma.indexOf("<")));
		
		return sonlandýrma.substring(0,sonlandýrma.indexOf("<"));
		 
	}

	 
	public String[] parser(StringBuffer buffer) {

		 Filmler = new ArrayList<>();
		
		StringBuffer bf = buffer;

		String x = "<td class=\"result_text\"> <a href=\"/title/";

		String xy = bf.toString();
		int xyLength = xy.length();

		int xyDuzenlenen = xy.replace(x, "").length();

		int fark = bf.length() - xyDuzenlenen;
		int donguSonu = fark / x.length();

		for (int i = 0; i < donguSonu; i++) {
			int index = bf.indexOf(x);
			String bulunan = bf.substring(index + 35, index + 150);
			Film film = new Film();
			int valueLink = bulunan.indexOf(" ");
			int valueName = bulunan.indexOf("<");
			int valueDate1 = bulunan.indexOf("(");
			int valueDate2 = bulunan.indexOf(")");
			film.setName(bulunan.substring(valueLink + 2, valueName));
			film.setLink("https://www.imdb.com/" + bulunan.substring(0, valueLink - 2));
			film.setDate(bulunan.substring(valueDate1 + 1, valueDate2));
			Filmler.add(film);
			bulunan = bf.replace(index + 35, index + 120, "").toString();
		}
		
		int size = Filmler.size();
		String[] dizi =	new String[size];
		for (int i = 0; i < Filmler.size(); i++) {
			dizi[i]=Filmler.get(i).getName()+"-"+Filmler.get(i).getDate();
			System.out.println(dizi[i]);
		}
		Film.Filmler=Filmler;
		return dizi;
		//EkranaBas(Filmler);

	}
}