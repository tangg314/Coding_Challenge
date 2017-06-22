package a.rssreader;

import a.rssreader.PCXMLparser.*;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * test loading the first article.
 * parameters are live and need to be updated before test.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>
            (MainActivity.class);

    @Test
    public void loadXmlFromNetworkTest() throws IOException, XmlPullParserException {
        String url = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284"; //Personal Capital RSS URL
//        MainActivity activity = rule.getActivity(); //access to MainActivity
        ArrayList<Item> articleStorage = new ArrayList<Item>();
        Item article = null;
        //retrieve articles from URL
        //make method public to run test
        articleStorage = new Download().loadXmlFromNetwork(url);

        //test item1's contents
        //contents are live. Test parameters below need to change to current parameters.
        //check url
        article = articleStorage.get(0);

        assertEquals(article.mTitle, "WannaCry Security Statement");
        assertEquals(article.mImageLink, "https://www.personalcapital.com/blog/wp-content/uploads/2017/05/cyber-security-blog-header-image-nick.jpg");
        assertEquals(article.mSummary, "Last week, a global cyberattack impacted hundreds of thousands of computer systems around the world. Cybercriminals " +
                "have deployed a malicious form of software known as ransomware, which...");
        assertEquals(article.mDate, "Wed, 17 May 2017 17:39:40 +0000");
        assertEquals(article.mArticleLink, "https://www.personalcapital.com/blog/personal-capital-news/wannacry-security-statement/");
    }
}
