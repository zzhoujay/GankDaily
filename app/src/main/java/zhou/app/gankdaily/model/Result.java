package zhou.app.gankdaily.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhou on 15-12-23.
 */
public class Result extends BaseModel implements Parcelable {

    public GankDaily results;
    public boolean error;

    public Result(GankDaily results) {
        this.results = results;
    }

    public Result(boolean error, GankDaily results) {
        this.results = results;
        this.error = error;
    }

    public boolean isSuccess() {
        return !error;
    }

    public Result() {
    }

    public static Result errorHandle(Throwable e) {
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.results, 0);
        dest.writeByte(error ? (byte) 1 : (byte) 0);
    }

    protected Result(Parcel in) {
        this.results = in.readParcelable(GankDaily.class.getClassLoader());
        this.error = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public String toString() {
        return "Result{" +
                "results=" + results +
                ", error=" + error +
                '}';
    }
}
