package zhou.app.gankdaily.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by zhou on 15-12-23.
 */
public class Gank extends BaseModel implements Parcelable {

    public String who;
    public String desc;
    public String type;
    public String url;
    public String objectId;
    public boolean used;
    public Date publishedAt;
    public Date createdAt;
    public Date updatedAt;

    public Gank(String who, String desc, String type, String url, String objectId, boolean used, Date publishedAt, Date createdAt, Date updatedAt) {
        this.who = who;
        this.desc = desc;
        this.type = type;
        this.url = url;
        this.objectId = objectId;
        this.used = used;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Gank() {
    }

    @Override
    public String toString() {
        return "Gank{" +
                "who='" + who + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", objectId='" + objectId + '\'' +
                ", used=" + used +
                ", publishedAt=" + publishedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.who);
        dest.writeString(this.desc);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.objectId);
        dest.writeByte(used ? (byte) 1 : (byte) 0);
        dest.writeLong(publishedAt != null ? publishedAt.getTime() : -1);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    protected Gank(Parcel in) {
        this.who = in.readString();
        this.desc = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.objectId = in.readString();
        this.used = in.readByte() != 0;
        long tmpPublishedAt = in.readLong();
        this.publishedAt = tmpPublishedAt == -1 ? null : new Date(tmpPublishedAt);
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Parcelable.Creator<Gank> CREATOR = new Parcelable.Creator<Gank>() {
        public Gank createFromParcel(Parcel source) {
            return new Gank(source);
        }

        public Gank[] newArray(int size) {
            return new Gank[size];
        }
    };
}
