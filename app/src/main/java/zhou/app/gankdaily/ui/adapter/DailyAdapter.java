package zhou.app.gankdaily.ui.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rx.functions.Action2;
import zhou.app.gankdaily.App;
import zhou.app.gankdaily.R;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.util.TextKit;

public class DailyAdapter extends BaseAdapter<DailyAdapter.Holder> {

    private GankDaily daily;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, null));
        return holder;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(daily.getType(position));
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = App.getApp().getResources().getColor(R.color.colorPrimary, App.getApp().getTheme());
        } else {
            color = App.getApp().getColor(R.color.colorPrimary);
        }
        holder.content.setText(TextKit.generate(daily.getGank(position), color));
    }

    @Override
    public int getItemCount() {
        return daily == null ? 0 : daily.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView title, content;

        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);

            content.setMovementMethod(LinkMovementMethod.getInstance());

        }
    }

    public void setDaily(GankDaily daily) {
        this.daily = daily;
        notifyDataSetChanged();
    }

    public void clearData() {
        this.daily = null;
        notifyDataSetChanged();
    }
}