package zhou.app.gankdaily.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import rx.functions.Action1;
import zhou.app.gankdaily.R;

public class ImageGalleryAdapter extends PagerAdapter {

    List<String> urls;

    public ImageGalleryAdapter(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls == null ? 0 : urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.layout_gallery, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.image);

        View error = v.findViewById(R.id.error_layout);
//        View progress = v.findViewById(R.id.progressBar)

        Action1 loadImage = view -> {
//            progress.setVisibility(View.VISIBLE)
            error.setVisibility(View.GONE);

            Glide.with(context).load(urls.get(position)).into(imageView);
//
//            Picasso.with(context).load(urls[position]).into(imageView, new Callback() {
//                @Override
//                void onSuccess() {
////                    progress.setVisibility(View.GONE)
//                }
//
//                @Override
//                void onError() {
////                    progress.setVisibility(View.GONE)
//                    error.setVisibility(View.VISIBLE)
//                }
//            })
        };

//        error.setOnClickListener(loadImage)

        loadImage.call(null);

        container.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}