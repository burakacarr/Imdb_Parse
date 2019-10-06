package io.opthema.com.egitim;

import java.util.List;
import java.util.ArrayList;
import io.opthema.com.egitim.Cast;

public class Movie {

	private String name,lik,year,descImdb,dectTurkSub, ýmageLink;
	public List<Cast> castList = new ArrayList<Cast>();
	public List<Subtitle> subList = new ArrayList<Subtitle>();
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLik() {
		return lik;
	}

	public void setLik(String lik) {
		this.lik = lik;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDescImdb() {
		return descImdb;
	}

	public void setDescImdb(String descImdb) {
		this.descImdb = descImdb;
	}

	public String getDectTurkSub() {
		return dectTurkSub;
	}

	public void setDectTurkSub(String dectTurkSub) {
		this.dectTurkSub = dectTurkSub;
	}

	public String getImageLink() {
		return ýmageLink;
	}

	public void setImageLink(String imageLink) {
		ýmageLink = imageLink;
	}
	
}
