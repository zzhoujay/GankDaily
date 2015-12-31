package zhou.app.gankdaily.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;

import rx.functions.Action1;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
import zhou.app.gankdaily.data.DailyProvider;
import zhou.app.gankdaily.ui.fragment.DailyFragment;
import zhou.app.gankdaily.util.TimeKit;

public class DailyActivity extends AppCompatActivity implements Action1<Integer> {

    private CoordinatorLayout coordinatorLayout;
    private DailyFragment currFragment;
    private int year, month, day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat);

        Intent i = getIntent();

        DailyFragment dailyFragment;

        if (!i.hasExtra(DailyProvider.DAILY_PROVIDER)) {
            int[] times = TimeKit.getTime();
            year = i.getIntExtra(Config.Static.YEAR, times[0]);
            month = i.getIntExtra(Config.Static.MONTH, times[1]);
            day = i.getIntExtra(Config.Static.DAY, times[2]);

            dailyFragment = DailyFragment.newInstance(year, month, day, true);
        } else {
            DailyProvider dailyProvider = i.getParcelableExtra(DailyProvider.DAILY_PROVIDER);
            dailyFragment = DailyFragment.newInstance(dailyProvider, true);
        }

        add(dailyFragment);


    }

    private void add(DailyFragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        this.currFragment = f;
    }

    private void replace(DailyFragment f) {
        if (currFragment == f) {
            return;
        }
        coordinatorLayout.removeAllViews();
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit();
        this.currFragment = f;
    }


    @Override
    public void call(Integer integer) {
        switch (integer) {
            case Config.Action.FINISH:
                finish();
                break;
            case Config.Action.CHANGE_DATE:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
                    currFragment.resetDate(year1, month, dayOfMonth);
                }, year, month - 1, day);
                datePickerDialog.show();
                break;
        }
    }

}