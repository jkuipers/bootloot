package nl.trifork.resttemplatebuilder.loot;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

@Configuration
public class RestTemplateConfig {

//    @Bean
    RestTemplateCustomizer loggingCustomizer() {
        return restTemplate -> {
            ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
            // not-so-proudly copied from the TestRestTemplate code:
            if (requestFactory instanceof InterceptingClientHttpRequestFactory) {
                Field requestFactoryField = ReflectionUtils.findField(RestTemplate.class, "requestFactory");
                ReflectionUtils.makeAccessible(requestFactoryField);
                requestFactory = (ClientHttpRequestFactory) ReflectionUtils.getField(requestFactoryField, restTemplate);
            }
            restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
            restTemplate.getInterceptors().add(new LoggingClientHttpRequestInterceptor());
        };
    }
}
