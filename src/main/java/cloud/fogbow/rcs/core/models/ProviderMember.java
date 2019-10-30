package cloud.fogbow.rcs.core.models;

public class ProviderMember {

    private String member;
    private boolean local;

    public ProviderMember() {
    }

    public ProviderMember(String member, boolean local) {
        this.member = member;
        this.local = local;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
