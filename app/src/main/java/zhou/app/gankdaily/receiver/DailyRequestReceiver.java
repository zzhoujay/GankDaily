package zhou.app.gankdaily.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import zhou.app.gankdaily.App;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.data.DailyProvider;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.ui.activity.DailyActivity;
import zhou.app.gankdaily.util.LogKit;
import zhou.app.gankdaily.util.TimeKit;

/**
 * Created by zhou on 15-12-31.
 */
public class DailyRequestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        requestDaily(context);
    }

    private void requestDaily(Context context) {
        if (App.last_request_time > 0 && System.currentTimeMillis() - App.last_request_time < Config.Configurable.MIN_TIME) {
            App.last_request_time = System.currentTimeMillis();
            return;
        }
        App.last_request_time = System.currentTimeMillis();
        int[] times = TimeKit.getTime();
        DailyProvider dailyProvider = new DailyProvider(times[0], times[1], times[2]);
        dailyProvider.loadByCache(gankDaily -> {
            if (gankDaily == null) {
                dailyProvider.load(gankDaily1 -> {
                    if (gankDaily1 != null) {
                        showNotification(context, dailyProvider);
                    } else {
                        LogKit.d("DailyRequestService", "load gank daily from network failure");
                    }
                }, false);
            } else {
                LogKit.d("DailyRequestService", "the gank daily cache exist");
            }
        });
    }

    private void showNotification(Context context, DailyProvider dailyProvider) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(context.getString(R.string.notice_title));
        builder.setContentText(context.getString(R.string.notice_text));
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        Intent intent = new Intent(context, DailyActivity.class);
        intent.putExtra(GankDaily.GANK_DAILY, dailyProvider);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(R.layout.activity_main, notification);
    }

}
