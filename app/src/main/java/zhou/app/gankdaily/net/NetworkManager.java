package zhou.app.gankdaily.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zhou.app.gankdaily.util.ErrorHandler;
import zhou.app.gankdaily.util.LogKit;

/**
 * Created by zhou on 15-10-20.
 * 网络请求管理器
 */
@SuppressWarnings("unused")
public final class NetworkManager {

    private static NetworkManager networkManager;

    @SuppressWarnings("unused")
    public static NetworkManager getInstance() {
        return networkManager;
    }

    @SuppressWarnings("unused")
    public static void init(Context context, Gson gson) {
        networkManager = new NetworkManager(context, gson);
    }

    private OkHttpClient client;
    private Gson gson;
    private Context context;
    private ErrorHandler errorHandler;

    private NetworkManager(Context context, Gson gson) {
        client = new OkHttpClient();
        client.setConnectTimeout(5, TimeUnit.SECONDS);
        client.setReadTimeout(5, TimeUnit.SECONDS);
        client.setWriteTimeout(5, TimeUnit.SECONDS);
        this.gson = gson;
        this.context = context;
    }

    @SuppressWarnings("unused")
    public void requestString(Request r, Action1<String> action1) {
        Observable
                .create((Subscriber<? super String> subscriber) -> {
                    try {
                        subscriber.onNext(client.newCall(r).execute().body().string());
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwable -> {
                    LogKit.d("request error", throwable);
                    if (errorHandler != null) {
                        action1.call(errorHandler.handleError(throwable).toString());
                    }
                });
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T extends ErrorHandler> void request(Request r, Action1<T> action1, Class<T> aClass) {
        Observable
                .create((Subscriber<? super T> subscriber) -> {
                    try {
                        String body = client.newCall(r).execute().body().string();
                        LogKit.d("response success", body);
                        subscriber.onNext(gson.fromJson(body, aClass));
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwable -> {
                    LogKit.d("request", r.urlString(), throwable);
                    if (errorHandler != null) {
                        action1.call((T) errorHandler.handleError(throwable));
                    }
                });
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T extends ErrorHandler> void request(Request r, Action1<T> action1, Type type) {
        Observable
                .create((Subscriber<? super T> subscriber) -> {
                    try {
                        String body = client.newCall(r).execute().body().string();
                        LogKit.d("request success", body);
                        subscriber.onNext((T) gson.fromJson(body, type));
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwable -> {
                    LogKit.d("request", r.urlString(), throwable);
                    if (errorHandler != null) {
                        action1.call((T) errorHandler.handleError(throwable));
                    }
                });
    }

    @SuppressWarnings("unused")
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    @SuppressWarnings("unused")
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
