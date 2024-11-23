package app;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class Main {
  public static void main(String[] args) throws IOException, TimeoutException {
    Consumer consumer = new Consumer();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        consumer.shutdown();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (TimeoutException e) {
        throw new RuntimeException(e);
      }
    }));

    consumer.startToConsume();
  }
}

