package com.example.khaled.mynewsapp.Parsing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.DropBoxManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.example.khaled.mynewsapp.Models.PieceOfNews;
import com.example.khaled.mynewsapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.methods.HttpGet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

        String url = "https://arabic.cnn.com/rss/";

        try {
            Log.i("XMLfileStr", url);
            Log.i("XMLfileStr", loadXmlFromNetwork(url));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        StackOverflowXmlParser stackOverflowXmlParser = new StackOverflowXmlParser();
        List<PieceOfNews> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        try {
            stream = downloadUrl(urlString);
            entries = stackOverflowXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.

        StringBuilder htmlString = new StringBuilder();
        for (PieceOfNews pieceOfNews : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(pieceOfNews.getTitleLink());
            htmlString.append("'>" + pieceOfNews.getArticleTitle() + "</a></p>");
        }
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}

