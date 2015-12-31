package zhou.app.gankdaily.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Calendar;

import rx.functions.Action1;
import zhou.app.gankdaily.App;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.util.HashKit;
import zhou.app.gankdaily.util.LogKit;
import zhou.app.gankdaily.util.NetworkKit;

/**
 * Created by zhou on 15-12-23.
 */
public class DailyProvider extends CommonProvider<GankDaily> implements Parcelable {

    public static final String DAILY_PROVIDER = "daily_provider";

    private int year, month, day;
    private boolean needCache;

    public DailyProvider(int year, int month, int day) {
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
        NetworkKit.daily(year, month, day, result -> {
            GankDaily gankDaily = null;
            if (result != null) {
                if (result.isSuccess()) {
                    gankDaily = result.results;
                    LogKit.d("gg", result);
                } else {
                    App.toast(R.string.failure_load_data);
                }
            } else {
                App.toast(R.string.error_unknown);
            }
            action1.call(gankDaily);
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

    public DailyProvider getNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new DailyProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public DailyProvider getPrevDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new DailyProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeByte(needCache ? (byte) 1 : (byte) 0);
    }

    protected DailyProvider(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.needCache = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DailyProvider> CREATOR = new Parcelable.Creator<DailyProvider>() {
        public DailyProvider createFromParcel(Parcel source) {
            return new DailyProvider(source);
        }

        public DailyProvider[] newArray(int size) {
            return new DailyProvider[size];
        }
    };
}
