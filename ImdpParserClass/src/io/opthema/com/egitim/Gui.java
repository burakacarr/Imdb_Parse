package io.opthema.com.egitim;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.http.client.ClientProtocolException;

public class Gui {

	private JFrame frame;
	private JTextField searcMovietext;
	private JList movieList;
	JList DirectorList;
	JList WriterList;
	JList StarList;
	JLabel DirectorLabel;
	JLabel ýmageLabel;
	JLabel WriterLabel;
	JLabel StarrLabel;
	JButton searchButton;
	JTextPane ýmdbDescText;
	JScrollPane menuScrollPane;
	JTextPane trDescription;
	List<Movie> movies = new ArrayList<Movie>();
	JList subList;
	int movieIndex;
	JScrollPane jPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @param ControlText
	 * @return UTF-8 E GÖRE KONTROL YAPIP GERÝ DÖNÜÞ SAÐLAR.
	 * @throws UnsupportedEncodingException
	 */
	String CharacterControl(String ControlText) throws UnsupportedEncodingException {
		byte[] utfKontrol = ControlText.getBytes();
		String result = new String(utfKontrol, "UTF-8");
		return result;
	}

	/**
	 * 
	 * @param Movie Set MovieList
	 * @return adds the wanted movie to the movies list
	 * @throws UnsupportedEncodingException
	 */
	void MovieShoot(String searchMovie) throws ClientProtocolException, IOException {
		String url = "https://www.imdb.com/find?ref_=nv_sr_fn&q=" + searchMovie + "&s=all";
		String result = DownloaderViaApache.downloadData(url);
		int lastIndex = result.length();
		String searchText = "<td class=\"result_text\"> <a href=\"/title/";
		if (result.indexOf(searchText) == -1) {
			return;
		}
		int loopControl = result.indexOf(searchText);
		movies.clear();
		while (loopControl != -1) {
			int index = result.indexOf(searchText);
			String bulunan = result.substring(loopControl + 35, loopControl + 150);
			Movie movie = new Movie();
			int valueLink = bulunan.indexOf(" ");
			int valueName = bulunan.indexOf("<");
			int valueDate1 = bulunan.indexOf("(");
			int valueDate2 = bulunan.indexOf(")");
			movie.setName(bulunan.substring(valueLink + 2, valueName));
			movie.setLik("https://www.imdb.com/" + bulunan.substring(0, valueLink - 2));
			movie.setYear(bulunan.substring(valueDate1 + 1, valueDate2));
			movies.add(movie);
			loopControl = result.indexOf(searchText, loopControl + bulunan.length());
			bulunan = result.substring(index, lastIndex);
		}
		int size = movies.size();
		String[] dizi = new String[size];
		for (int i = 0; i < size; i++) {
			String myFilm = movies.get(i).getName() + "-" + movies.get(i).getYear();
			dizi[i] = CharacterControl(myFilm);
			System.out.println(movies.get(i).getLik());
		}
		movieList.setListData(dizi);
	}

	/**
	 * 
	 * @param Movie description ýmdb add movielist
	 * @return adds the cast of the given movie to the film list
	 * @throws UnsupportedEncodingException
	 */
	void ImdbParserData(int Index) throws ClientProtocolException, IOException {
		movieIndex = Index;
		String result = DownloaderViaApache.downloadData(movies.get(Index).getLik());
		String firstIndex = "<div class=\"summary_text\">";
		String lastIndex = "<";
		if (result.indexOf(firstIndex) != -1) {
			result = result.substring(result.indexOf(firstIndex) + firstIndex.length(), result.length());
			result = result.substring(0, result.indexOf(lastIndex) - lastIndex.length());
			ýmdbDescText.setText(CharacterControl(result.trim()));
		} else {
			result = "Movie description not found!!";
			ýmdbDescText.setText("Movie description not found!!");
		}
		movies.get(Index).setDescImdb(result.trim());
	}

