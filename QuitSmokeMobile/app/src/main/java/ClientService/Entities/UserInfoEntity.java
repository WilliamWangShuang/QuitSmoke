package ClientService.Entities;

import java.util.Date;

public class UserInfoEntity {
    private String name;
    private String email;
    private String password;
    private String city;
    private String partnerId;
    private boolean isPartner;
    private int point;
    private String suburb;
    private String registerDate;
    private boolean isSmoker;
    private String planId;

    public UserInfoEntity() {}

    public UserInfoEntity(String name, String email, String password, String city, String suburb, Boolean isSmoker) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isSmoker = isSmoker;
        this.city = city;
        this.suburb = suburb;
    }

    public UserInfoEntity(String name, String email, String password, String city, String partnerId, boolean isPartner, int point, String suburb, String registerDate, boolean isSmoker) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.partnerId = partnerId;
        this.isPartner = isPartner;
        this.point = point;
        this.suburb = suburb;
        this.registerDate = registerDate;
        this.isSmoker = isSmoker;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
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

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
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

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
