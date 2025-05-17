package pl.tks.event;

public class RollbackClientEvent {
    private String clientId;

    public RollbackClientEvent() {}

    public RollbackClientEvent(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
