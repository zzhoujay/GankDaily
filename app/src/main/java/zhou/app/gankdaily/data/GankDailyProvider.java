package zhou.app.gankdaily.data;

import java.io.File;
import java.util.Calendar;

import rx.functions.Action1;
import zhou.app.gankdaily.App;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.util.HashKit;
import zhou.app.gankdaily.util.NetworkKit;

/**
 * Created by zhou on 15-12-23.
 */
public class GankDailyProvider extends CommonProvider<GankDaily> {

    private int year, month, day;
    private boolean needCache;

    public GankDailyProvider(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        key = HashKit.md5(String.format("year:%d,month:%d,day:%d-cache", year, month, day));
        file = new File(App.getCacheFile(), key);

        Calendar now = Calendar.getInstance();
        int y = now.get(Calendar.YEAR);
        if (year < y) {
            needCache = true;
        } else if (year == y) {
            int m = now.get(Calendar.MONTH) + 1;
            if (month < m) {
                needCache = true;
            } else if (month == m) {
                int d = now.get(Calendar.DAY_OF_MONTH);
                needCache = day <= d;
            } else {
                needCache = false;
            }
        } else {
            needCache = false;
        }
    }

    @Override
    protected void loadByNetwork(Action1<GankDaily> action1, boolean more) {
        NetworkKit.time(year, month, day, result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    t = result.results;
                } else {
                    App.toast(R.string.failure_load_data);
                }
            } else {
                App.toast(R.string.error_unknown);
            }
        });
    }

    @Override
    protected void onNoNetwork() {
        App.toast(R.string.error_no_network);
    }

    @Override
    public boolean needCache() {
        return needCache;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public GankDailyProvider getNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new GankDailyProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public GankDailyProvider getPrevDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new GankDailyProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}
