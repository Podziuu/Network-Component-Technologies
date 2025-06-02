package pl.tks.ports.infrastructure;

import java.util.UUID;

public interface RollbackHandlerPort {
    void handleClientCreationRollback(String userId, String reason);
}