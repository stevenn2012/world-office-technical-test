package co.com.worldoffice.shoppingcart.batch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "properties.upload-products-runner")
public class UploadProductsRunnerProperties {
    private String initialProductsFilePath;
}
