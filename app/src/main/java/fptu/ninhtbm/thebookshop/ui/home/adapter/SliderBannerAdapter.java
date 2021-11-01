package fptu.ninhtbm.thebookshop.ui.home.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import fptu.ninhtbm.thebookshop.R;

public class SliderBannerAdapter extends SliderViewAdapter<SliderBannerAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    private final List<String> mSliderItems;
    private final Context mContext;

    // Constructor
    public SliderBannerAdapter(Context context, List<String> sliderDataArrayList) {
        mSliderItems = sliderDataArrayList;
        mContext = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_banner, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(mSliderItems.get(position))
                .into(viewHolder.imageViewBackground);
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.img_banner);
            this.itemView = itemView;
        }
    }
}