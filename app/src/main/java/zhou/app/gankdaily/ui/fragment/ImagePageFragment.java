package zhou.app.gankdaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.util.LogKit;

public class ImagePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView icon = (ImageView) inflater.inflate(R.layout.fragment_image_page, container, false);
        icon.setClickable(true);
        Bundle b = getArguments();
        if (b != null) {
            String url = b.getString(Config.Static.URL);
            LogKit.i("url",url);
            Glide.with(getContext()).load(url).into(icon);
//            Picasso.with(getActivity()).load(url).into(icon);
//            icon.setOnClickListener({ v ->
//                if (icon.getDrawable()) {
//                    ArrayList<String> urls = new ArrayList<>(1)
//                    urls << url
//                    Intent intent = new Intent(getActivity(), ImageGalleryActivity.class)
//                    intent.putStringArrayListExtra(Config.Static.URLS, urls)
//                    intent.putExtra(Config.Static.POSITION, 0)
//                    startActivity(intent)
//                }
//            })
        }
        return icon;
    }


    public static ImagePageFragment newInstance(String url) {
        ImagePageFragment imagePageFragment = new ImagePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Config.Static.URL, url);
        imagePageFragment.setArguments(bundle);
        return imagePageFragment;
    }
}