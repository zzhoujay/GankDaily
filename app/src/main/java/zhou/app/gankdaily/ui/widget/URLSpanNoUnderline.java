package zhou.app.gankdaily.ui.widget;

import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import zhou.app.gankdaily.R;
import zhou.app.gankdaily.common.Config;

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }

    public URLSpanNoUnderline(Parcel src) {
        super(src);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
//        boolean open = Config.getBoolean(widget.getResources().getString(R.string.key_open), Config.Configurable.HANDLE_BY_ME);
//        if (open) {
//            Intent intent = new Intent(widget.getContext(), WebActivity.class)
//            intent.putExtra(Config.Static.URL, getURL())
//            widget.getContext().startActivity(intent)
//        } else {
//            super.onClick(widget)
//        }
    }
}