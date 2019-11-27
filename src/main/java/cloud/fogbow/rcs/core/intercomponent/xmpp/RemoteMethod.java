package cloud.fogbow.rcs.core.intercomponent.xmpp;

public enum RemoteMethod {

    REMOTE_GET_SERVICE("remoteGetService"),
    REMOTE_GET_SERVICES("remoteGerServices");
    
    private final String method;

    RemoteMethod(final String methodName) {
        this.method = methodName;
    }

    @Override
    public String toString() {
        return method;
    }
}
