package ray1.shader;

import egl.math.Color;
import egl.math.MathHelper;
import ray1.shader.Texture;
import egl.math.Colorf;
import egl.math.Vector2;

import java.awt.font.ImageGraphicAttribute;

/**
 * A Texture class that treats UV-coordinates outside the [0.0, 1.0] range as if they
 * were at the nearest image boundary.
 * @author eschweic zz335
 *
 */
public class ClampTexture extends Texture {

	public Colorf getTexColor(Vector2 texCoord) {
		if (image == null) {
			System.err.println("Warning: Texture uninitialized!");
			return new Colorf();
		}
				
		// 1) Convert the input texture coordinates to integer pixel coordinates. Adding 0.5
		//    before casting a double to an int gives better nearest-pixel rounding.
		// 2) Clamp the resulting coordinates to the image boundary.
		// 3) Create a Color object based on the pixel coordinate (use Color.fromIntRGB
		//    and the image object from the Texture class), convert it to a Colord, and return it.
		// NOTE: By convention, UV coordinates specify the lower-left corner of the image as the
		//    origin, but the ImageBuffer class specifies the upper-left corner as the origin.
		int i = (int) Math.round(texCoord.x * image.getWidth() + 0.5);
		int j = (int) Math.round(texCoord.y * image.getHeight() + 0.5);
		i = MathHelper.clamp(i, 0, image.getWidth());
		j = MathHelper.clamp(j, 0, image.getHeight());
        Color color = Color.fromIntRGB(image.getRGB(i,j));
        return new Colorf(color);
	}

}
