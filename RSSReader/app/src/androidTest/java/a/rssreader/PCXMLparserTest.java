package a.rssreader;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Xml;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * This class test the PCXMLparser class.
 * The readItem() and readFeed() must be made public in order to run test.
 * Otherwise the methods are commented out in this test.
 */
public class PCXMLparserTest {
    Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
    XmlPullParser mParser = Xml.newPullParser();

//    @Test
//    public void readItemTest() throws IOException, XmlPullParserException {
//        InputStream inputStream = testContext.getAssets().open("item_test.xml"); //test file contains one item
//        //setup
//        PCXMLparser pcxmLparser = new PCXMLparser();
//        mParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//        mParser.setInput(inputStream, null);
//        mParser.nextTag();
//
//        //read a single item
//        //make method public to test
//        PCXMLparser.Item item = pcxmLparser.readItem(mParser);
//
//        //test item contents
//        assertEquals(item.mTitle, "Personal Capital Retire Smart Guide – 65 Expert Tips");
//        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/05/780X300-retire-smart-blog.jpg");
//        assertEquals(item.mSummary, "Retirement isn’t the end - instead, it’s the beginning of an " +
//                "exciting, brand-new chapter in your life. Every day, our advisors chat with people just like you about planning, saving...");
//        assertEquals(item.mDate, "Thu, 04 May 2017 15:05:20 +0000");
//        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/financial-planning-2/personal-capital-retire-smart-guide-65-expert-tips/");
//    }

//    @Test
//    public void readFeedTest() throws IOException, XmlPullParserException {
//        InputStream inputStream = testContext.getAssets().open("feed_test.xml"); //test file contains rss feed
//        ArrayList<PCXMLparser.Item>itemStorage = new ArrayList<PCXMLparser.Item>();
//        PCXMLparser.Item item = null;
//
//        //setup
//        PCXMLparser pcxmLparser = new PCXMLparser();
//        mParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//        mParser.setInput(inputStream, null);
//        mParser.nextTag();
//        mParser.nextTag();
//
//        //make method public to test
//        itemStorage = pcxmLparser.readFeed(mParser);
//
//        //test item1's contents
//        item = itemStorage.get(0);
//        assertEquals(item.mTitle, "Personal Capital Retire Smart Guide – 65 Expert Tips");
//        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/05/780X300-retire-smart-blog.jpg");
//        assertEquals(item.mSummary, "Retirement isn’t the end - instead, it’s the beginning of an " +
//                "exciting, brand-new chapter in your life. Every day, our advisors chat with people just like you about planning, saving...");
//        assertEquals(item.mDate, "Thu, 04 May 2017 15:05:20 +0000");
//        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/financial-planning-2/personal-capital-retire-smart-guide-65-expert-tips/");
//
//        //test item20's contents
//        item = itemStorage.get(19);
//        assertEquals(item.mTitle, "Personal Capital Tax Series – 5: Is an Annuity Right for You?");
//        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/03/annuities-taxes-blog-post-header.jpg");
//        assertEquals(item.mSummary, "If you could purchase one product that promised to give you a steady income for life and take the " +
//                "worry out of outliving your assets, would you jump at the chance? While it sounds like an...");
//        assertEquals(item.mDate, "Mon, 20 Mar 2017 17:03:08 +0000");
//        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/financial-planning-2/personal-capital-tax-series-5-annuity-right/");
//
//        //test last item's contents
//        item = itemStorage.get(itemStorage.size()-1);
//        assertEquals(item.mTitle, "Consumers Scream About Fiduciary Rule and Section 1033 of Dodd-Frank");
//        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/02/angry-mob-blog.jpg");
//        assertEquals(item.mSummary, "This weekend, Personal Capital conducted a survey of some of its users regarding the potential repeal of " +
//                "the Department of Labor’s Fiduciary Rule and Section 1033 of Dodd-Frank. The...");
//        assertEquals(item.mDate, "Thu, 09 Feb 2017 17:00:22 +0000");
//        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/personal-capital-news/consumers-scream-fiduciary-rule-section-1033-dodd-frank/");
//    }

    @Test
    public void parseXMLTest() throws IOException, XmlPullParserException {
        InputStream inputStream = testContext.getAssets().open("feed_test.xml"); //test file contains rss feed
        ArrayList<PCXMLparser.Item>itemStorage = new ArrayList<PCXMLparser.Item>();
        PCXMLparser.Item item = null;
        PCXMLparser pcxmLparser = new PCXMLparser();

        itemStorage = pcxmLparser.parseXML(inputStream);

        //test item1's contents
        item = itemStorage.get(0);
        assertEquals(item.mTitle, "Personal Capital Retire Smart Guide – 65 Expert Tips");
        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/05/780X300-retire-smart-blog.jpg");
        assertEquals(item.mSummary, "Retirement isn’t the end - instead, it’s the beginning of an " +
                "exciting, brand-new chapter in your life. Every day, our advisors chat with people just like you about planning, saving...");
        assertEquals(item.mDate, "Thu, 04 May 2017 15:05:20 +0000");
        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/financial-planning-2/personal-capital-retire-smart-guide-65-expert-tips/");

        //test item20's contents
        item = itemStorage.get(19);
        assertEquals(item.mTitle, "Personal Capital Tax Series – 5: Is an Annuity Right for You?");
        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/03/annuities-taxes-blog-post-header.jpg");
        assertEquals(item.mSummary, "If you could purchase one product that promised to give you a steady income for life and take the " +
                "worry out of outliving your assets, would you jump at the chance? While it sounds like an...");
        assertEquals(item.mDate, "Mon, 20 Mar 2017 17:03:08 +0000");
        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/financial-planning-2/personal-capital-tax-series-5-annuity-right/");

        //test last item's contents
        item = itemStorage.get(itemStorage.size()-1);
        assertEquals(item.mTitle, "Consumers Scream About Fiduciary Rule and Section 1033 of Dodd-Frank");
        assertEquals(item.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/02/angry-mob-blog.jpg");
        assertEquals(item.mSummary, "This weekend, Personal Capital conducted a survey of some of its users regarding the potential repeal of " +
                "the Department of Labor’s Fiduciary Rule and Section 1033 of Dodd-Frank. The...");
        assertEquals(item.mDate, "Thu, 09 Feb 2017 17:00:22 +0000");
        assertEquals(item.mArticleLink, "https://www.personalcapital.com/blog/personal-capital-news/consumers-scream-fiduciary-rule-section-1033-dodd-frank/");
    }
}
