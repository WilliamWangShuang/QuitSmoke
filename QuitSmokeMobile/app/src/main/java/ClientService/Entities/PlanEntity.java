package clientservice.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SlidingPaneLayout;

public class PlanEntity implements Parcelable {
    private String uid;
    private int targetAmount;
    private String createDate;
    private String status;
    private int realAmount;

    public PlanEntity() {}

    private PlanEntity(Parcel in) {
        uid = in.readString();
        targetAmount = in.readInt();
        createDate = in.readString();
        status = in.readString();
        realAmount = in.readInt();
    }

    public PlanEntity(String uid, int targetAmount, String createDate, String status) {
        this.uid = uid;
        this.targetAmount = targetAmount;
        this.createDate = createDate;
        this.status = status;
    }

    public int getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(int realAmount) {
        this.realAmount = realAmount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeInt(targetAmount);
        out.writeString(createDate);
        out.writeString(status);
        out.writeInt(realAmount);
    }

    public static final Creator<PlanEntity> CREATOR = new Creator<PlanEntity>() {
        @Override
        public PlanEntity createFromParcel(Parcel in) {
            return new PlanEntity(in); }
        @Override
        public PlanEntity[] newArray(int size) {
            return new PlanEntity[size]; }
    };
}
