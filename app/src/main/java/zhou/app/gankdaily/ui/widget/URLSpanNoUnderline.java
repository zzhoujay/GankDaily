package zhou.app.gankdaily.ui.widget;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String url) {
        super(url);
    }

    @SuppressWarnings("unused")
    public URLSpanNoUnderline(Parcel src) {
        super(src);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

}