	/**
	 * 
	 * @param Movie Set MovieList
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	public void DataSet(int index, String searchText) throws ClientProtocolException, IOException {
		String result = DownloaderViaApache.downloadData(movies.get(index).getLik());
		String castType = "";
		if (result.indexOf(searchText) == -1) {
			return;
		}
		if (searchText.equals("/?ref_=tt_ov_dr\">")) {
			castType = "Director";
		} else if (searchText.equals("/?ref_=tt_ov_wr\">")) {
			castType = "Writers";
		} else {
			castType = "Stars";
		}
		int globalIndex = result.indexOf(searchText);
		String bulunan = result.substring(globalIndex, result.length());
		while (globalIndex != -1) {
			bulunan = bulunan.substring(bulunan.indexOf(searchText), bulunan.length());
			String nameString = bulunan.substring(0, bulunan.indexOf("</a>"));
			nameString = bulunan.substring(searchText.length(), bulunan.indexOf("</a>"));
			String castLinkString = result.substring(globalIndex - 6, globalIndex);
			// System.out.println(castLinkString);
			Cast cast = new Cast();
			if (nameString.equals("See full cast & crew")) {
				globalIndex = -1;
				continue;
			}
			// ayný isimde kayýt varsa kontrol ekle
			else {
				cast.setCastName(CharacterControl(nameString));
				cast.setCastLinkString(CharacterControl(castLinkString));
				cast.setType1(CharacterControl(castType));
				movies.get(index).castList.add(cast);
			}
			bulunan = bulunan.substring(searchText.length(), bulunan.length());
			globalIndex = bulunan.indexOf(searchText);
			System.out.println(nameString);
		}
	}

	/**
	 * 
	 * @param Movie Set MovieList
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	void GetTurkishDescription(int index) throws ClientProtocolException, IOException {
		String link = movies.get(index).getLik();
		String firstSearch = "/tt";
		String lastSearch = "/?ref";
		int indexFirst = link.indexOf(firstSearch);
		int indexLast = link.indexOf(lastSearch);
		String ýdString = link.substring(indexFirst + firstSearch.length(), indexLast);
		String result = DownloaderViaApache.downloadData("https://turkcealtyazi.org/mov/" + ýdString + "/");
		String searchText = "<div class=\"ozet-goster\" itemprop=\"description\">";
		if (result.indexOf("<div class=\"ozet-goster\" itemprop=\"description\">") == -1)
			trDescription.setText("Türkçe açýklama bulunamadý");
		int control = result.indexOf(searchText, 0);
		result = result.replace("-", "|");
		int endIndex = result.indexOf(" | ", control);
		if (endIndex == -1) {
			result = result.substring(control, result.indexOf("<div"));
		} else {
			result = result.substring(control + searchText.length(), endIndex);
		}
		movies.get(index).setDectTurkSub(CharacterControl(result));
		trDescription.setText(movies.get(index).getDectTurkSub());
	}

	/**
	 * 
	 * @param Movie Set MovieList
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	void DataSetList(int index) {
		int writerIndex = 0;
		int directorIndex = 0;
		int starIndex = 0;
		if (movies.get(index).castList.size() == 0) {
			return;
		}
		for (int i = 0; i < movies.get(index).castList.size(); i++) {
			if (movies.get(index).castList.get(i).getType1().equals("Director")) {
				directorIndex = directorIndex + 1;
			} else if (movies.get(index).castList.get(i).getType1().equals("Writers")) {
				writerIndex = writerIndex + 1;
			} else {
				starIndex++;
			}
		}
		String[] writerList = new String[writerIndex];
		String[] directorlist = new String[directorIndex];
		String[] starlist = new String[starIndex];
		writerIndex = 0;
		directorIndex = 0;
		starIndex = 0;
		for (int i = 0; i < movies.get(index).castList.size(); i++) {
			if (movies.get(index).castList.get(i).getType1().equals("Director")) {
				directorlist[directorIndex] = movies.get(index).castList.get(i).getCastName();
				directorIndex = directorIndex + 1;
			} else if (movies.get(index).castList.get(i).getType1().equals("Writers")) {
				writerList[writerIndex] = movies.get(index).castList.get(i).getCastName();
				writerIndex = writerIndex + 1;
			} else {
				starlist[starIndex] = movies.get(index).castList.get(i).getCastName();
				starIndex++;
			}
		}
		StarList.setListData(starlist);
		WriterList.setListData(writerList);
		DirectorList.setListData(directorlist);
	}

	/**
	 * 
	 * @param Movie ýmage Set MovieList
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	public void ImageSet(int index) throws ClientProtocolException, IOException {
		String result = DownloaderViaApache.downloadData(movies.get(index).getLik());
		String searchText = "?ref_=tt_ov_i\">";
		int startIndex = result.indexOf(searchText);
		if (startIndex == -1) {
			ýmageLabel.setText("RESÝM BULUNAMADI");
			return;
		}
		result = result.substring(startIndex, result.length());
		String secontFind = "src=";
		int secondIndex = result.indexOf(secontFind);
		String lastFind = ".jpg\"";
		int lastIndex = result.indexOf(lastFind);
		result = result.substring(secondIndex + secontFind.length() + 1, lastIndex + lastFind.length() - 1);
		if (result == null) {
			ýmageLabel.setText("RESÝM BULUNAMADI");
		}
		movies.get(index).setImageLink(result);
		URL url = new URL(result);
		InputStream in = new BufferedInputStream(url.openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int n = 0;
		while (-1 != (n = in.read(buf))) {
			out.write(buf, 0, n);
		}
		out.close();
		in.close();
		byte[] response = out.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(response);
		BufferedImage bImage2 = ImageIO.read(bis);
		ImageIcon ýmg = new ImageIcon(bImage2);
		ýmageLabel.setIcon(ýmg);
	}

	/**
	 * 
	 * @param Movie subtitle add MovieList.subtitlelist
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	public void subtitleParser(int index) throws ClientProtocolException, IOException {
		String link = movies.get(index).getLik();
		String firstSearch = "/tt";
		String lastSearch = "/?ref";
		int indexFirst = link.indexOf(firstSearch);
		int indexLast = link.indexOf(lastSearch);
		String ýdString = link.substring(indexFirst + firstSearch.length(), indexLast);
		String result = DownloaderViaApache.downloadData("https://turkcealtyazi.org/mov/" + ýdString + "/");
		String startText = "href=\"/sub/";
		String middleTextString = "title=\"";
		if (result.indexOf(startText) == -1) {
			return;
		}
		int scopeIndex = 0;
		while (scopeIndex > -1) {
			int startIndex = result.indexOf(startText, scopeIndex);
			int middleIndex = result.indexOf("title=\"", startIndex);
			int endIndex = result.indexOf("\">", middleIndex);
			String subtitleName = result.substring(middleIndex + middleTextString.length(), endIndex);
			String subLinkString = subtitleName.substring(subtitleName.indexOf(" - ", 0) + 3);
			subLinkString = "https://turkcealtyazi.org/sub/" + subLinkString + "/"
					+ movies.get(index).getName().replace(" ", "-").trim() + ".html";
			Subtitle subtitle = new Subtitle();
			subtitle.setSubLink(subLinkString);
			subtitle.setSubName(CharacterControl(subtitleName));
			movies.get(index).subList.add(subtitle);
			scopeIndex = result.indexOf(startText, endIndex + "\">".length());
		}
		String[] arraySubName = new String[movies.get(index).subList.size()];
		for (int i = 0; i < movies.get(index).subList.size(); i++) {
			arraySubName[i] = movies.get(index).subList.get(i).getSubName();
		}
		subList.setListData(arraySubName);
	}

	/**
	 * 
	 * @param download selected subtitle
	 * @return prints cast elements
	 * @throws UnsupportedEncodingException
	 */
	public void subtitleDownload(int index) throws ClientProtocolException, IOException {
		String result = DownloaderViaApache.downloadData(movies.get(movieIndex).subList.get(index).getSubLink());
		String searchIdid = "idid\" value=\"";
		String searchAltid = "altid\" value=\"";
		String searchSidid = "sidid\" value=\"";
		String ididString = result.substring(result.indexOf(searchIdid) + searchIdid.length(),
				result.indexOf(searchIdid) + searchIdid.length() + 6);
		String sididString = result.substring(result.indexOf(searchSidid) + searchSidid.length(),
				result.indexOf(searchSidid) + searchSidid.length() + 6);
		String altidString = result.substring(result.indexOf(searchAltid) + searchAltid.length(),
				result.indexOf(searchAltid) + searchAltid.length() + 6);
		DownloaderViaApache.DownloadSubData("https://turkcealtyazi.org/ind", ididString, altidString, sididString);
	}

