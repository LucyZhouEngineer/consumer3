package app.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiftRideRecord {
  private final List<LiftRide> rides;

  public LiftRideRecord() {
    this.rides = new CopyOnWriteArrayList<>();
  }

  public void addRide(LiftRide ride) {
    rides.add(ride);
  }

  public List<LiftRide> getRides() {
    return new ArrayList<>(rides);
  }
}