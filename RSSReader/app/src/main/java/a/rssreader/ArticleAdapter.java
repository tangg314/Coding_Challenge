package a.rssreader;

import a.rssreader.PCXMLparser.*;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * manages RecycleView's elements. Sets up how elements' display
 * article contents.
 *
 * referenced Google's training,
 * https://developer.android.com/training/material/lists-cards.html
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private ArrayList<Item> mArticleStorage = null;
    private ProgressBar mProgressBar = null;
    private static final int HEADER_VIEW_TYPE = 0;
    private static final int NORMAL_VIEW_TYPE = 1;
    private Resources mRes = null;

    /**
     * public constructor.
     * @param articleStorage contains articles
     */
    public ArticleAdapter(ArrayList<Item> articleStorage, Resources res){
        mArticleStorage = articleStorage;
        mRes = res;
    }

    /**
     * For each article. Displays different content of article.
     */
    public static class ArticleViewHolder extends RecyclerView.ViewHolder{
        CardView cardView = null;
        ImageView articlePic = null;
        TextView articleTitle = null;
        TextView articleDate = null;
        TextView articleSummary = null;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.articleCardView);
            articlePic = (ImageView)itemView.findViewById(R.id.articlePic);
            articleTitle = (TextView)itemView.findViewById(R.id.articleTitle);
            articleDate = (TextView)itemView.findViewById(R.id.articleDate);
            articleSummary = (TextView)itemView.findViewById(R.id.articleSummary);
        }
    }

    /**
     * Sets up new article's layout.
     * @return new article's layout
     */
    private CardView setupNewArticleView(View view, Context context){
        int length_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                view.getContext().getResources().getDisplayMetrics());
        int length_20 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                view.getContext().getResources().getDisplayMetrics());

        //CardView layout
        //ArticleAdapter requires R.layout so using XML
        CardView cardViewLayout = (CardView)view.findViewById(R.id.newArticleCardView);
        CardView.LayoutParams params = new CardView.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewLayout.setLayoutParams(params);
        cardViewLayout.setRadius(0);//corner radius

        //FrameLayout
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(R.id.newarticleCardViewFrameLayout);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(frameParams);
        cardViewLayout.addView(frameLayout);

        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams linearLayoutParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0,0,0,length_20);
        cardViewLayout.addView(linearLayout);

        //article picture
        ImageView articlePic = new ImageView(context);
        articlePic.setId(R.id.articlePic);
        ViewGroup.LayoutParams articlePicParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articlePic.setLayoutParams(articlePicParams);
        linearLayout.addView(articlePic);

        //article title
        TextView articleTitle = new TextView(context);
        articleTitle.setId(R.id.articleTitle);
        ViewGroup.LayoutParams articleTitleParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articleTitle.setLayoutParams(articleTitleParams);
        articleTitle.setGravity(Gravity.TOP|Gravity.LEFT);
        articleTitle.setLines(1); //max number of lines
        articleTitle.setEllipsize(TextUtils.TruncateAt.END); //ellipsize at end
        articleTitle.setTextSize(24);
        articleTitle.setTypeface(Typeface.DEFAULT_BOLD);
        articleTitle.setPadding(length_16,0,length_16,0);
        linearLayout.addView(articleTitle);

        //article date
        TextView articleDate = new TextView(context);
        articleDate.setId(R.id.articleDate);
        ViewGroup.LayoutParams articleDateParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articleDate.setLayoutParams(articleDateParams);
        articleDate.setGravity(Gravity.TOP|Gravity.LEFT);
        articleDate.setTextSize(12);
        articleDate.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        articleDate.setPadding(length_16,0,length_16,0);
        linearLayout.addView(articleDate);

        //article summary
        TextView articleSummary = new TextView(context);
        articleSummary.setId(R.id.articleSummary);
        ViewGroup.LayoutParams articleSummaryParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articleSummary.setLayoutParams(articleSummaryParams);
        articleSummary.setGravity(Gravity.TOP|Gravity.LEFT);
        articleSummary.setLines(2); //max number of lines
        articleSummary.setEllipsize(TextUtils.TruncateAt.END); //ellipsize at end
        articleSummary.setTextSize(14);
        articleSummary.setPadding(length_16,0,length_16,0);
        linearLayout.addView(articleSummary);

        return cardViewLayout;
    }

    /**
     * Sets up article's CardView layout.
     * @param view
     * @param context
     * @return Article's CardView
     */
    private CardView setupCardView(View view, Context context){
        int length_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                view.getContext().getResources().getDisplayMetrics());
        int length_1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                view.getContext().getResources().getDisplayMetrics());
        //CardView layout
        //ArticleAdapter requires R.layout so using XML
        CardView cardViewLayout = (CardView)view.findViewById(R.id.articleCardView);
        CardView.LayoutParams params = new CardView.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewLayout.setLayoutParams(params);
        cardViewLayout.setRadius(0);//corner radius

        //FrameLayout
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(R.id.articleCardViewFrameLayout);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(frameParams);
        cardViewLayout.addView(frameLayout);

        //LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams linearLayoutParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(length_1,length_1,length_1,length_1);
        frameLayout.addView(linearLayout);

        //article picture
        ImageView articlePic = new ImageView(context);
        articlePic.setId(R.id.articlePic);
        ViewGroup.LayoutParams articlePicParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articlePic.setLayoutParams(articlePicParams);
        linearLayout.addView(articlePic);

        //article title
        TextView articleTitle = new TextView(context);
        articleTitle.setId(R.id.articleTitle);
        ViewGroup.LayoutParams articleTitleParams = new ViewGroup.LayoutParams(
                //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        articleTitle.setLayoutParams(articleTitleParams);
        articleTitle.setTextSize(12);
        articleTitle.setTextColor(Color.BLACK);
        articleTitle.setTypeface(Typeface.DEFAULT_BOLD);
        articleTitle.setBackgroundColor(Color.WHITE);
        articleTitle.setAlpha((float) 0.7);
        articleTitle.setGravity(Gravity.TOP|Gravity.LEFT);
        articleTitle.setLines(2); //max number of lines
        articleTitle.setEllipsize(TextUtils.TruncateAt.END); //ellipsize at end
        articleTitle.setPadding(length_16,0,length_16,0);

        LinearLayout titleLayout = new LinearLayout(context);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
          //width, height
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleLayout.setLayoutParams(titleLayoutParams);
        titleLayout.setGravity(Gravity.BOTTOM);
        titleLayout.addView(articleTitle);
        titleLayout.setPadding(length_1,length_1,length_1,length_1);
        frameLayout.addView(titleLayout);

        return cardViewLayout;
    }

    /**
     * sets up progress bar
     * @param view
     * @return progress bar
     */
    private ProgressBar setupProgressBar(View view, int viewType){
        FrameLayout artilceFrameLayout = (FrameLayout)view.findViewById(R.id.articleCardViewFrameLayout);
        FrameLayout newArticleFrameLayout = (FrameLayout)view.findViewById(R.id.newarticleCardViewFrameLayout);

        int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                view.getContext().getResources().getDisplayMetrics());
        ProgressBar progressBar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleSmall);
        progressBar.setId(R.id.bitmapProgressBar);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                //width, height
        length, length);
        progressBar.setLayoutParams(params);

        //add progress bar
        if(viewType == NORMAL_VIEW_TYPE){//add to other articles
            artilceFrameLayout.addView(progressBar);
        }
        else{//add to latest article
            newArticleFrameLayout.addView(progressBar);
        }

        return progressBar;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        //create a new element's view for RecycleView
        if(viewType == NORMAL_VIEW_TYPE){//normal article
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.articlecardview, parent, false);//layout for normal RecyclerView element
            setupCardView(view, parent.getContext());
            mProgressBar = setupProgressBar(view, NORMAL_VIEW_TYPE);
        }
        else if(viewType == HEADER_VIEW_TYPE){//latest article
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.newarticlecardview, parent, false);//layout for header RecyclerView element
            setupNewArticleView(view, parent.getContext());
            mProgressBar = setupProgressBar(view, HEADER_VIEW_TYPE);
        }

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        final Item article = mArticleStorage.get(position); // article at position.
        Storage storage = null;
        int height_normal = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, mRes.getDisplayMetrics());//90dp
        int height_header = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, mRes.getDisplayMetrics());//150dp
        int screenWidth_dp = mRes.getConfiguration().smallestScreenWidthDp;

        //specify each RecyclerView element's contents
        if(position > 0){//other articles
             storage = new Storage(
                    article.mImageLink, //Image link for article at "position"
                    holder, position, null, mProgressBar, screenWidth_dp/2, height_normal); //other parameters
        }
        else{//latest articles
             storage = new Storage(
                    article.mImageLink, //Image link for article at "position"
                    holder, position, null, mProgressBar, screenWidth_dp, height_header); //other parameters
        }

        new Download().SetBitmap(storage);//set article picture
        holder.articleTitle.setText(article.mTitle);//set article title

        //clicking on article's picture launches article
        holder.articlePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ArticleFullPageActivity.class);
                intent.putExtra("url",
                        article.mArticleLink + //Article link for article at "position"
                                "?displayMobileNavigation=0"); //removes web navigation bar
                intent.putExtra("title", article.mTitle);

                view.getContext().startActivity(intent);
            }
        });

        if(position == 0){//latest article has additional fields
            SimpleDateFormat parse_format = new SimpleDateFormat("EEE, dd MMM yyyy");//RSS format
            SimpleDateFormat print_format = new SimpleDateFormat("MMM dd, yyyy");//display format
            Date date = null;
            try {
                    date = parse_format.parse(article.mDate);
            }
            catch (ParseException e) {e.printStackTrace();}

            holder.articleDate.setText(print_format.format(date));
            holder.articleSummary.setText(article.mSummary);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position){
        int viewType = -1;

        //other articles
        if(position > 0){
            viewType = NORMAL_VIEW_TYPE;
        }
        //latest article
        else{
            viewType = HEADER_VIEW_TYPE;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        //number of articles
        return mArticleStorage.size();
    }

    /**
     * stores data for use in adapter
     */
    protected class Storage{
        protected String mUrl = null; //article link
        protected ArticleViewHolder mHolder = null; //represents one article
        protected int mPosition = -1; //article position
        protected Bitmap mBitmap = null; //article bitmap
        protected ProgressBar mProgressBar = null; //bitmap progress bar
        protected int mPictureWidth = -1; //bitmap width
        protected int mPictureHeight = -1; //bitmap height

        private Storage(String url, ArticleViewHolder holder, int position, Bitmap bitmap, ProgressBar progressBar, int pictureWidth, int pictureHeight){
            mUrl = url;
            mHolder = holder;
            mPosition = position;
            mBitmap = bitmap;
            mProgressBar = progressBar;
            mPictureWidth = pictureWidth;
            mPictureHeight = pictureHeight;
        }
    }
}
