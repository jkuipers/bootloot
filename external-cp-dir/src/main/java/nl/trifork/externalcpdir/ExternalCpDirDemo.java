package nl.trifork.externalcpdir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Enumeration;

@SpringBootApplication
public class ExternalCpDirDemo {

	static Logger LOG = LoggerFactory.getLogger(ExternalCpDirDemo.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ExternalCpDirDemo.class, args);

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		try (InputStream is = new ClassPathResource("truststore.jks").getInputStream()) {
			trustStore.load(is, null);
		}
		LOG.info("Loaded TrustStore with aliases:");
		Enumeration<String> aliases = trustStore.aliases();
		while (aliases.hasMoreElements()) {
			LOG.info("\t{}", aliases.nextElement());
		}
	}

}