	public Gui() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1452, 757);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		searcMovietext = new JTextField();
		searcMovietext.setBounds(58, 53, 165, 28);
		frame.getContentPane().add(searcMovietext);
		searcMovietext.setColumns(10);

		searchButton = new JButton("Search");
		searchButton.setBounds(301, 53, 89, 28);
		frame.getContentPane().add(searchButton);

		movieList = new JList();
		movieList.setVisibleRowCount(1);
		movieList.setBounds(58, 120, 165, 184);
		frame.getContentPane().add(movieList);

		ýmdbDescText = new JTextPane();
		ýmdbDescText.setBounds(253, 120, 426, 156);
		frame.getContentPane().add(ýmdbDescText);

		DirectorLabel = new JLabel("Directors");
		DirectorLabel.setBounds(95, 357, 46, 14);
		frame.getContentPane().add(DirectorLabel);

		WriterLabel = new JLabel("Writers");
		WriterLabel.setBounds(315, 357, 46, 14);
		frame.getContentPane().add(WriterLabel);

		StarrLabel = new JLabel("Stars");
		StarrLabel.setBounds(545, 357, 46, 14);
		frame.getContentPane().add(StarrLabel);

		DirectorList = new JList();
		DirectorList.setBounds(39, 382, 155, 138);
		frame.getContentPane().add(DirectorList);

		WriterList = new JList();
		WriterList.setBounds(266, 382, 155, 138);
		frame.getContentPane().add(WriterList);

		StarList = new JList();
		StarList.setBounds(491, 382, 155, 138);
		frame.getContentPane().add(StarList);

		ýmageLabel = new JLabel("");
		ýmageLabel.setBounds(1102, 28, 309, 492);
		frame.getContentPane().add(ýmageLabel);

		trDescription = new JTextPane();
		trDescription.setBounds(689, 120, 398, 156);
		frame.getContentPane().add(trDescription);

		subList = new JList();
		subList.setBounds(710, 287, 366, 379);
		frame.getContentPane().add(subList);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MovieShoot(searcMovietext.getText().toString().replace(" ", "+"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		movieList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList listData = (JList) e.getSource();
				try {
					ImdbParserData(listData.getSelectedIndex());
					DataSet(listData.getSelectedIndex(), "/?ref_=tt_ov_dr\">");
					DataSet(listData.getSelectedIndex(), "/?ref_=tt_ov_wr\">");
					DataSet(listData.getSelectedIndex(), "/?ref_=tt_ov_st_sm\">");
					DataSetList(listData.getSelectedIndex());
					ImageSet(listData.getSelectedIndex());
					GetTurkishDescription(listData.getSelectedIndex());
					subtitleParser(listData.getSelectedIndex());

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		subList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList listData = (JList) e.getSource();
				try {
					subtitleDownload(listData.getSelectedIndex());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				super.mouseClicked(e);
			}

		});
	}
}