package nl.trifork.metricsdimensions.loot;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Configuration
public class MetricsConfig {

    /**
     * Ensures that <code>uri</code> tags of HTTP-related metrics do not include request parameters,
     * to reduce cardinality.
     *
     * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-metrics-rest-template">RestTemplate metrics</a>
     */
    @Bean
    MeterFilter queryParameterStrippingMeterFilter() {
        return MeterFilter.replaceTagValues("uri", url -> {
            int i = url.indexOf('?');
            return i == -1 ? url : url.substring(0, i);
        });
    }


    /**
     * Adds a {@code monitoring_request} tag to each metric containing either {@code true} or {@code false}
     * based on the presence of an HTTP request header {@code X-Monitoring} with a value of {@code true}
     * (case-insensitive).
     * <p>
     * This ensures that we can separate e.g. HTTP metrics resulting from monitoring requests from regular requests.
     */
    @Bean
    MeterFilter monitoringRequestTaggingMeterFilter() {
        return new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                boolean monitoringRequest = false;
                ServletRequestAttributes reqAttrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (reqAttrs != null) {
                    monitoringRequest = "true".equalsIgnoreCase(reqAttrs.getRequest().getHeader("X-Monitoring"));
                }
                return id.withTag(Tag.of("monitoring_request", Boolean.toString(monitoringRequest)));
            }
        };
    }

    /**
     * Required to allow the  above filter to retrieve the request from the RequestContextHolder
     */
    @Bean
    RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
