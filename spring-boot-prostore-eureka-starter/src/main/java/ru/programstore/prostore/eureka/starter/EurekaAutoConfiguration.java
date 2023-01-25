package ru.programstore.prostore.eureka.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.programstore.prostore.core.JsonCommandHandler;
import ru.programstore.prostore.core.JsonQueryHandler;
import ru.programstore.prostore.core.CommandBus;
import ru.programstore.prostore.core.QueryBus;
import ru.programstore.prostore.eureka.EurekaCommandBus;
import ru.programstore.prostore.eureka.EurekaCommandListener;
import ru.programstore.prostore.eureka.EurekaQueryBus;
import ru.programstore.prostore.eureka.EurekaQueryListener;

@Configuration
@EnableConfigurationProperties(EurekaProperties.class)
public class EurekaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CommandBus commandBus(EurekaClient eurekaClient, HttpClient httpClient, ObjectMapper objectMapper) {
        return new EurekaCommandBus(
            eurekaClient, httpClient, objectMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryBus queryBus(EurekaClient eurekaClient, HttpClient httpClient, ObjectMapper objectMapper) {
        return new EurekaQueryBus(
            eurekaClient, httpClient, objectMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public EurekaCommandListener eurekaCommandListener(JsonCommandHandler jsonCommandHandler) {
        return new EurekaCommandListener(jsonCommandHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public EurekaQueryListener eurekaQueryListener(JsonQueryHandler jsonQueryHandler) {
        return new EurekaQueryListener(jsonQueryHandler);
    }

    @Bean
    @Qualifier("eureka")
    @ConditionalOnMissingBean
    public HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    @Bean
    @Qualifier("eureka")
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
