package ClientService.Entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class MeanEntity implements Comparable, Parcelable {
    private int ageEnd;
    private int ageStart;
    private String gender;
    private int meanConsume;

    public MeanEntity() {}

    private MeanEntity(Parcel in) {
        ageEnd = in.readInt();
        ageStart = in.readInt();
        gender = in.readString();
        meanConsume = in.readInt();
    }

    public MeanEntity(int ageEnd, int ageStart, String gender, int meanConsume) {
        this.ageEnd = ageEnd;
        this.ageStart = ageStart;
        this.gender = gender;
        this.meanConsume = meanConsume;
    }

    public int getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(int ageEnd) {
        this.ageEnd = ageEnd;
    }

    public int getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(int ageStart) {
        this.ageStart = ageStart;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMeanConsume() {
        return meanConsume;
    }

    public void setMeanConsume(int meanConsume) {
        this.meanConsume = meanConsume;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        MeanEntity compare = (MeanEntity)o;

        if (getAgeStart() > compare.getAgeEnd())
            return 1;
        else if (getAgeEnd() < compare.getAgeStart())
            return -1;
        else
            return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ageEnd);
        out.writeInt(ageStart);
        out.writeString(gender);
        out.writeInt(meanConsume);
    }

    public static final Creator<MeanEntity> CREATOR = new Creator<MeanEntity>() {
        @Override
        public MeanEntity createFromParcel(Parcel in) {
            return new MeanEntity(in); }
        @Override
        public MeanEntity[] newArray(int size) {
            return new MeanEntity[size]; }
    };
}
