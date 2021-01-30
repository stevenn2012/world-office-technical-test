package co.com.worldoffice.shoppingcart.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class UtilsHelper {

    private UtilsHelper() {
        // Not is necessary this implementation
    }

    public static boolean isNotDouble(String text) {
        try {
            Double.parseDouble(text.trim());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static boolean isNotNumber(String text) {
        try {
            Long.parseLong(text.trim());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static double getValueWithDiscount(double price, double discountPercent) {
        return price * (1 - (discountPercent / 100));
    }

    public static String getIp(ServerHttpRequest request) throws UnknownHostException {
        try {
            return request.getRemoteAddress().getAddress().getHostAddress();
        } catch (Exception e) {
            return InetAddress.getLocalHost().getHostAddress();
        }
    }
}