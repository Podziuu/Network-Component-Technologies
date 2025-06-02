package pl.tks.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RollbackClientEvent {
    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("reason")
    private String reason;

    public RollbackClientEvent() {}

    public RollbackClientEvent(String clientId, String reason) {
        this.clientId = clientId;
        this.reason = reason;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RollbackClientEvent{" +
                "clientId='" + clientId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
