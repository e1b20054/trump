package group9.trump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TrumpApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrumpApplication.class, args);
  }

}
