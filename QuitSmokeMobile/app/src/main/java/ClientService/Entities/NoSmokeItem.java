package clientservice.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class NoSmokeItem implements Parcelable {
    private String address;
    private double latitude;
    private double longitude;
    private String name;
    private String type;

    public NoSmokeItem() {}

    public NoSmokeItem(String address, double latitude, double longitude, String name, String type) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.type = type;
    }

    private NoSmokeItem(Parcel in) {
        // parcel self instance
        this.address= in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.name = in.readString();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeString(type);
    }

    public static final Creator<NoSmokeItem> CREATOR = new Creator<NoSmokeItem>() {
        @Override
        public NoSmokeItem createFromParcel(Parcel in) {
            return new NoSmokeItem(in); }
        @Override
        public NoSmokeItem[] newArray(int size) {
            return new NoSmokeItem[size]; }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
