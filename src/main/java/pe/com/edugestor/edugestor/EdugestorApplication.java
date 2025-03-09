package pe.com.edugestor.edugestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EdugestorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdugestorApplication.class, args);
	}

}
