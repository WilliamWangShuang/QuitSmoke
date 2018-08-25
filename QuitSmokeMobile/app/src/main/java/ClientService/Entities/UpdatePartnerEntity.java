package ClientService.Entities;

public class UpdatePartnerEntity {
    private String smokerUID;
    private String partnerEmail;

    public UpdatePartnerEntity() {}

    public UpdatePartnerEntity(String smokerUID, String partnerEmail) {
        this.smokerUID = smokerUID;
        this.partnerEmail = partnerEmail;
    }

    public String getSmokerUID() {
        return smokerUID;
    }

    public void setSmokerUID(String smokerUID) {
        this.smokerUID = smokerUID;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }
}
