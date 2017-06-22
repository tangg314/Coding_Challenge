package a.rssreader;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.allOf;

/**
 * tests app's UI.
 * loadXmlFromNetwork()from MainActivity must be made public to test.
 */
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<MainActivity>(
                                                                MainActivity.class);

    @Test
    public void press_refreshButton() throws IOException, XmlPullParserException {
        //checks everything refreshes after refresh button is pressed.
        MainActivity mainActivity = mainActivityRule.getActivity();//access activity

        //click refresh button, articles should reload
        onView(withId(R.id.refreshButton)).perform(click());

        displayToolBar();//check tool bar
        displayLatest_Article();//check articles
        displayOther_Article();
    }

    @Test
    public void displayToolBar(){
        //checks the toolbar and content is displayed
        MainActivity mainActivity = mainActivityRule.getActivity();//access activity

        //check tool bar title is displayed
        onView(withId(R.id.toolBarTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.toolBarTitle)).check(matches(withText("Personal Capital")));
        //check refresh button is displayed
        onView(withId(R.id.refreshButton)).check(matches(isDisplayed()));
    }

    @Test
    public void displayLatest_Article() throws IOException, XmlPullParserException {
        //checks the latest article is displaying correct content
        MainActivity mainActivity = mainActivityRule.getActivity();//access activity
        final String url = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";//RSS url
        ArrayList<PCXMLparser.Item> storage = null;//stores articles

        storage = new Download().loadXmlFromNetwork(url);//download articles
        String articleTitle = storage.get(0).mTitle;
        String articleSummary = storage.get(0).mSummary;

        onView(withId(R.id.articleRecyclerView))//check article picture is present
                .perform(scrollTo(withId(R.id.newArticleCardView)))//only position 0 has newArticleCardView, so no need to specify position
                .check(matches(hasDescendant(withId(R.id.articlePic))));

        onView(withId(R.id.articleRecyclerView))//check article title is present
                .perform(scrollTo(withId(R.id.newArticleCardView)))
                .check(matches(hasDescendant(withText(articleTitle))));

        onView(withId(R.id.articleRecyclerView))//check article date is present
                .perform(scrollTo(withId(R.id.newArticleCardView)))
                .check(matches(hasDescendant(withId(R.id.articleDate))));

        onView(withId(R.id.articleRecyclerView))//check article summary is present
                .perform(scrollTo(withId(R.id.newArticleCardView)))
                .check(matches(hasDescendant(withText(articleSummary))));
    }

    @Test
    public void displayOther_Article() throws IOException, XmlPullParserException {
        //checks the latest article is displaying correct content
        MainActivity mainActivity = mainActivityRule.getActivity();//access activity
        final String url = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";//RSS url
        ArrayList<PCXMLparser.Item> storage = null;//stores articles

        storage = new Download().loadXmlFromNetwork(url);//download articles
        int position_end = storage.size()-1;
        String articleTitle_1 = storage.get(1).mTitle;//article title for article at position 1
        String articleSummary_1 = storage.get(1).mSummary;//article summary for article at position 1
        String articleTitle_end = storage.get(position_end).mTitle;//article title for article at position end
        String articleSummary_end = storage.get(position_end).mSummary;//article summary for article at position end

        //position_1
        onView(withId(R.id.articleRecyclerView))//check article picture is present
                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(1))//articleCardView are in position 1-end, position is important
                .check(matches(hasDescendant(withId(R.id.articlePic))));

        onView(withId(R.id.articleRecyclerView))//check article title is present
                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(1))
                .check(matches(hasDescendant(withText(articleTitle_1))));

//cannot scrollTo position_end
//        //position_end
//        onView(withId(R.id.articleRecyclerView))//check article picture is present
//                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(position_end))//articleCardView are in position 1-end, position is important
//                .check(matches(hasDescendant(withId(R.id.articlePic))));
//
//        onView(withId(R.id.articleRecyclerView))//check article title is present
//                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(position_end))
//                .check(matches(hasDescendant(withText(articleTitle_end))));

        //the following should fail
//        onView(withId(R.id.articleRecyclerView))//check article summary is present
//                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(1))
//                .check(matches(hasDescendant(withText(articleSummary_1))));
//
//        onView(withId(R.id.articleRecyclerView))//check article summary is present
//                .perform(scrollTo(withId(R.id.articleCardView)).atPosition(position_end))
//                .check(matches(hasDescendant(withText(articleSummary_end))));
    }

    @Test
    public void clickon_Article() throws XmlPullParserException, IOException {
        //article is clicked on
        //check toolbar title and article WebView is displayed
        MainActivity mainActivity = mainActivityRule.getActivity();//access activity
        final String url = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";//RSS url
        ArrayList<PCXMLparser.Item> storage = null;//stores articles

        storage = new Download().loadXmlFromNetwork(url);//download articles
        String articleTitle = storage.get(1).mTitle;
        String articleUrl = storage.get(1).mArticleLink;

        //click on article's picture to launch full page article
        onView(withId(R.id.articleRecyclerView))
                .perform(actionOnItemAtPosition(1, click()));

        //check title in tool bar is displayed
        onView(withId(R.id.articleFullPageTitle)).check(matches(isDisplayed()));
        //check title is correct
        onView(withId(R.id.articleFullPageTitle)).check(matches(withText(articleTitle)));
        //check WebView that displays article is displayed.
        onView(withId(R.id.articleFullPageWebView)).check(matches(isDisplayed()));
    }
}
