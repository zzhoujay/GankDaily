package zhou.app.gankdaily.ui.adapter;

import android.support.v7.widget.RecyclerView;

import rx.functions.Action2;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    Action2 clickListener, longClickListener;


    void setClickListener(Action2 clickListener) {
        this.clickListener = clickListener;
    }

    void setLongClickListener(Action2 longClickListener) {
        this.longClickListener = longClickListener;
    }

    void removeItem(int position) {

    }
}