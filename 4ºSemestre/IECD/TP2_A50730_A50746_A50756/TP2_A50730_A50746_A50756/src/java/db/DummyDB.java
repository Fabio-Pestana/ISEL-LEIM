package db;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormatSymbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Simula a existência de uma base de dados.
 * Usa as facilidades de adaptação às variantes regionais, nomeadamente a língua.
 */
public class DummyDB {
	private List<String> lista=null;
	/**
	 * @param items		simula a existência de várias alternativas
	 */
	public DummyDB(String items, String filePath) {
		if(items.compareToIgnoreCase("paises")==0)
			lista=CountriesDB("");  // pt, en, fr, it
		if(items.compareToIgnoreCase("meses")==0)
			lista=MonthsDB("it");	// meses em italiano
		if(items.compareToIgnoreCase("palavras")==0)
			lista=WordsDB();
		if(items.compareToIgnoreCase("arterias")==0)
			lista=ArteriasDB();
		if(items.compareToIgnoreCase("users")==0)
			lista=UsersDB(filePath);
	}
	
	private ArrayList<String> CountriesDB(String language) {
	    /* A list containing all the country/regions names in the world: */
	    ArrayList<String> countries = new ArrayList<String>();
	    // Map ISO countries to custom country object
	    String[] countryCodes = Locale.getISOCountries();
	    for (String countryCode : countryCodes) {
		Locale locale = new Locale(language, countryCode);
		countries.add(locale.getDisplayCountry());
	    }
	    Collections.sort(countries);
	    return countries;
	}
	
	private ArrayList<String> MonthsDB(String language) {
	    ArrayList<String> months = new ArrayList<String>(
		    Arrays.asList(new DateFormatSymbols(new Locale(language)).getMonths()));
	    Collections.sort(months);
	    return months;
	}
	
	private ArrayList<String> WordsDB() {
		ArrayList<String> words = new ArrayList<String>(
		Arrays.asList("Gazeta", "publica", "hoje","no","jornal","uma","breve","nota","de","faxina","na","quermesse"));
		Collections.sort(words);
		return words;
	}
	
	private ArrayList<String> ArteriasDB() {
		ArrayList<String> arterias = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		        DummyDB.class.getResourceAsStream("arterias.txt")));
		String line = null;
		try {
			while((line=reader.readLine())!=null)
				arterias.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arterias;
	}
	
	public ArrayList<String> UsersDB(String filePath) {
        ArrayList<String> usernames = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream(filePath)) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String username = element.getElementsByTagName("username").item(0).getTextContent();
                    usernames.add(username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usernames;
    }
	
	/**
	 * @param query 	String que é comparado com o início
	 * @param nLetters 	Quantidade de letras minimo exigido para preencher a lista
	 * @param nItems	Quantidade de sugestões devolvidas
	 * @return  		Lista de items que satisfazem a query
	 */
	public List<String> getData(String query, int nLetters, int nItems) {
		List<String> matched = new ArrayList<String>();
		if(query==null || query.length()<nLetters) // numero minimo de letras
			return matched;
		query = query.toLowerCase();
		for(int i=0; i<lista.size(); i++) {
			String item = lista.get(i).toLowerCase();
			if( item.toLowerCase().startsWith(query)) { 
				matched.add(lista.get(i));
			if( matched.size()>nItems)  // limita o numero de respostas
				break;
			}
		}
		return matched;
	}
}