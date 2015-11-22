package com.spark.material9gag.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.spark.material9gag.R;
import com.spark.material9gag.data.ImageCacheManager;
import com.spark.material9gag.model.Feed;
import com.spark.material9gag.util.DensityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Spark on 11/19/2015.
 */
public class FeedsAdapter extends CursorAdapter {

    private static final int[] COLORS = {R.color.holo_blue_light, R.color.holo_green_light,
            R.color.holo_orange_light, R.color.holo_purple_light, R.color.holo_red_light};

    private static final int IMAGE_MAX_HEIGHT = 240;
    private Drawable mDefaultImageDrawable;
    private ListView mListView;
//    private Resources mResource;
    private LayoutInflater mLayoutInflater;

    public FeedsAdapter(Context context, ListView listView) {
        super(context, null, false);
//        mResource = context.getResources();
        mLayoutInflater = LayoutInflater.from(context);
        mListView = listView;
    }

    @Override
    public Feed getItem(int position) {
        getCursor().moveToPosition(position);
        return Feed.fromCursor(getCursor());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.listitem_feed, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Feed feed = Feed.fromCursor(cursor);
        view.setEnabled(!mListView.isItemChecked(cursor.getPosition()
                + mListView.getHeaderViewsCount()));
        holder.imageRequest = ImageCacheManager.loadImage(feed.getImages().normal, ImageCacheManager
                .getImageListener(holder.imageView, mDefaultImageDrawable, mDefaultImageDrawable), 0, DensityUtils.dip2px(context, IMAGE_MAX_HEIGHT));
        holder.caption.setText(feed.getCaption());
    }

     class ViewHolder{
        @Bind(R.id.iv_normal)
        ImageView imageView;
        @Bind(R.id.tv_caption)
        TextView caption;

        public ImageLoader.ImageContainer imageRequest;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
