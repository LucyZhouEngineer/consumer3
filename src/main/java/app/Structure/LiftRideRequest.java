package app.Structure;

public class LiftRideRequest {

  private Integer resortID;
  private String seasonID;
  private String dayID;
  private Integer skierID;
  private LiftRide liftRide;

  // Constructor

  public LiftRideRequest() {
  }

  public LiftRideRequest(Integer resortID, String seasonID, String dayID, Integer skierID,
      LiftRide liftRide) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.skierID = skierID;
    this.liftRide = liftRide;
  }

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

  public LiftRide getLiftRide() {
    return liftRide;
  }
}
