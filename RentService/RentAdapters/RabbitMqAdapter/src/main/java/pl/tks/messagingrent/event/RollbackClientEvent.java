package pl.tks.messagingrent.event;

import java.io.Serializable;

public class RollbackClientEvent implements Serializable {
    private String clientId;
    private String reason;

    public RollbackClientEvent() {
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
}
