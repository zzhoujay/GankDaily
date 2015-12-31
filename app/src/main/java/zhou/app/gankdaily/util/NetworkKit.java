package zhou.app.gankdaily.util;

import com.squareup.okhttp.Request;

import rx.functions.Action1;
import zhou.app.gankdaily.App;
import zhou.app.gankdaily.model.Result;
import zhou.app.gankdaily.net.NetworkManager;

/**
 * Created by zhou on 15-12-23.
 */
public class NetworkKit {

    public static void daily(int year, int month, int day, Action1<Result> closure) {
        Request request = new Request.Builder().get().url(String.format("%s/%d/%d/%d", App.TIME_URL, year, month, day)).build();
        NetworkManager.getInstance().requestString(request, result -> {
            try {
                closure.call(JsonKit.generate(result, App.getApp().gson));
            } catch (Exception e) {
                LogKit.d("JsonKit", "error", e);
                closure.call(Result.errorHandle(e));
            }
        });
    }

}
