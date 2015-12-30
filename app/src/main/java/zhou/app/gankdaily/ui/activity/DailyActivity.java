package zhou.app.gankdaily.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import rx.functions.Action1;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;
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
        int[] times = TimeKit.getTime();
        year = i.getIntExtra(Config.Static.YEAR, times[0]);
        month = i.getIntExtra(Config.Static.MONTH, times[1]);
        day = i.getIntExtra(Config.Static.DAY, times[2]);

        add(DailyFragment.newInstance(year, month, day, true));
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
//                    Intent i = new Intent(this, DailyActivity.class);
//                    i.putExtra(Config.Static.YEAR, year);
//                    i.putExtra(Config.Static.MONTH, month + 1);
//                    i.putExtra(Config.Static.DAY, day);
//                    startActivity(i);
                    currFragment.pushDate(year1, month, dayOfMonth);
                }, year, month - 1, day);
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !currFragment.isStackEmpty()) {
            currFragment.popDate();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}