package zhou.app.gankdaily.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import rx.functions.Action1;

public class BaseFragment extends Fragment {

    Action1<Integer> notifier;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Action1) {
            notifier = (Action1<Integer>) activity;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Action1) {
            notifier = (Action1<Integer>) context;
        }
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            appCompatActivity.setSupportActionBar(toolbar);
        }
    }

    protected ActionBar getSupportActionBar() {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            return appCompatActivity.getSupportActionBar();
        }
        return null;
    }

    protected void noticeActivity(int noticeId) {
        if (notifier != null) {
            notifier.call(noticeId);
        }
    }

    protected void initView(View v) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21)
            getActivity().getWindow().setStatusBarColor(color);
    }

    protected void setToolbarColor(int color){

    }

    protected void setTitle(String title) {
        getActivity().setTitle(title);
    }

    protected void setTitle(int res) {
        getActivity().setTitle(res);
    }

    boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}