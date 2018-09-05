package clientservice.entities;

public class CalculateFrsEntity {
    private int age;
    private String gender;
    private int chol;
    private int hdl;
    private int sbp;
    private boolean isTreated;

    public CalculateFrsEntity() {}

    public CalculateFrsEntity(int age, String gender, int chol, int hdl, int sbp, boolean isTreated) {
        this.age = age;
        this.gender = gender;
        this.chol = chol;
        this.hdl = hdl;
        this.sbp = sbp;
        this.isTreated = isTreated;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getChol() {
        return chol;
    }

    public void setChol(int chol) {
        this.chol = chol;
    }

    public int getHdl() {
        return hdl;
    }

    public void setHdl(int hdl) {
        this.hdl = hdl;
    }

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public boolean getIsTreated() {
        return isTreated;
    }

    public void setIsTreated(boolean treated) {
        isTreated = treated;
    }
}
