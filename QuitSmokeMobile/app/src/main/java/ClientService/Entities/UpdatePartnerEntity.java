package ClientService.Entities;

public class UpdatePartnerEntity {
    private String smokerNodeName;
    private String partnerEmail;

    public UpdatePartnerEntity() {}

    public UpdatePartnerEntity(String smokerNodeName, String partnerEmail) {
        this.smokerNodeName = smokerNodeName;
        this.partnerEmail = partnerEmail;
    }

    public String getSmokerNodeName() {
        return smokerNodeName;
    }

    public void setSmokerNodeName(String smokerNodeName) {
        this.smokerNodeName = smokerNodeName;
    }

    public String getPartnerEmail() {
        return partnerEmail;
    }

    public void setPartnerEmail(String partnerEmail) {
        this.partnerEmail = partnerEmail;
    }
}
