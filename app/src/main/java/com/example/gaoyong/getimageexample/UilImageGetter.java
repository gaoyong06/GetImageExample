package com.example.gaoyong.getimageexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

import java.io.File;

/**
 * @author gao yong (gaoyong06[at]qq[dot]com)
 * inspire from: https://github.com/nostra13/Android-Universal-Image-Loader/issues/451
 * description:
 *          使用 Android-Universal-Image-Loader 实现 android Html.fromHtml (String source, Html.ImageGetter imageGetter, Html.TagHandler tagHandler)中的imageGetter方法.
 * sample:
 *          String htmlSnippetCode = "如图，已知抛物线 y=ax<sup>2</sup>+bx+c（a≠0）的顶点坐标为（4，<img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iQLN4bAAABOwdsNv0023.png\" height=\"42\" width=\"7\">），且与y轴交于点C，与x轴交于A，B两点（点A在点B的左边），且A点坐标为（2，0）。在抛物线的对称轴 <img src=\"http://p2.qingguo.com/G1/M00/9F/B2/rBACE1PfJ7iTXZhbAAABESQziLA683.png\" height=\"21\" width=\"6\">上是否存在一点P，使AP+CP的值最小？若存在，则AP+CP的最小值为（&nbsp;&nbsp;&nbsp;&nbsp;）<img src=\"http://p1.qingguo.com/G1/M00/62/2A/rBACFFPfJ7iiO8gTAAALHRPDATw26.jpeg\" height=\"165\" width=\"193\">";
 *          TextView TVMain = (TextView) this.findViewById(R.id.TVMain);
 *          UilImageGetter imageGetter = new UilImageGetter(TVMain,this);
 *          Spanned spanned = Html.fromHtml(htmlSnippetCode, imageGetter, null);
 *          TVMain.setText(spanned);
 */
public class UilImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView view;
    private int viewWillResetHeight;
    private CustomImageLoadingListener listener;
    private GIEApplication application;

    public UilImageGetter(TextView view,Context context,GIEApplication application) {

        this.application = application;
        this.context = context;
        this.view = view;
    }

    @Override
    public Drawable getDrawable(String source) {

        Log.d(Constants.tag, "UilImageGetter getDrawable is start PID = " + android.os.Process.myPid() + " Thread ID = " + Thread.currentThread().getId());
        URLDrawable urlDrawable = new URLDrawable(UilImageGetter.this.context);
        urlDrawable.setBounds(0, 0, urlDrawable.getIntrinsicWidth(), urlDrawable.getIntrinsicHeight());
        //实现方法一
//        listener = new CustomImageLoadingListener(urlDrawable);
//        ImageLoader.getInstance().loadImage(source,listener);

        //实现方法二
        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
        asyncTask.execute(source);

        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String source = params[0];
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(source);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            UilImageGetter.this.replaceImage(result,urlDrawable);
        }
    }

    private class CustomImageLoadingListener extends SimpleImageLoadingListener {

        private URLDrawable urlDrawable;

        CustomImageLoadingListener(URLDrawable urlDrawable){

            this.urlDrawable = urlDrawable;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {

            super.onLoadingStarted(imageUri,view);
            File discCacheImg = DiskCacheUtils.findInCache(imageUri,UilImageGetter.this.application.discCache);
            Log.d(Constants.tag, "CustomImageLoadingListener onLoadingStarted is run discCacheImg = " + discCacheImg);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            //TODO:
            //do something
            super.onLoadingFailed(imageUri,view,failReason);
            Log.d(Constants.tag, "CustomImageLoadingListener onLoadingFailed is run imageUri = " + imageUri);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            super.onLoadingComplete(imageUri, view, loadedImage);
            UilImageGetter.this.replaceImage(loadedImage,this.urlDrawable);
            File discCacheDir = UilImageGetter.this.application.discCache.getDirectory();
            Log.d(Constants.tag, "CustomImageLoadingListener onLoadingComplete is run discCacheDir = " + discCacheDir);
            File discCacheImg = UilImageGetter.this.application.discCache.get(imageUri);
            Log.d(Constants.tag, "CustomImageLoadingListener onLoadingComplete is run discCacheImg = " + discCacheImg);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

            super.onLoadingCancelled(imageUri,view);
            //TODO:
                //do something
        }
    }

    public void replaceImage(final Bitmap loadedImage,final URLDrawable urlDrawable) {

        //http://stackoverflow.com/questions/18359806/linearlayouts-width-and-height-are-zero
        //备注:
        //  当图片是从网络获取时:UilImageGetter.this.view.getHeight() 大于0
        //  当图片是从文件缓存目录获取时:UilImageGetter.this.view.getHeight() 等于0 [等于0就出问题了]
        //  等于0的原因是replaceImage执行时UilImageGetter.this.view还未完成布局尺寸的计算,解决办法时把replaceImage的业务放到UI Thread的message Queue里面.
        //  待能处理这个Runnable对象时,UilImageGetter.this.view 应该已经完成布局尺寸计算了.
        this.view.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (null != loadedImage) {

                    //按照240dip,对px和dp做处理
                    int loadedImageWidth = (int) Math.round(loadedImage.getWidth() * 1.5);
                    int loadedImageheight = (int) Math.round(loadedImage.getHeight() * 1.5);
                    Drawable result = new BitmapDrawable(context.getResources(), loadedImage);
                    result.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                    urlDrawable.setBounds(0, 0, loadedImageWidth, loadedImageheight);
                    urlDrawable.drawable  = result;
                    UilImageGetter.this.view.invalidate();

                    if(0 != UilImageGetter.this.viewWillResetHeight) {

                        UilImageGetter.this.viewWillResetHeight += loadedImageheight;

                    }else {

                        UilImageGetter.this.viewWillResetHeight = UilImageGetter.this.view.getHeight() + loadedImageheight;
                    }

                    //http://stackoverflow.com/questions/7870312/android-imagegetter-images-overlapping-text
                    // For ICS
                    UilImageGetter.this.view.setHeight(UilImageGetter.this.viewWillResetHeight);

                    // Pre ICS
                    UilImageGetter.this.view.setEllipsize(null);
                }
            }
        } );
    }
}
