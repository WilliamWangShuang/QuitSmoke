package clientservice.entities;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class SurveyResultEntity implements Parcelable {
    private MeanEntity myMeanGroupEntity;
    private List<MeanEntity> meanEntityList;
    private List<ChanceAgeEntity> chanceAgeEntityList;
    private List<MotivationAgeEntity> motivationAgeEntityList;
    private List<MotivationGenderEntity> motivationGenderEntityList;

    public SurveyResultEntity() {
        myMeanGroupEntity = myMeanGroupEntity == null ? new MeanEntity() : myMeanGroupEntity;
        meanEntityList = (meanEntityList == null || meanEntityList.size() == 0) ? new ArrayList<MeanEntity>() : meanEntityList;
        chanceAgeEntityList = (chanceAgeEntityList == null || chanceAgeEntityList.size() == 0) ? new ArrayList<ChanceAgeEntity>() : chanceAgeEntityList;
        motivationAgeEntityList = (motivationAgeEntityList == null || motivationAgeEntityList.size() == 0) ? new ArrayList<MotivationAgeEntity>() : motivationAgeEntityList;
        motivationGenderEntityList = (motivationGenderEntityList == null || motivationGenderEntityList.size() == 0) ? new ArrayList<MotivationGenderEntity>() : motivationGenderEntityList;
    }

    private SurveyResultEntity(Parcel in) {
        // parcel self instance
        this.myMeanGroupEntity= (MeanEntity)in.readParcelable(MeanEntity.class.getClassLoader());
        this.meanEntityList = in.readArrayList(MeanEntity.class.getClassLoader());
        this.chanceAgeEntityList = in.readArrayList(MotivationAgeEntity.class.getClassLoader());
        this.motivationAgeEntityList = in.readArrayList(ChanceAgeEntity.class.getClassLoader());
        this.motivationGenderEntityList = in.readArrayList(MotivationGenderEntity.class.getClassLoader());
    }

    public MeanEntity getMyMeanGroupEntity() {
        return myMeanGroupEntity;
    }

    public List<MeanEntity> getMeanEntityList() {
        return meanEntityList;
    }

    public List<ChanceAgeEntity> getChanceAgeEntityList() {
        return chanceAgeEntityList;
    }

    public List<MotivationAgeEntity> getMotivationAgeEntityList() {
        return motivationAgeEntityList;
    }

    public List<MotivationGenderEntity> getMotivationGenderEntityList() {
        return motivationGenderEntityList;
    }

    // add element methods for lists
    public void setMyMean(int ageEnd, int ageStart, String gender, int meanConsume) {
        myMeanGroupEntity = new MeanEntity(ageEnd, ageStart, gender, meanConsume);
    }

    public void addMeanEntity(int ageEnd, int ageStart, String gender, int meanConsume) {
        MeanEntity entity = new MeanEntity(ageEnd, ageStart, gender, meanConsume);
        meanEntityList.add(entity);
    }

    public void addChanceAgeEntity(int ageEnd, int ageStart, String behaviour, double proportion) {
        ChanceAgeEntity entity = new ChanceAgeEntity(ageEnd, ageStart, behaviour, proportion);
        chanceAgeEntityList.add(entity);
    }

    public void addMotivationAgeEntity(int ageEnd, int ageStart, String behaviour, double proportion) {
        MotivationAgeEntity entity = new MotivationAgeEntity(ageEnd, ageStart, behaviour, proportion);
        motivationAgeEntityList.add(entity);
    }

    public void addMotivationGenderEntity(int rank, String gender, String behaviour, double proportion, String classification) {
        MotivationGenderEntity entity = new MotivationGenderEntity(rank, gender, behaviour, proportion, classification);
        motivationGenderEntityList.add(entity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(myMeanGroupEntity, flags);
        dest.writeList(meanEntityList);
        dest.writeList(chanceAgeEntityList);
        dest.writeList(motivationAgeEntityList);
        dest.writeList(motivationGenderEntityList);
    }

    public static final Creator<SurveyResultEntity> CREATOR = new Creator<SurveyResultEntity>() {
        @Override
        public SurveyResultEntity createFromParcel(Parcel in) {
            return new SurveyResultEntity(in); }
        @Override
        public SurveyResultEntity[] newArray(int size) {
            return new SurveyResultEntity[size]; }
    };
}
