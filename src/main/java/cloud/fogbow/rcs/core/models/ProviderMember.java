package cloud.fogbow.rcs.core.models;

public class ProviderMember {

    private String member;
    private String location;

    public ProviderMember() {
    }
    
    public ProviderMember(String member) {
        this.member = member;
    }
    
    public ProviderMember(String member, String location) {
        this.member = member;
        this.location = location;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
