import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class POFactory {

    public static final String URL = "https://www.goodreads.com";
    public static final String KEY = "OGKMsvHS6WTed3fSBAMl0Q";


    public Document parseXML(String file) throws Exception{
//        System.out.println(response.getBody());
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(file)));

        document.getDocumentElement().normalize();
//        System.out.println(document.getTextContent());
        return document;
    }

    public int getAuthorId(String authorName) throws Exception {

        int authorID = 0;
        HttpResponse<String> getResponse = Unirest.get(URL + "/api/author_url/" + authorName + "?key=" + KEY).asString();
        Document document = parseXML(getResponse.getBody());

        NodeList authorList = document.getElementsByTagName("author");
        for (int i = 0; i < authorList.getLength(); i++) {
            Node nNode = authorList.item(0);
//            System.out.println("\nCurrent Element :" + nNode.getAttributes());
            Element eElement = (Element) nNode;
//            System.out.println(eElement.getAttribute("id"));
            authorID = Integer.parseInt(eElement.getAttribute("id"));
        }
        return authorID;
    }

    public int booksCount(int authorID) throws Exception{
        HttpResponse<String> response =
                Unirest.get(URL + "/author/list/" + authorID + "?key=" + KEY).asString();
        Document document = parseXML(response.getBody());
        Node total = document.getElementsByTagName("books").item(0);
        Element eElement = (Element) total;
        return Integer.parseInt(eElement.getAttribute("total"));
    }

    public int booksOnThePage(int authorID) throws Exception{
        HttpResponse<String> response =
                Unirest.get(URL + "/author/list/" + authorID + "?key=" + KEY).asString();
        Document document = parseXML(response.getBody());
        Node total = document.getElementsByTagName("books").item(0);
        Element eElement = (Element) total;
        return Integer.parseInt(eElement.getAttribute("end"));
    }

    public int totalPages(int authorID) throws Exception{

        int totalBooksCount = booksCount(authorID);

        int displayedBooks = booksOnThePage(authorID);
        if (totalBooksCount % displayedBooks != 0)
            return totalBooksCount / displayedBooks + 1;
        else
            return totalBooksCount / displayedBooks;
    }


    public String[][] booksList(String authorName) throws Exception {
        int authorID = getAuthorId(authorName);    //("Jack London");
        System.out.println(authorName);
        int pagesCount = totalPages(authorID);
        String[][] authorsBooks = new String[booksCount(authorID)][4];
//        System.out.println(pagesCount);

        int iterator = 0;

        for (int page = 1; page <= pagesCount; page++) {
            System.out.println("Current page: " + page);
            HttpResponse<String> response =
                    Unirest.get(URL + "/author/list/" + authorID + "?key=" + KEY + "&page=" + page).asString();
            Document document = parseXML(response.getBody());

            NodeList books = document.getElementsByTagName("book");
            for (int temp = 0; temp < books.getLength(); temp++) {

                Node nNode = books.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    authorsBooks[iterator][0] = eElement.getElementsByTagName("id").item(0).getTextContent();
                    authorsBooks[iterator][1] = eElement.getElementsByTagName("title").item(0).getTextContent();
                    authorsBooks[iterator][2] = eElement.getElementsByTagName("name").item(0).getTextContent();
                    authorsBooks[iterator][3] = eElement.getElementsByTagName("image_url").item(0).getTextContent();

                    iterator++;
                }
            }
        }
        return authorsBooks;
    }
}
