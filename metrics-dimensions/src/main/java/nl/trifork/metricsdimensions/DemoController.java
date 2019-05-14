package nl.trifork.metricsdimensions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class DemoController {

    private Random random = new Random();

    @GetMapping("/search")
    ResponseEntity<String> search(@RequestParam String query) {
        if (random.nextInt(3) == 2) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Slow down!");
        }
        return ResponseEntity.ok("We tried, but no search results...");
    }

    @PostMapping("/loot")
    ResponseEntity<String> buyLoot(@RequestBody String productEan) {
        if (random.nextInt(4) == 3) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Not enough funds");
        }
        return ResponseEntity.ok("You got product " + productEan);
    }
}
