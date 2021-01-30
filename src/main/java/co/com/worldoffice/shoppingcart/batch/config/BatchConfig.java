package co.com.worldoffice.shoppingcart.batch.config;

import co.com.worldoffice.shoppingcart.batch.dto.BatchStepsDTO;
import co.com.worldoffice.shoppingcart.batch.steps.JobCompletionListener;
import co.com.worldoffice.shoppingcart.batch.steps.Processor;
import co.com.worldoffice.shoppingcart.batch.steps.Reader;
import co.com.worldoffice.shoppingcart.batch.steps.Writer;
import co.com.worldoffice.shoppingcart.domain.repository.IProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
public class BatchConfig {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job processJob(UploadProductsRunnerProperties uploadProductsRunnerProperties, IProductRepository productRepository) {
        return jobBuilderFactory.get("processJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(uploadCSVData(uploadProductsRunnerProperties, productRepository))
                .end()
                .build();
    }

    @Bean
    public Step uploadCSVData(UploadProductsRunnerProperties uploadProductsRunnerProperties, IProductRepository productRepository) {
        return stepBuilderFactory.get("uploadCSVData").<BatchStepsDTO, Mono<BatchStepsDTO>>chunk(1)
                .reader(new Reader(uploadProductsRunnerProperties.getInitialProductsFilePath()))
                .processor(new Processor())
                .writer(new Writer(productRepository))
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionListener();
    }
}