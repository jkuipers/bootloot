package nl.trifork.resttemplatebuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SelfInvokingController {

    private RestTemplate restTemplate = new RestTemplate();
    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/first")
    String first() {
        logger.info("Received call to first, calling second");
        String response = restTemplate.getForObject("http://localhost:8080/second", String.class);
        logger.info("Received response from second: {}", response);
        return response;
    }

    @GetMapping("/second")
    String second() {
        logger.info("Received call to second");
        return "hello from second";
    }
}
