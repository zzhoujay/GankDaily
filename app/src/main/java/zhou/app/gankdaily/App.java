package zhou.app.gankdaily;

import android.app.Application;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import zhou.app.gankdaily.net.NetworkManager;

/**
 * Created by zhou on 15-12-23.
 */
public class App extends Application {

    public static final String SITE_URL = "http://gank.avosapps.com";
    public static final String TYPE_URL = SITE_URL + "/api/data";
    public static final String TIME_URL = SITE_URL + "/api/day";
    public static final String RANDOM_URL = SITE_URL + "/api/random/data";

    private static App app;

    public Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        NetworkManager.init(this, gson);
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
}
