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

        String xml = getXmlFromUrl(url);
        Document doc = getDomElement(xml);

        NodeList nodeList = doc.getElementsByTagName("item");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            String title = getValue(e, "title");
            String link = getValue(e, "link");
            String description = getValue(e, "description");
            String pubDate = getValue(e, "pubDate");
            String category = getValue(e, "category");
            String guid = getValue(e, "guid");
            NodeList nodeList2 = doc.getElementsByTagName("enclosure");
            String enclosure = "";
            if (nodeList2 != null){
                Element e2 = (Element) nodeList2.item(i);
                enclosure = e2.getAttribute("url");
            }
            Log.i("XMLparse", guid);
            pieceOfNewsList.add(new PieceOfNews(Integer.valueOf(guid.substring(0, 5)), title, enclosure,  description, link, category, pubDate));
        }
        return pieceOfNewsList;
    }

    @Override
    protected void onPostExecute(List<PieceOfNews> pieceOfNewsList) {
        super.onPostExecute(pieceOfNewsList);

        if (pieceOfNewsList == null){
            Toast.makeText(context, context.getResources().getString(R.string.error_loading), Toast.LENGTH_LONG).show();
        }
    }

    private String getXmlFromUrl(String url) {
        String xml = null;

        try {
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
}

