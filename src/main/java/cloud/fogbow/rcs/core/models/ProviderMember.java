package cloud.fogbow.rcs.core.models;

import java.util.List;

public class ProviderMember {

    private String member;
    private List<Service> services;

    public ProviderMember() {
    }
    
    public ProviderMember(String member) {
        this.member = member;
    }
    
    public ProviderMember(String member, List<Service> services) {
        this.member = member;
        this.services = services;
    }
    
    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
