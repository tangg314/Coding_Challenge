package a.rssreader;


import a.rssreader.PCXMLparser.*;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * retrieves XML and displays articles from XML.
 * referenced Google's Training exercise,
 * https://developer.android.com/training/basics/network-ops/xml.html#read
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView = null;
    private ArticleAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String url = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";

        setContentView(setUpLayout());//set up views
        setUpToolBar();
        setUpRecyclerView();

        loadRSSPage(url);

        //refresh button on tool bar
        //refresh main page
        Button refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRSSPage(url);
            }
        });
    }

    /**
     * sets up tool bar.
     * @return tool bar.
     */
    private Toolbar setUpToolBar(){
        LinearLayout mainActivityLayout = (LinearLayout) findViewById(R.id.MainActivityLayout);
        int length_20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int length_24 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        int length_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int length_56 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

        //toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setId(R.id.toolBar);
        ViewGroup.LayoutParams barParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, length_56);
        toolbar.setLayoutParams(barParams);
        toolbar.setBackgroundColor(Color.BLUE);
        mainActivityLayout.addView(toolbar);

        //toolbar title
        TextView toolBarTitle = new TextView(this);
        toolBarTitle.setId(R.id.toolBarTitle);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolBarTitle.setLayoutParams(titleParams);
        toolBarTitle.setText("Personal Capital");
        toolBarTitle.setTextColor(Color.WHITE);
        toolBarTitle.setTypeface(Typeface.DEFAULT_BOLD);
        toolBarTitle.setTextSize(20);
        toolBarTitle.setGravity(Gravity.CENTER_VERTICAL);

        //linear layout title
        LinearLayout linearLayoutTitle = new LinearLayout(this);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutTitle.setLayoutParams(titleLayoutParams);
        linearLayoutTitle.addView(toolBarTitle);
        linearLayoutTitle.setGravity(Gravity.LEFT);
        linearLayoutTitle.setPadding(0,0,length_16,0);
        toolbar.addView(linearLayoutTitle);

        //progress bar
        toolbar.addView(setupProgressBar());

        //refresh button
        Button refreshButton = new Button(this);
        refreshButton.setId(R.id.refreshButton);
        ViewGroup.LayoutParams buttonParams = new ViewGroup.LayoutParams(
                //width, height
                length_24, length_24);
        refreshButton.setLayoutParams(buttonParams);
        refreshButton.setBackgroundResource(R.drawable.ic_refresh_white_36dp);

        //linear layout button
        LinearLayout linearLayoutButton = new LinearLayout(this);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutButton.setLayoutParams(buttonLayoutParams);
        toolbar.addView(linearLayoutButton);
        linearLayoutButton.addView(refreshButton);
        linearLayoutButton.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutButton.setGravity(Gravity.RIGHT);
        linearLayoutButton.setPadding(0,length_16,length_16,length_16);

        return toolbar;
    }

    /**
     * sets up progress bar
     * @return progress bar
     */
    private ProgressBar setupProgressBar(){
        int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                getApplicationContext().getResources().getDisplayMetrics());
        ProgressBar progressBar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleSmall);
        progressBar.setId(R.id.xmlProgressBar);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                //width, height
                length, length);
        progressBar.setLayoutParams(params);

        return progressBar;
    }

    /**
     * setup activity layout.
     * @return layout ready to be used in setContentView().
     */
    private LinearLayout setUpLayout(){
        LinearLayout mainActivityLayout = new LinearLayout(this);
        mainActivityLayout.setId(R.id.MainActivityLayout);
        LinearLayout.LayoutParams topLayoutParams = new LinearLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainActivityLayout.setLayoutParams(topLayoutParams);
        mainActivityLayout.setOrientation(LinearLayout.VERTICAL);


        return mainActivityLayout;
    }

    /**
     * Sets up RecyclerView used to display articles
     * @return RecyclerView
     */
    private RecyclerView setUpRecyclerView(){
        LinearLayout mainActivityLayout = (LinearLayout) findViewById(R.id.MainActivityLayout);
        int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setId(R.id.articleRecyclerView);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                //width, height
                RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(params);

        mainActivityLayout.addView(recyclerView);

        return recyclerView;
    }

    /**
     * load RSS page
     * @param url location of RSS feed
     */
    private void loadRSSPage(String url){
        ConnectivityManager manager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if(isConnected){//connected to internet, download XML
            DownloadXMLTask task = new DownloadXMLTask();//download XML
            task.execute(url);
        }

        else{//not connected to internet, show error
            Toast toast = Toast.makeText(getApplicationContext(), "Please make sure you have internet access.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * download XML.
     * Connect data to RecyclerView responsible for all articles.
     */
    private class DownloadXMLTask extends AsyncTask<String, Void, ArrayList<Item>>{
        @Override
        protected ArrayList<Item> doInBackground(String... urls) {
            ArrayList<Item> results = new ArrayList<Item>();
            try{
                results = new Download().loadXmlFromNetwork(urls[0]);
            }
            //todo error message
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            //todo error message
            catch (IOException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.xmlProgressBar);
            progressBar.setVisibility(View.VISIBLE);//load articles, show progress bar
        }

        @Override
        protected void onPostExecute(ArrayList<Item> articleStorage){
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.xmlProgressBar);
            progressBar.setVisibility(View.INVISIBLE);//done loading, hide progress bar

            int screenWidth_dp = getResources().getConfiguration().smallestScreenWidthDp;
            final int spanCountCell = 2;
            final int spanCountTablet = 3;

            //Display articles
            if(recyclerView == null){//set up new RecyclerView, first time load articles
                recyclerView = (RecyclerView)findViewById(R.id.articleRecyclerView);//set up RecyclerView
                recyclerView.setItemViewCacheSize(30);//performance measures
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

                //for phones grid view has 2 columns. for tablets grid view has 3 columns.
                if(screenWidth_dp <= 600){ //cell phone size
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCountCell);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if(position == 0){//latest article, header RecycleView
                                return spanCountCell;
                            }
                            else{//other articles
                                return 1;
                            }
                        }
                    });
                    recyclerView.setLayoutManager(gridLayoutManager);
                }
                else{//tablet size
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), spanCountTablet);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if(position == 0){//latest article, header RecycleView
                                return spanCountTablet;
                            }
                            else{//other articles
                                return 1;
                            }
                        }
                    });
                    recyclerView.setLayoutManager(gridLayoutManager);
                }

                //set up ArticleAdapter
                adapter = new ArticleAdapter(articleStorage, getResources());
                recyclerView.setAdapter(adapter);
            }

            else{//refresh button pressed
                adapter.notifyDataSetChanged();
            }
        }
    }
}
