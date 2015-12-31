package zhou.app.gankdaily.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 15-12-23.
 */
public class GankDaily extends BaseModel implements Parcelable {

    public static final String GANK_DAILY = "gank_daily";

    public List<String> types;
    public List<List<Gank>> ganks;

    public GankDaily(List<String> types, List<List<Gank>> ganks) {
        this.types = types;
        this.ganks = ganks;
    }

    public GankDaily() {
    }

    public String getType(int position) {
        return types == null ? null : types.get(position);
    }

    public List<Gank> getGank(int position) {
        return ganks == null ? null : ganks.get(position);
    }

    public int size() {
        return types == null ? 0 : types.size();
    }


    @Override
    public String toString() {
        return "GankDaily{" +
                "types=" + types +
                ", ganks=" + ganks +
                '}';
    }

    public boolean isEmpty() {
        return types == null || ganks == null || types.isEmpty() || ganks.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.types);
        dest.writeList(this.ganks);
    }

    protected GankDaily(Parcel in) {
        this.types = in.createStringArrayList();
        this.ganks = new ArrayList<List<Gank>>();
        in.readList(this.ganks, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<GankDaily> CREATOR = new Parcelable.Creator<GankDaily>() {
        public GankDaily createFromParcel(Parcel source) {
            return new GankDaily(source);
        }

        public GankDaily[] newArray(int size) {
            return new GankDaily[size];
        }
    };
}
