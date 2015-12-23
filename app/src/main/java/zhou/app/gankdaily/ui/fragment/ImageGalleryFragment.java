package zhou.app.gankdaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.ui.adapter.ImageGalleryAdapter;

public class ImageGalleryFragment extends BaseFragment {

    public static final int ID_SHARE = 0x23456;
    public static final int ID_SAVE = 0x34567;

    List<String> urls;
    int position;
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        if (b != null) {
            urls = b.getStringArrayList(Config.Static.URLS);
            position = b.getInt(Config.Static.POSITION, 0);
        }
        setTitle(String.format("%d/%d", position + 1, urls == null ? 0 : urls.size()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPager = (ViewPager) inflater.inflate(R.layout.fragment_viewpager, container, false);
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(urls);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position, false);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(String.format("%d/%d", position + 1, urls == null ? 0 : urls.size()));
            }
        });
        return viewPager;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.add(0, ID_SHARE, 0, R.string.share);
//        menu.add(0, ID_SAVE, 0, R.string.save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ID_SHARE:
//                String url = urls?.get(viewPager.getCurrentItem());
//                Picasso.with(getActivity()).load(url).into(new Target() {
//                    @Override
//                    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        File file = new File(App.getInstance().getExternalCacheDir(), "temp.jpg")
//                        FileKit.saveBitmapFile(bitmap, file);
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("image/*");
//                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//                        startActivity(intent);;
//                    }
//
//                    @Override
//                    void onBitmapFailed(Drawable errorDrawable) {
//                        Toast.makeText(getActivity(), R.string.error_get_image, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
                return true;
            case ID_SAVE:
//                String url = urls?.get(viewPager.getCurrentItem());
//                File file = new File(App.SAVE_PATH, FileKit.getFileRealName(url));
//                Picasso.with(getActivity()).load(url).into(new Target() {
//                    @Override
//                    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        FileKit.saveBitmapFile(bitmap, file)
//                        Toast.makeText(getActivity(), "图片保存在:${file.getAbsolutePath()}", Toast.LENGTH_LONG).show()
//                    }
//
//                    @Override
//                    void onBitmapFailed(Drawable errorDrawable) {
//                        Toast.makeText(getActivity(), R.string.error_get_image, Toast.LENGTH_SHORT).show()
//                    }
//
//                    @Override
//                    void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ImageGalleryFragment newInstance(ArrayList<String> urls, int position) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Config.Static.URLS, urls);
        bundle.putInt(Config.Static.POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}