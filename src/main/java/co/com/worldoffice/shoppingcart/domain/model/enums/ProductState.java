package co.com.worldoffice.shoppingcart.domain.model.enums;

import java.util.Arrays;

public enum ProductState {
    NEW("Nuevo"), USED("Usado");

    private final String value;

    ProductState(String stringValue) {
        value = stringValue;
    }

    public static ProductState getByValue(String value) {
        return Arrays.stream(ProductState.values())
                .filter(state -> state.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
