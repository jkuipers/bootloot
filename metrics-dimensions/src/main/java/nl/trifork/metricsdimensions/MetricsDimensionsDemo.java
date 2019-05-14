package nl.trifork.metricsdimensions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MetricsDimensionsDemo implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MetricsDimensionsDemo.class, args);
	}

	@Autowired RestTemplate restTemplate;

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 1000; i++) {
			try {
				System.out.println(restTemplate.getForObject("http://localhost:8080/search?query="+i, String.class));
			} catch (HttpStatusCodeException e) {
				System.out.println(e.getResponseBodyAsString());
			}
			try {
				System.out.println(restTemplate.postForObject("http://localhost:8080/loot", Integer.toString(i), String.class));
			} catch (HttpStatusCodeException e) {
				System.out.println(e.getResponseBodyAsString());
			}
		}

	}
}
