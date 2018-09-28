package clientservice.entities;

import java.util.Date;

public class UserInfoEntity {
    private String name;
    private String email;
    private String password;
    private String partnerEmail;
    private boolean isPartner;
    private int point;
    private String registerDate;
    private boolean isSmoker;
    private String uid;
    private String age;
    private String gender;
    private String smokerNodeName;
    private String pricePerPack;

    public UserInfoEntity() {}

    public UserInfoEntity(String name, String age, String gender, String email, String password, Boolean isSmoker, Boolean isSupporter, String pricePerPack) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isSmoker = isSmoker;
        this.isPartner = isSupporter;
        this.age = age;
        this.gender = gender;
        this.pricePerPack = pricePerPack;
    }

    public UserInfoEntity(String name, String email, String password, String partnerEmail, boolean isPartner, int point, String registerDate, boolean isSmoker, String uid, String age, String pricePerPack) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.partnerEmail = partnerEmail;
        this.isPartner = isPartner;
        this.point = point;
        this.registerDate = registerDate;
        this.isSmoker = isSmoker;
        this.uid = uid;
        this.age = age;
        this.pricePerPack = pricePerPack;
    }

    public String getPricePerPack() {
        return pricePerPack;
    }

    public void setPricePerPack(String pricePerPack) {
        this.pricePerPack = pricePerPack;
    }

    public String getSmokerNodeName() {
        return smokerNodeName;
    }

    public void setSmokerNodeName(String smokerNodeName) {
        this.smokerNodeName = smokerNodeName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }

    public boolean isPartner() {
        return isPartner;
    }

    public void setPartner(boolean partner) {
        isPartner = partner;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isSmoker() {
        return isSmoker;
    }

    public void setSmoker(boolean smoker) {
        isSmoker = smoker;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
