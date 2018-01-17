package com.example.khaled.mynewsapp.Parsing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.khaled.mynewsapp.Models.PieceOfNews;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by khaled on 1/14/18.
 */

public class XmlParser extends AsyncTask<Void, Void, List<PieceOfNews>> {

    private String TAG = XmlParser.class.getSimpleName();
    private List<PieceOfNews> pieceOfNewsList = new ArrayList<>();
    private  Context context;

    public XmlParser (Context context){this.context = context;}

    @Override
    protected List<PieceOfNews> doInBackground(Void... arg0) {
        List<PieceOfNews> pieceOfNewsList = new ArrayList<>();
        String url = "https://arabic.cnn.com/rss/";

        String xml = getXmlFromUrl(url); // getting XML
        Document doc = getDomElement(xml);

        NodeList nodeList = doc.getElementsByTagName("item");

// looping through all item nodes <item>
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            String title = getValue(e, "title"); // name child value
            String link = getValue(e, "link"); // cost child value
            String description = getValue(e, "description"); // description child value
            String pubDate = getValue(e, "pubDate");

            pieceOfNewsList.add(new PieceOfNews(0, 0, title, "", "", description, link, "", pubDate));
        }
        return pieceOfNewsList;
    }

    @Override
    protected void onPostExecute(List<PieceOfNews> pieceOfNewsList) {
        super.onPostExecute(pieceOfNewsList);

        if (pieceOfNewsList == null){
            Toast.makeText(context, "Error Loading Data", Toast.LENGTH_LONG).show();
        }
        /*for (int i =0;i < result.size();i++){
            DBUtils dbUtils = new DBUtils(context);
            dbUtils.insertRecipe(result.get(i));
        }*/
    }

    private String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }

    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    /*private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds );
        conn.setConnectTimeout(15000 /* milliseconds );
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }*/
}

