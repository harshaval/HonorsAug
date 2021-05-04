package co.raveblue.skeletontest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

// A class that shows how the inserted images have been adapted into the system to be shown
public class ImageAdapter extends BaseAdapter {
    private Context imContext;
    private Integer[] imImageIds;

    public ImageAdapter(Context con, Integer[] mImageIds) {
        this.imImageIds = imImageIds;
        imContext = con;
    }

    @Override
    public int getCount() {
        return imImageIds.length;
    }

    @Override
    public Object getItem(int pos) {
        return imImageIds[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(imContext);
        i.setImageResource(imImageIds[position]);
        i.setAdjustViewBounds(true);
        i.setLayoutParams(new Gallery.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        i.setBackgroundResource(imImageIds[0]);
        return i;
    }

}