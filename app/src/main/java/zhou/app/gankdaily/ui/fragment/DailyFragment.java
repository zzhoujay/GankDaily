package zhou.app.gankdaily.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;

import java.util.List;

import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.data.DailyProvider;
import zhou.app.gankdaily.data.DataManager;
import zhou.app.gankdaily.model.Gank;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.ui.adapter.DailyAdapter;
import zhou.app.gankdaily.util.TimeKit;

public class DailyFragment extends BaseFragment {

    public static final int ID_REFRESH = 0x78901;
    private static final int ID_SETTING = 0x34542;

    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DailyProvider provider;
    DailyAdapter daliyAdapter;
    FloatingActionButton fab;
    View loading, loadingProgress, empty, error;
    boolean isMain = false;
    int year, month, day;
    int count;
    MenuItem refresh;
    ImageView icon;


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
            if (b.containsKey(DailyProvider.DAILY_PROVIDER)) {
                provider = b.getParcelable(DailyProvider.DAILY_PROVIDER);
                if (provider != null) {
                    year = provider.getYear();
                    month = provider.getMonth();
                    day = provider.getDay();
                }
            } else {
                year = b.getInt(Config.Static.YEAR, year);
                month = b.getInt(Config.Static.MONTH, month);
                day = b.getInt(Config.Static.DAY, day);
                provider = new DailyProvider(year, month, day);
            }
            isMain = b.getBoolean(Config.Static.IS_MAIN, false);
        }

        provider.setNoticeable(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        initView(view);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (isMain)
                ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle(R.string.app_name);

        daliyAdapter = new DailyAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(daliyAdapter);

        fab.setOnClickListener(v -> noticeActivity(Config.Action.CHANGE_DATE));

        error.setOnClickListener(v -> requestDaily());

        requestDaily();

        return view;
    }

    @Override
    protected void initView(View v) {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        loading = v.findViewById(R.id.loading);
        loadingProgress = v.findViewById(R.id.progressBar);
        empty = v.findViewById(R.id.no_data);
        error = v.findViewById(R.id.error);
        icon = (ImageView) v.findViewById(R.id.icon);
    }

    /**
     * 重新设置日期
     *
     * @param year  年份
     * @param month 月份
     * @param day   日期
     */
    public void resetDate(int year, int month, int day) {
        this.provider = new DailyProvider(year, month, day);
        requestDaily();
    }

    /**
     * 设置数据
     *
     * @param daily 数据
     */
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
                            DataManager.update(provider, this::setUpData);
                            count++;
                        } else {
                            count++;
                            provider = provider.getPrevDay();
                            if (TimeKit.future(provider.getYear(), provider.getMonth(), provider.getDay())) {
                                //如如果要加载的数据是今天或以后
                                DataManager.update(provider, this::setUpData);
                            } else {
                                DataManager.get(provider, this::setUpData);
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

                List<String> urls = Stream.of(welfares).map(value -> value.url).collect(Collectors.toList());

                Glide.with(getActivity()).load(urls.get(0)).into(icon);

                daliyAdapter.setDaily(daily);

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
        refresh = menu.add(0, ID_REFRESH, 0, R.string.text_refresh);
        refresh.setIcon(R.mipmap.ic_refresh);
        refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        refresh.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ID_REFRESH:
                requestUpdate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    /**
     * 请求每天的干货
     */
    private void requestDaily() {
        setTitle(getString(R.string.loading));
        setLoading();
        if (refresh != null)
            refresh.setVisible(false);
        requestData();
    }

    /**
     * 请求更新
     */
    private void requestUpdate() {
        setTitle(getString(R.string.loading));
        setLoading();
        if (refresh != null)
            refresh.setVisible(false);
        DataManager.update(provider, gankDaily -> {
            setUpData(gankDaily);
            Toast.makeText(getActivity(), R.string.success_update, Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        DataManager.get(provider, this::setUpData);
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
        setTitle(String.format("%d%s%d%s%d%s", provider.getYear(), getString(R.string.year), provider.getMonth(), getString(R.string.month), provider.getDay(), getString(R.string.day)));
    }

    /**
     * 创建一个实例
     *
     * @param year   年份
     * @param month  月份
     * @param day    日期
     * @param isMain 是不是今天的
     * @return 实例
     */
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

    public static DailyFragment newInstance(DailyProvider dailyProvider, boolean isMain) {
        DailyFragment dailyFragment = new DailyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DailyProvider.DAILY_PROVIDER, dailyProvider);
        bundle.putBoolean(Config.Static.IS_MAIN, isMain);
        dailyFragment.setArguments(bundle);
        return dailyFragment;
    }
}