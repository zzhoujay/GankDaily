package zhou.app.gankdaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(daily.getType(position));
        holder.content.setText(TextKit.generate(daily.getGank(position), App.getApp().getResources().getColor(R.color.material_lightBlue_500, App.getApp().getTheme())));
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

//            if (itemView instanceof CardView) {
//                def card = itemView as CardView
//                if (App.themeIsLight()) {
//                    card.setCardBackgroundColor(App.getInstance().getCardLight())
//                    title.setTextColor(App.getInstance().getTextLight())
//                } else {
//                    card.setCardBackgroundColor(App.getInstance().getCardDark())
//                    title.setTextColor(App.getInstance().getTextDark())
//                }
//            }

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