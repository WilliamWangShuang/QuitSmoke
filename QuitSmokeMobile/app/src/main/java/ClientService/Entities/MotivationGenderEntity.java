package ClientService.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class MotivationGenderEntity implements Parcelable {
    private int rank;
    private String gender;
    private String behaviour;
    private double proportion;
    private String classification;

    public MotivationGenderEntity() {}

    private MotivationGenderEntity(Parcel in) {
        rank = in.readInt();
        gender = in.readString();
        behaviour = in.readString();
        proportion = in.readDouble();
        classification = in.readString();
    }

    public MotivationGenderEntity(int rank, String gender, String behaviour, double proportion, String classification) {
        this.rank = rank;
        this.gender = gender;
        this.behaviour = behaviour;
        this.proportion = proportion;
        this.classification = classification;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(rank);
        out.writeString(gender);
        out.writeString(behaviour);
        out.writeDouble(proportion);
        out.writeString(classification);
    }

    public static final Creator<MotivationGenderEntity> CREATOR = new Creator<MotivationGenderEntity>() {
        @Override
        public MotivationGenderEntity createFromParcel(Parcel in) {
            return new MotivationGenderEntity(in); }
        @Override
        public MotivationGenderEntity[] newArray(int size) {
            return new MotivationGenderEntity[size]; }
    };
}
