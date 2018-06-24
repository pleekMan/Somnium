package lights;

import globals.Main;
import globals.PAppletSingleton;

public class Picker {

	// VALUES ARE NORMALIZED
	float x, y;
	int color;
	Main p5;

	public Picker(float _x, float _y) {
		p5 = getP5();
		x = _x;
		y = _y;

		color = p5.color(0);
	}

	public void setX(float _x) {
		x = _x;
	}

	public void setY(float _y) {
		y = _y;
	}

	public void setColor(int _c) {
		color = _c;
	}

	public float getX() {
		// VALUES ARE NORMALIZED
		return x;
	}

	public float getY() {
		// VALUES ARE NORMALIZED
		return y;
	}

	public int getColor() {
		return color;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}