package ClientService.Entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class ChanceAgeEntity implements Comparable, Parcelable {
    private int ageEnd;
    private int ageStart;
    private String behaviour;
    private double proportion;

    public ChanceAgeEntity() {}

    private ChanceAgeEntity(Parcel in) {
        ageEnd = in.readInt();
        ageStart = in.readInt();
        behaviour = in.readString();
        proportion = in.readDouble();
    }

    public ChanceAgeEntity(int ageEnd, int ageStart, String behaviour, double proportion) {
        this.ageEnd = ageEnd;
        this.ageStart = ageStart;
        this.behaviour = behaviour;
        this.proportion = proportion;
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

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ChanceAgeEntity compare = (ChanceAgeEntity)o;

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
        out.writeString(behaviour);
        out.writeDouble(proportion);
    }

    public static final Creator<ChanceAgeEntity> CREATOR = new Creator<ChanceAgeEntity>() {
        @Override
        public ChanceAgeEntity createFromParcel(Parcel in) {
            return new ChanceAgeEntity(in); }
        @Override
        public ChanceAgeEntity[] newArray(int size) {
            return new ChanceAgeEntity[size]; }
    };
}