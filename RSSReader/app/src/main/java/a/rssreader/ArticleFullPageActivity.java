package a.rssreader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * responsible for displaying article in WebView.
 */
public class ArticleFullPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayout());
        setUpToolBar();
        setupProgressBar();
        setupWebview();
        Bundle bundle = getIntent().getExtras();
        String urlString = bundle.getString("url");

        //load article
        loadArticlePage(urlString);

        //article title
        TextView toolbarTitle = (TextView)findViewById(R.id.articleFullPageTitle);
        toolbarTitle.setText(bundle.getString("title"));
    }

    private void loadArticlePage(String url){
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if(isConnected){//connected to internet, download article
            //displays article
            WebView mWebView = (WebView)findViewById(R.id.articleFullPageWebView); //displays article
            mWebView.getSettings().setJavaScriptEnabled(true); //allows interaction with WebView
            mWebView.loadUrl(url); //article url
            //shows loading article progress
            mWebView.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress){
                    if(progress == 100){//done loading, make progress bar disappear
                        ProgressBar progressBar = (ProgressBar)findViewById(R.id.fullpageArticleProgressBar);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

        else{//not connected to internet, show error
            Toast toast = Toast.makeText(getApplicationContext(), "Please make sure you have internet access.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * set up activity's layout
     * @return activity's layout
     */
    private LinearLayout setupLayout(){
        //activity layout
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setId(R.id.articleFullPageLayout);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayout.setLayoutParams(params);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        return mainLayout;
    }

    /**
     * sets up tool bar.
     * @return tool bar.
     */
    private Toolbar setUpToolBar(){
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.articleFullPageLayout);
        int length_56 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

        //toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setId(R.id.fullPagetoolBar);
        ViewGroup.LayoutParams barParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, length_56);
        toolbar.setLayoutParams(barParams);
        toolbar.setBackgroundColor(Color.BLUE);
        mainLayout.addView(toolbar);

        //toolbar title
        TextView toolBarTitle = new TextView(this);
        toolBarTitle.setId(R.id.articleFullPageTitle);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolBarTitle.setLayoutParams(titleParams);
        toolBarTitle.setTextColor(Color.WHITE);
        toolBarTitle.setTypeface(Typeface.DEFAULT_BOLD);
        toolBarTitle.setTextSize(20);
        toolBarTitle.setGravity(Gravity.CENTER_VERTICAL);
        toolBarTitle.setLines(1);
        toolBarTitle.setEllipsize(TextUtils.TruncateAt.END);

        //linear layout title
        LinearLayout linearLayoutTitle = new LinearLayout(this);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutTitle.setLayoutParams(titleLayoutParams);
        linearLayoutTitle.addView(toolBarTitle);
        linearLayoutTitle.setGravity(Gravity.LEFT);
        toolbar.addView(linearLayoutTitle);

        return toolbar;
    }

    /**
     * sets up progress bar
     * @return progress bar
     */
    private ProgressBar setupProgressBar(){
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.articleFullPageLayout);
        int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                getApplicationContext().getResources().getDisplayMetrics());
        ProgressBar progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleSmall);
        progressBar.setId(R.id.fullpageArticleProgressBar);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                //width, height
                length, length);
        progressBar.setLayoutParams(params);
        mainLayout.addView(progressBar);

        return progressBar;
    }

    /**
     * sets up WebView that displays article full page.
     * @return WebView
     */
    private WebView setupWebview(){
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.articleFullPageLayout);
        //WebView to display article link
        WebView webView = new WebView(this);
        webView.setId(R.id.articleFullPageWebView);
        ViewGroup.LayoutParams webViewParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(webViewParams);
        mainLayout.addView(webView);

        return webView;
    }
}
