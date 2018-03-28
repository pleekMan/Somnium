package clips.spaceCreatures;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PVector;

public class Antennae {
	Main p5;

	PVector[] nodes;

	float motion = 39;
	float motionIncrement;
	float alphaMultiplier = 0;

	PGraphics drawLayer;

	public Antennae() {
		p5 = getP5();

		motionIncrement = p5.random(0.001f, 0.01f);

		nodes = new PVector[p5.floor(p5.random(5, 20))];

		nodes[0] = new PVector(0, 0, 10);

		for (int i = 1; i < nodes.length; i++) {
			float x = nodes[i - 1].x + p5.random(-10, 10);
			float y = nodes[i - 1].y + p5.random(5, 20);
			float z = nodes[i - 1].z * 1.3f;//random(20, 10);

			nodes[i] = new PVector(x, y, z);
		}
	}

	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
	}

	public void render() {
		drawLayer.noFill();

		drawLayer.beginShape();
		for (int i = 1; i < nodes.length; i++) {
			drawLayer.stroke(300 - (255 * (i / (float) nodes.length)), alphaMultiplier * 255);
			drawLayer.vertex(nodes[i].x + p5.random(-10, 10), nodes[i].y + p5.random(-10, 10), nodes[i].z);
		}
		drawLayer.endShape();

		motion += motionIncrement;
	}

	public void setOpacity(float value) {
		alphaMultiplier = value;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
