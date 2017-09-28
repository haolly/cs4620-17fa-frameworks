package ray1.shader;

import egl.math.*;
import ray1.IntersectionRecord;
import ray1.Light;
import ray1.Ray;
import ray1.Scene;

import java.util.List;

/**
 * A Phong material.
 *
 * @author ags, pramook
 */
public class Phong extends Shader {

	/** The color of the diffuse reflection. */
	protected final Colorf diffuseColor = new Colorf(Color.White);
	public void setDiffuseColor(Colorf diffuseColor) { this.diffuseColor.set(diffuseColor); }
	public Colorf getDiffuseColor() {return new Colorf(diffuseColor);}

	/** The color of the specular reflection. */
	protected final Colorf specularColor = new Colorf(Color.White);
	public void setSpecularColor(Colorf specularColor) { this.specularColor.set(specularColor); }
	public Colorf getSpecularColor() {return new Colorf(specularColor);}

	/** The exponent controlling the sharpness of the specular reflection. */
	protected float exponent = 1.0f;
	public void setExponent(float exponent) { this.exponent = exponent; }
	public float getExponent() {return exponent;}

	public Phong() { }

	/**
	 * @see Object#toString()
	 */
	public String toString() {    
		return "phong " + diffuseColor + " " + specularColor + " " + exponent + " end";
	}

	/**
	 * Evaluate the intensity for a given intersection using the Phong shading model.
	 *
	 * @param outIntensity The color returned towards the source of the incoming ray.
	 * @param scene The scene in which the surface exists.
	 * @param ray The ray which intersected the surface.
	 * @param record The intersection record of where the ray intersected the surface.
	 */
	@Override
	public void shade(Colorf outIntensity, Scene scene, Ray ray, IntersectionRecord record) {
		// 1) Loop through each light in the scene.
		// 2) If the intersection point is shadowed, skip the calculation for the light.
		//	  See Shader.java for a useful shadowing function.
		// 3) Compute the incoming direction by subtracting
		//    the intersection point from the light's position.
		// 4) Compute the color of the point using the Phong shading model. Add this value
		//    to the output.

		outIntensity.set(0);
		List<Light> lights = scene.getLights();
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			Ray shadowRay = new Ray();
			boolean isShadow = isShadowed(scene, light, record, shadowRay);
			if(!isShadow) {
				Colorf lightIntensity = new Colorf();
				Vector3d lightDir = record.location.clone().sub(light.position);
				double r = lightDir.lenSq();
				Vector3d l = lightDir.clone().mul(-1).normalize();
				lightIntensity.set(light.intensity.clone().div((float)r));
				Vector3 diffuse;
				if(texture != null) {
				    diffuseColor.set(texture.getTexColor(new Vector2(record.texCoords)));
                }
                diffuse = getDiffuseColor().mul((float) Math.max(0, l.clone().dot(record.normal)));
				Vector3d h = (l.clone().add(ray.direction.clone().mul(-1).normalize())).normalize();
				double exp = Math.pow(Math.max(0, record.normal.clone().dot(h)), exponent);
				Vector3 specular = specularColor.mul((float) exp);
				lightIntensity.mul(diffuse.add(specular));
				outIntensity.add(lightIntensity);
			}
		}
	}

}
