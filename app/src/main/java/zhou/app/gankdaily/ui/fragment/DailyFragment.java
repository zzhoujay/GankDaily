package zhou.app.gankdaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.data.DataManager;
import zhou.app.gankdaily.data.GankDailyProvider;
import zhou.app.gankdaily.model.Gank;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.ui.adapter.DailyAdapter;
import zhou.app.gankdaily.util.TimeKit;

public class DailyFragment extends BaseFragment {

    public static final int ID_REFRESH = 0x78901;

    //    ImageView icon;
    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    GankDailyProvider provider;
    DailyAdapter dailyAdapter;
    ViewPager viewPager;
    FloatingActionButton fab;
    View loading, loadingProgress, empty, error;
    boolean isMain = false;
    int year, month, day;
    int count;
    MenuItem refresh;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        int[] time = TimeKit.getTime();
        year = time[0];
        month = time[1];
        day = time[2];
        if (b != null) {
            year = b.getInt(Config.Static.YEAR, year);
            month = b.getInt(Config.Static.MONTH, month);
            day = b.getInt(Config.Static.DAY, day);
            isMain = b.getBoolean(Config.Static.IS_MAIN, false);
        }

        provider = new GankDailyProvider(year, month, day);
        provider.setNoticeable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        loading = view.findViewById(R.id.loading);
        loadingProgress = view.findViewById(R.id.progressBar);
        empty = view.findViewById(R.id.no_data);
        error = view.findViewById(R.id.error);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (isMain)
//                ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px);
                ab.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle(R.string.app_name);

        dailyAdapter = new DailyAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dailyAdapter);
        requestDaily();

        fab.setOnClickListener(v -> noticeActivity(Config.Action.CHANGE_DATE));

        error.setOnClickListener(v -> requestDaily());

        return view;
    }


    protected void setUpData(GankDaily daily) {
        if (daily != null) {
            if (daily.isEmpty()) {
                if (isMain) {
                    //为主页的情况
                    if (count > Config.Configurable.MAX_iteration) {
                        //重复加载次数过多(到达了底端)
                        setTitle(provider.getYear(), provider.getMonth(), provider.getDay());
                        setEmpty();
                    } else {
                        //加载前一天的数据
                        if (count == 0) {
                            // 重新加载今天的内容
                            DataManager.getInstance().update(provider, this::setUpData);
                            count++;
                        } else {
                            count++;
                            provider = provider.getPrevDay();
                            if (TimeKit.future(provider.getYear(), provider.getMonth(), provider.getDay())) {
                                //如如果要加载的数据是今天或以后
                                DataManager.getInstance().update(provider, this::setUpData);
                            } else {
                                DataManager.getInstance().get(provider, this::setUpData);
                            }
                        }
                    }
                } else {
                    // Empty
                    setTitle(provider.getYear(), provider.getMonth(), provider.getDay());
                    setEmpty();
                }
            } else {
                // Success
                setTitle(provider.getYear(), provider.getMonth(), provider.getDay());
                List<List<Gank>> ganks = daily.ganks;
                List<String> types = daily.types;
                List<Gank> welfares = ganks.get(types.indexOf(Config.Type.WELFARE));

                List<ImagePageFragment> fs = Stream.of(welfares).map(value -> ImagePageFragment.newInstance(value.url)).collect(Collectors.toList());

                PagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
                    @Override
                    public Fragment getItem(int i) {
                        return fs.get(i);
                    }

                    @Override
                    public int getCount() {
                        return fs.size();
                    }
                };
                viewPager.setAdapter(adapter);

                dailyAdapter.setDaily(daily);

                setSuccess();
            }
        } else {
            // Error
            setTitle(provider.getYear(), provider.getMonth(), provider.getDay());
            setError();
        }
        if (refresh != null) {
            refresh.setVisible(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        refresh = menu.add(0, ID_REFRESH, 0, R.string.text_refresh)
//        refresh.setIcon(R.drawable.ic_refresh_48px)
//        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
//        refresh.setVisible(false)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (isMain) {
//                    noticeActivity(Config.Action.OPEN_DRAWER_LAYOUT)
//                } else {
//                    noticeActivity(Config.Action.FINISH)
//                }
//                return true;
//            case ID_REFRESH:
//                requestUpdate()
//                return true
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    private void requestDaily() {
        setTitle(getString(R.string.loading));
        setLoading();
        if (refresh != null)
            refresh.setVisible(false);
        requestData();
    }

    private void requestUpdate() {
        setTitle(getString(R.string.loading));
        setLoading();
        if (refresh != null)
            refresh.setVisible(false);
        DataManager.getInstance().update(provider, gankDaily -> {
            setUpData(gankDaily);
            Toast.makeText(getActivity(), R.string.success_update, Toast.LENGTH_SHORT).show();
        });
    }

    private void requestData() {
        DataManager.getInstance().get(provider, this::setUpData);
    }

    private void setLoading() {
        loading.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    private void setSuccess() {
        loading.setVisibility(View.INVISIBLE);
    }

    private void setError() {
        loading.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.GONE);
        empty.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
    }

    private void setEmpty() {
        loading.setVisibility(View.VISIBLE);
        loadingProgress.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
    }

    protected void setTitle(int year, int month, int day) {
        setTitle(String.format("%d%s%d%s%d%s", year, getString(R.string.year), month, getString(R.string.month), day, getString(R.string.day)));
    }

    public static DailyFragment newInstance(int year, int month, int day, boolean isMain) {
        DailyFragment fragment = new DailyFragment();
        Bundle bundle = new Bundle();
        if (year > 0 && month > 0 && day > 0) {
            bundle.putInt(Config.Static.YEAR, year);
            bundle.putInt(Config.Static.MONTH, month);
            bundle.putInt(Config.Static.DAY, day);
        }
        bundle.putBoolean(Config.Static.IS_MAIN, isMain);
        fragment.setArguments(bundle);
        return fragment;
    }
}