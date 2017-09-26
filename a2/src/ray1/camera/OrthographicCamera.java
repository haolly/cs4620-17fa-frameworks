package ray1.camera;

import egl.math.Vector3;
import ray1.Ray;

public class OrthographicCamera extends Camera {

    private Vector3 u;
    private Vector3 v;
    private Vector3 w;


    /**
     * Initialize the derived view variables to prepare for using the camera.
     */
    public void init() {
        w = getViewDir().clone().mul(-1).normalize();
        u = getViewUp().clone().cross(w).normalize();
        v = w.clone().cross(u).normalize();
    }

    /**
     * Set outRay to be a ray from the camera through a point in the image.
     *
     * @param outRay The output ray (not normalized)
     * @param inU The u coord of the image point (range [0,1])
     * @param inV The v coord of the image point (range [0,1])
     */
    public void getRay(Ray outRay, float inU, float inV) {
        double fu = (inU * getViewWidth());
        double fuNew = fu - getViewWidth()/2;
        double fv = (inV * getViewHeight());
        double fvNew = fv - getViewHeight()/2;
        Vector3 origin = getViewPoint().add(u.clone().mul((float) fuNew)).add(v.clone().mul((float) fvNew));
        outRay.direction.set(-w.x, -w.y, -w.z);
        outRay.origin.set(origin.x, origin.y, origin.z);
    }

}
