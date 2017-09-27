package ray1.surface;

import egl.math.MathHelper;
import egl.math.Vector3d;
import ray1.IntersectionRecord;
import ray1.Ray;
import egl.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
  
  /** The center of the sphere. */
  protected final Vector3 center = new Vector3();
  public void setCenter(Vector3 center) { this.center.set(center); }
  
  /** The radius of the sphere. */
  protected float radius = 1.0f;
  public void setRadius(float radius) { this.radius = radius; }
  
  protected final double M_2PI = 2 * Math.PI;
  
  public Sphere() { }
  
  /**
   * Tests this surface for intersection with ray. If an intersection is found
   * record is filled out with the information about the intersection and the
   * method returns true. It returns false otherwise and the information in
   * outRecord is not modified.
   *
   * @param outRecord the output IntersectionRecord
   * @param rayIn the ray to intersect
   * @return true if the surface intersects the ray
   */
  public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
      Vector3d eSubC = rayIn.origin.clone().sub(center);
      double t = rayIn.direction.clone().dot(eSubC.clone());
      double tt = t * t;
      double e = (eSubC.clone().dot(eSubC.clone()) - radius * radius);
      double discriminant = tt - e;
      if(discriminant >= 0) {
          double sqrtRes = Math.sqrt(discriminant);
          double res = -t - sqrtRes;
          Vector3d intersectPt = rayIn.origin.clone().add(rayIn.direction.clone().mul(res));
          Vector3d normal = intersectPt.clone().sub(center).div(radius);

          outRecord.location.set(intersectPt);
          outRecord.normal.set(normal);
          outRecord.t = res;
          outRecord.surface = this;
          //TODO
          //outRecord.texCoords
          return true;
      }
      else {
          return false;
      }
  }
  
  /**
   * @see Object#toString()
   */
  public String toString() {
    return "sphere " + center + " " + radius + " " + shader + " end";
  }

}