package zhou.app.gankdaily;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import zhou.app.gankdaily.net.NetworkManager;
import zhou.app.gankdaily.receiver.DailyRequestReceiver;

/**
 * Created by zhou on 15-12-23.
 */
public class App extends Application {

    public static final String SITE_URL = "http://gank.avosapps.com";
    public static final String TYPE_URL = SITE_URL + "/api/data";
    public static final String TIME_URL = SITE_URL + "/api/day";
    public static final String RANDOM_URL = SITE_URL + "/api/random/data";

    private static App app;

    public static long last_request_time = -1;

    public Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        NetworkManager.init(this, gson);

        setNotice();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);

        setAlarm(Collections.singletonList(calendar));
    }


    public static App getApp() {
        return app;
    }

    public static File getCacheFile() {
        return app.getCacheDir();
    }

    public static void toast(int res) {
        Toast.makeText(app, res, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg) {
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    private void setNotice() {
        List<Integer> time = Arrays.asList(8, 10, 12, 14, 16);
        List<Calendar> calendars = Stream.of(time).map(value -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, value);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar;
        }).collect(Collectors.toList());
        setAlarm(calendars);
    }

    private void setAlarm(List<Calendar> calendars) {
        if (calendars == null || calendars.isEmpty())
            return;
        AlarmManager alarmManager = (AlarmManager) app.getSystemService(ALARM_SERVICE);
        for (Calendar calendar : calendars) {
            long time = calendar.getTimeInMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(app, (int) time, new Intent(app, DailyRequestReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

}
