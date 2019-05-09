package nl.trifork.mvcerrors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@SpringBootApplication
public class MvcErrorsDemo {

	public static void main(String[] args) {
		SpringApplication.run(MvcErrorsDemo.class, args);
	}

	@RestController
	static class DemoController {
		@PostMapping("/")
		String handle(@Valid @RequestBody LootboxPurchaseRequest request) {
			return "Request was valid";
		}
	}
}
