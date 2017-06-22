package a.rssreader;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class handles parsing Personal Capital's RSS.
 * Each article's title, image link, summary, publish date, and article link are retrieved.
 * The articles are stored in a ArrayList.
 *
 * referenced Google's Training exercise,
 * https://developer.android.com/training/basics/network-ops/xml.html#read
 */
public class PCXMLparser {
    /**
     * create XML parser and parse Personal Capital RSS.
     * @param inputStream source to be parsed.
     * @return ArrayList of items or articles.
     * @throws XmlPullParserException
     * @throws IOException
     */
    public ArrayList<Item> parseXML(InputStream inputStream) throws XmlPullParserException, IOException{
        ArrayList<Item> results = new ArrayList<Item>();
        try{
            //setup XML parser
            XmlPullParser mParser = Xml.newPullParser();
            mParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            mParser.setInput(inputStream, null);
            //skip tags in the beginning
            mParser.nextTag();
            mParser.nextTag();
            results = readFeed(mParser);
        }
        finally {
            inputStream.close();
        }

        return results;
    }

    /**
     * reads entire XML feed.
     * @param parser XML parser
     * @return ArrayList of items or articles.
     * @throws XmlPullParserException
     * @throws IOException
     */
    private ArrayList<Item> readFeed(XmlPullParser parser)throws XmlPullParserException, IOException{
        ArrayList<Item> result = new ArrayList<Item>(); //item nodes, or articles
        String name = null; //XML tag name

        //keep reading until END_TAG
        //make sure current event is start tag
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            //store article contents
            //ID by "item"
            name = parser.getName();
            if (name.equals("item")){
                result.add(
                        //retrieves one item
                        readItem(parser));
            }
            //skip tags that are not important
            else{
                skipTag(parser);
            }
        }
        return result;
    }

    /**
     * contains one Personal Capital article
     */
    public static class Item{
        //item contents
        String mTitle = null;
        String mImageLink = null;
        String mSummary = null;
        String mDate = null;
        String mArticleLink = null;
        //constructor
        private Item(String title, String imageLink, String summary, String date, String articleLink){
            mTitle = title;
            mImageLink = imageLink;
            mSummary = summary;
            mDate = date;
            mArticleLink = articleLink;
        }
    }

    /**
     * parses contents of item or article.
     * @param parser XML parser
     * @return Item or one article
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, "item");
        String name = null; //XML tag name
        //item contents
        String mTitle = null;
        String mImageLink = null;
        String mSummary = null;
        String mDate = null;
        String mArticleLink = null;

        //read all item contents
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            //process contents//
            name = parser.getName();
            if(name.equals("title")){
                parser.require(XmlPullParser.START_TAG, null, "title");
                mTitle = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "title");
            }

            else if(name.equals("media:content")){
                parser.require(XmlPullParser.START_TAG, null, "media:content");
                mImageLink = parser.getAttributeValue(null, "url");
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "media:content");
            }

            else if(name.equals("description")){
                parser.require(XmlPullParser.START_TAG, null, "description");
                mSummary = readText(parser);
                //remove HTML tag
                mSummary = mSummary.replace("<p>", "");
                mSummary = mSummary.replace("</p>", "");
                parser.require(XmlPullParser.END_TAG, null, "description");
            }

            else if(name.equals("pubDate")){
                parser.require(XmlPullParser.START_TAG, null, "pubDate");
                mDate = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "pubDate");
            }

            else if(name.equals("link")){
                parser.require(XmlPullParser.START_TAG, null, "link");
                mArticleLink = readText(parser);
                parser.require(XmlPullParser.END_TAG, null, "link");
            }

            else{
                skipTag(parser);
            }
        }

        return new Item(mTitle, mImageLink, mSummary, mDate, mArticleLink);
    }

    /**
     * retrieve text from XML content
     * @param parser XML parser
     * @return text as String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser parser)throws IOException, XmlPullParserException{
        String result = "";
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * skip tags that are not important.
     * @param parser XML parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException{
        //first tag encountered should be START_TAG
        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }

        int depth = 1; //depth after first START_TAG

        //consumes START_TAG and all events to and including END_TAG
        //depth = 0 when nested elements have been consumed
        while (depth != 0){
            switch (parser.next()){
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
