package clientservice.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

public class NoSmokePlace implements Parcelable {
    private String type;
    private List<NoSmokeItem> list;

    public NoSmokePlace() {}

    public NoSmokePlace(String type, List<NoSmokeItem> list) {
        this.type = type;
        this.list = list;
    }

    private NoSmokePlace(Parcel in) {
        // parcel self instance
        this.type= in.readString();
        this.list = in.readArrayList(NoSmokeItem.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeList(list);
    }

    public static final Creator<NoSmokePlace> CREATOR = new Creator<NoSmokePlace>() {
        @Override
        public NoSmokePlace createFromParcel(Parcel in) {
            return new NoSmokePlace(in); }
        @Override
        public NoSmokePlace[] newArray(int size) {
            return new NoSmokePlace[size]; }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NoSmokeItem> getList() {
        return list;
    }

    public void setList(List<NoSmokeItem> list) {
        this.list = list;
    }
}
