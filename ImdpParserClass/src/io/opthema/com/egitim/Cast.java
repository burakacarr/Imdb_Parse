package io.opthema.com.egitim;

enum type{
	Director,Writer,Star
}

public class Cast {
	//public List<type> Type = new ArrayList<type>();
	private String castName,castLinkString;
	private String type1,type2,type3;
	public Cast() {
		type1="";
		type2="";
		type3="";
	}
	public String getCastName() {
		return castName;
	}

	public void setCastName(String castName) {
		this.castName = castName;
	}

	public String getCastLinkString() {
		return castLinkString;
	}

	public void setCastLinkString(String castLinkString) {
		this.castLinkString = castLinkString;
	}

	public String getType3() {
		return type3;
	}

	public void setType3(String type3) {
		this.type3 = type3;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}
	

}
