package co.com.worldoffice.shoppingcart.batch.steps;

import co.com.worldoffice.shoppingcart.batch.dto.BatchStepsDTO;
import co.com.worldoffice.shoppingcart.utils.interfaces.CorrelationIdAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Reader implements ItemReader<BatchStepsDTO>, CorrelationIdAllocator {

    private final String filePath;

    private InputStream in;
    private InputStreamReader isr;
    private BufferedReader bf;
    private boolean isOpenConnections = false;

    public Reader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public BatchStepsDTO read() {
        String correlationId = assignCorrelative();
        BatchStepsDTO.BatchStepsDTOBuilder responseBuilder = BatchStepsDTO.builder().correlativeID(correlationId);
        try {
            if (!isOpenConnections) {
                openConnections();
            }
            String line = bf.readLine();
            if (line != null) {
                log.debug("");
                log.info("line read: {}", line);
                return responseBuilder.line(line).build();
            } else {
                closeConnections();
                return null;
            }
        } catch (IOException e) {
            log.error("Error reading file");
            log.trace("Error reading file, trace: ", e);
            return responseBuilder.build();
        }
    }

    private void openConnections() {
        try {
            log.info("Uploading initial products starting");
            log.debug("Doing connection with csv file...");

            this.in = new ClassPathResource(filePath).getInputStream();
            this.isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            this.bf = new BufferedReader(isr);
            isOpenConnections = true;
            log.debug("Reading file...");

            String fileColumns = bf.readLine();
            log.debug("Columns of file: {}", fileColumns);
        } catch (IOException e) {
            log.error("Error reading first line of file");
            log.trace("Error reading first line of file, trace: ", e);
        }
    }

    private void closeConnections() {
        isOpenConnections = false;
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}