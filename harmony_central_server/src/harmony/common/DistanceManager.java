package harmony.common;

/**
 * 
 * @author Seongjun Park
 * @since 2016/5/27
 * @version 2016/5/27
 */
public class DistanceManager {

  /**
   * 
   * @param latitude1
   * @param longitude1
   * @param latitude2
   * @param longitude2
   * @return
   */
  public static double distance(double latitude1, double longitude1,
      double latitude2, double longitude2) {
    double theta = longitude1 - longitude2;
    double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2))
        + Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2))
            * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    dist *= 1.609344; // kilometer
    dist *= 1000.0; // meter

    return dist;
  }

  private static double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private static double rad2deg(double rad) {
    return (rad / Math.PI * 180.0);
  }
}
