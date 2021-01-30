package co.com.worldoffice.shoppingcart.utils.interfaces;

import org.slf4j.MDC;

import java.util.UUID;

public interface CorrelationIdAllocator {

    default String assignCorrelative() {
        return assignCorrelative(null);
    }

    default String assignCorrelative(String correlative) {
        String cId = correlative;

        if (cId == null || cId.isEmpty()) {
            cId = getNewCorrelative();
        }

        MDC.putCloseable("correlation-id", cId);
        return cId;
    }

    default String getNewCorrelative() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
