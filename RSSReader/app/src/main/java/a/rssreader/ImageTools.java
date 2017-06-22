package a.rssreader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;


/**
 * Tools to manipulate images to avoid out of memory errors
 * Scales full size image to a smaller image
 */
public class ImageTools {
    public ImageTools(){}

    //resize an image to display efficiently(inSampleSize)
    //Resources resources
    //int icon image to resize
    //int mReqWidth width of display container
    //int mReqHeight height of display container
    //return Bitmap resized image
    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int icon, int mReqWidth, int mReqHeight){
        //check image properties
        //avoid memory allocation
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //create bitmap
        BitmapFactory.decodeResource(resources, icon, options);
        //calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);
        //decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(resources, icon, options);
    }

    /**
     * retrieves Bitmap from InputStream created using URL
     * @param inputStream0
     * @param url
     * @param mReqWidth width of bitmap
     * @param mReqHeight height of bitmap
     * @return bitmap from InputStream
     */
    public static Bitmap decodeSampleBitmapFromStream(InputStream inputStream0, String url, int mReqWidth, int mReqHeight){
        //check image properties
        //avoid memory allocation
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream0, new Rect(), options);//create bitmap

        //calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);
        //decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        try {//InputStream must be loaded again after decodeStream()
            inputStream0 = new Download().downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(inputStream0, new Rect(), options);
    }

    //calculate size of image. so image can be displayed efficiently
    //BitmapFactory.Options options
    //int reqWidth width of display container
    //int reqHeight height of display container
    //return int size of image
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        //original image properties
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //image is too big, inefficient
        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            //calculate largest inSampleSize value thats is power of 2 and keep
            //height and weight larger than required
            while((halfHeight/inSampleSize) >= reqHeight &&
                    (halfWidth/inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

