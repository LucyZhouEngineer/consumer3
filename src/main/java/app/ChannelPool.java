package app;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChannelPool {
  private static final Logger log = LoggerFactory.getLogger(ChannelPool.class);
  private final BlockingQueue<Channel> pool;
  private final Connection connection;
  private final int poolSize;

  public ChannelPool(Connection connection, int poolSize) {
    this.connection = connection;
    this.poolSize = poolSize;
    this.pool = new LinkedBlockingQueue<>(poolSize);
    initializePool();
  }

  private void initializePool() {
    try {
      for (int i = 0; i < poolSize; i++) {
        Channel channel = connection.createChannel();
        pool.offer(channel);
      }
    } catch (IOException e) {
      log.error("Error initializing channel pool", e);
      throw new RuntimeException("Failed to initialize channel pool", e);
    }
  }

  public Channel borrowChannel() throws IOException {
    Channel channel = pool.poll();
    if (channel == null || !channel.isOpen()) {
      log.debug("Creating a new channel as pool is empty or channel was closed");
      channel = connection.createChannel();
    }
    return channel;
  }

  public void returnChannel(Channel channel) {
    if (channel != null && channel.isOpen()) {
      pool.offer(channel);
    }
  }

  public void close() {
    pool.forEach(channel -> {
      try {
        if (channel != null && channel.isOpen()) {
          channel.close();
        }
      } catch (Exception e) {
        log.error("Error closing channel", e);
      }
    });
  }
}