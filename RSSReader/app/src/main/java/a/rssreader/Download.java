package a.rssreader;

import a.rssreader.ArticleAdapter.*;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * class responsible for downloading content such as Bitmap.
 */
public class Download {
    private Storage mStorage = null;

    /**
     * downloads and sets bitmap using storage items
     * @param storage contains data needed
     */
    public void SetBitmap(Storage storage){
        mStorage = storage;
        DownloadBitmapTask downloadBitmapTask = new DownloadBitmapTask();
        downloadBitmapTask.execute(storage);
    }

    private class DownloadBitmapTask extends AsyncTask<Storage, Void, Storage> {
        @Override
        protected Storage doInBackground(Storage... storages) {
            Bitmap bitmap = null;

            try {
                bitmap = loadBitmapFromNetwork(storages[0].mUrl, storages[0].mPictureWidth, storages[0].mPictureHeight);
                storages[0].mBitmap = bitmap;
            }
            //todo error
            catch (IOException e) {
                e.printStackTrace();
            }
            return storages[0];
        }

        @Override
        protected void onPostExecute(Storage storage) {
            super.onPostExecute(storage);
            if(storage.mProgressBar != null){//done loading image. Hide progress bar.
                storage.mProgressBar.setVisibility(View.GONE);
            }

            ImageView articlePicture = storage.mHolder.articlePic;
            int height_normal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, articlePicture.getResources().getDisplayMetrics());//90dp
            int height_header = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, articlePicture.getResources().getDisplayMetrics());//150dp

            //other articles
            if(storage.mPosition > 0){
                articlePicture.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height_normal));
            }
            //latest article, header RecycleView
            else{
                articlePicture.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height_header));
            }

            articlePicture.setAdjustViewBounds(true); //keep aspect ratio
            articlePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            articlePicture.setImageBitmap(storage.mBitmap);
        }
    }

    /**
     * Retrieves RSS articles from URL
     * @param urlString location of RSS
     * @return ArrayList of articles
     * @throws XmlPullParserException
     * @throws IOException
     */
    protected ArrayList<PCXMLparser.Item> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException{
        PCXMLparser pcxmLparser = new PCXMLparser(); //parses articles and returns them in ArrayList
        ArrayList<PCXMLparser.Item> articleStorage = null;
        InputStream stream = null;

        try{
            //retrieve articles from URL
            stream = downloadUrl(urlString);
            articleStorage = pcxmLparser.parseXML(stream);
        }
        finally {
            //done with stream
            if(stream != null){
                stream.close();
            }
        }

        return articleStorage;
    }

    /**
     * Retrieves bitmap from URL
     * @param urlString location of bitmap
     * @param width width of picture to load
     * @param height height of picture to load
     * @return bitmap retrieved
     * @throws IOException
     */
    protected Bitmap loadBitmapFromNetwork(String urlString, int width, int height) throws IOException{
        Bitmap results = null;
        InputStream stream = null;

        try{
            //retrieve bitmap
            stream = downloadUrl(urlString);
            results = new ImageTools().decodeSampleBitmapFromStream(stream, urlString, width, height);
        }
        finally {
            //done with stream
            if(stream != null){
                stream.close();
            }
        }

        return results;
    }

    /**
     * Connects to URL and makes InputStream
     * @param urlString
     * @return
     * @throws IOException
     */
    protected InputStream downloadUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setReadTimeout(10000);//milliseconds
        conn.setConnectTimeout(15000);//milliseconds
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        //start query
        conn.connect();

        return conn.getInputStream();
    }
}
