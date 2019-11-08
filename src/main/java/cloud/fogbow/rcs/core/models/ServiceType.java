package cloud.fogbow.rcs.core.models;

public enum ServiceType {

    LOCAL("local"), AS("as"), FNS("fns"), MS("ms"), RAS("ras");
    
    private String name;
    
    private ServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
