package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import app.Structure.LiftRideRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.dbcp2.BasicDataSource;

public class LiftRideDAO {

  private static final Logger log = LoggerFactory.getLogger(LiftRideDAO.class);
  private static final BasicDataSource dataSource = DataSource.getDataSource();

  public void saveToDatabase(List<LiftRideRequest> requests) {
    String query = "INSERT INTO SkierLiftRides (resortID, seasonID, dayID, skierID, liftID, time) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

      for (LiftRideRequest request : requests) {
        stmt.setInt(1, request.getResortID());
        stmt.setString(2, request.getSeasonID());
        stmt.setString(3, request.getDayID());
        stmt.setInt(4, request.getSkierID());
        stmt.setInt(5, request.getLiftRide().getLiftID());
        stmt.setInt(6, request.getLiftRide().getTime());
        stmt.addBatch();
      }

      int[] result = stmt.executeBatch();
      System.out.println("Inserted " + result.length + " records into the database.");
    } catch (SQLException e) {
      throw new RuntimeException("Error saving to database", e);
    }
  }
}
