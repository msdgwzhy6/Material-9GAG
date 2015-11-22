package com.spark.material9gag;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PicDetailActivity extends AppCompatActivity {

    @Bind(R.id.pv_pic)
    PhotoView pvPic;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private String imgUrl;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);
        ButterKnife.bind(this);
        imgUrl = getIntent().getStringExtra("IMAGE_URL");
        photoViewAttacher = new PhotoViewAttacher(pvPic);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).considerExifParams(true).build();
        ImageLoader.getInstance().displayImage(imgUrl,pvPic,options,
                new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                photoViewAttacher.update();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(photoViewAttacher != null){
            photoViewAttacher.cleanup();
        }
    }
}
