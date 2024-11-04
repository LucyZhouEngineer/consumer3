
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

  private final static String QUEUE_NAME = "test";
  private static Map<Integer, Integer> liftRides = new HashMap<>();
  private static ObjectMapper objectMapper = new ObjectMapper();
    private static final int NUM_CONSUMERS = 64;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("52.12.172.16");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(100);
    
    ExecutorService executorService = Executors.newFixedThreadPool(NUM_CONSUMERS);
    for (int i = 0; i < NUM_CONSUMERS; i++) {
      executorService.submit(() -> {
        try {
          Channel consumerChannel = connection.createChannel();
          consumerChannel.basicConsume(QUEUE_NAME, false, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            consumerChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            updateLiftRides(message);
          }, consumerTag -> {});
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
  }
  
  private static void updateLiftRides(String message) {
    try {
      LiftRideRequest liftRideRequest = objectMapper.readValue(message, LiftRideRequest.class);
      int skierID = liftRideRequest.getSkierID();
      synchronized (liftRides) {
        liftRides.put(skierID, liftRides.getOrDefault(skierID, 0) + 1);
      }
//      System.out.println("Current lift rides: " + liftRides);
    } catch (Exception e) {
      System.err.println("Failed to update lift rides: " + e.getMessage());
    }
  }


    static class LiftRideRequest {
      private Integer resortID;
      private String seasonID;
      private String dayID;
      private Integer skierID;
      
      @JsonProperty("liftRide")
      private LiftRide body;
      
      public LiftRideRequest() {
      }
      //getters
        public Integer getResortID() {
            return resortID;
        }
        public String getSeasonID() {
            return seasonID;
        }
        public String getDayID() {
            return dayID;
        }
        public Integer getSkierID() {
            return skierID;
        }
        public LiftRide getBody() {
            return body;
        }
        
    }


    static class LiftRide {
      private Integer liftID;
      private Integer time;
  
      public LiftRide() {
      }
      public Integer getLiftID() {
        return liftID;
      }
      public Integer getTime() {
        return time;
      }
      }
}