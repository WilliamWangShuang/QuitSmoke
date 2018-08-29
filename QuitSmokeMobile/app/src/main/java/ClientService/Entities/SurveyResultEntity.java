package ClientService.Entities;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SurveyResultEntity {
    private static MeanEntity myMeanGroupEntity;
    private static List<MeanEntity> meanEntityList;
    private static List<ChanceAgeEntity> chanceAgeEntityList;
    private static List<MotivationAgeEntity> motivationAgeEntityList;
    private static List<MotivationGenderEntity> motivationGenderEntityList;

    public SurveyResultEntity() {
        myMeanGroupEntity = myMeanGroupEntity == null ? new MeanEntity() : myMeanGroupEntity;
        meanEntityList = (meanEntityList == null || meanEntityList.size() == 0) ? new ArrayList<MeanEntity>() : meanEntityList;
        chanceAgeEntityList = (chanceAgeEntityList == null || chanceAgeEntityList.size() == 0) ? new ArrayList<ChanceAgeEntity>() : chanceAgeEntityList;
        motivationAgeEntityList = (motivationAgeEntityList == null || motivationAgeEntityList.size() == 0) ? new ArrayList<MotivationAgeEntity>() : motivationAgeEntityList;
        motivationGenderEntityList = (motivationGenderEntityList == null || motivationGenderEntityList.size() == 0) ? new ArrayList<MotivationGenderEntity>() : motivationGenderEntityList;
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

    // class level getters and setters
    public int getMyMeanAgeEnd() {
        return myMeanGroupEntity.getAgeEnd();
    }

    public int getMyMeanAgeStart() {
        return myMeanGroupEntity.getAgeStart();
    }

    public String getMyMeanGender() {
        return myMeanGroupEntity.getGender();
    }

    public int getMyMeanConsume() {
        return myMeanGroupEntity.getMeanConsume();
    }

    // mean entity
    public class MeanEntity implements Comparable {
        private int ageEnd;
        private int ageStart;
        private String gender;
        private int meanConsume;

        public MeanEntity() {
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
    }
    // chance age entity
    public class ChanceAgeEntity implements Comparable {
        private int ageEnd;
        private int ageStart;
        private String behaviour;
        private double proportion;

        public ChanceAgeEntity() {
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
    }
    // motivation age entity
    public class MotivationAgeEntity implements Comparable{
        private int ageEnd;
        private int ageStart;
        private String behaviour;
        private double proportion;

        public MotivationAgeEntity() {
        }

        public MotivationAgeEntity(int ageEnd, int ageStart, String behaviour, double proportion) {
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
            MotivationAgeEntity compare = (MotivationAgeEntity)o;

            if (getAgeStart() > compare.getAgeEnd())
                return 1;
            else if (getAgeEnd() < compare.getAgeStart())
                return -1;
            else
                return 0;
        }
    }

    public class MotivationGenderEntity {
        private int rank;
        private String gender;
        private String behaviour;
        private double proportion;
        private String classification;

        public MotivationGenderEntity() {
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
    }
}
