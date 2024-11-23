package app.Structure;

import java.util.Objects;

public class LiftRide {

  private Integer time;
  private Integer liftID;

  // Constructor
  public LiftRide() {}

  public LiftRide(Integer time, Integer liftID) {
    this.time = time;
    this.liftID = liftID;
  }

  // Getter and Setter for time
  public Integer getTime() {
    return time;
  }

  // Getter and Setter for liftID
  public Integer getLiftID() {
    return liftID;
  }

 
  // toString method for debugging
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Structure.LiftRide {\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    liftID: ").append(toIndentedString(liftID)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  // Helper method for formatting toString output
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
