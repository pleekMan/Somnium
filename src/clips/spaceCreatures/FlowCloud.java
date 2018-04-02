package clips.spaceCreatures;

import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class FlowCloud {
	Main p5;
	PVector[] particlesPosition;
	PVector[] particlesVelocity;

	PVector size;
	float sizeMultiplier = 4;

	int[] colorPair; //color
	
	PGraphics drawLayer;
	
	public FlowCloud(PVector _size) {

		p5 = getP5();

		size = _size.copy();
		size.mult(sizeMultiplier);

		colorPair = new int[2];

		particlesPosition = new PVector[200];
		particlesVelocity = new PVector[200];

		for (int i = 0; i < particlesPosition.length; i++) {
			particlesPosition[i] = new PVector(p5.random(size.x), p5.random(size.y), p5.random(size.z));
			particlesVelocity[i] = new PVector(0, 0, p5.random(1, 5));
		}
	}
	
	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
	}
	public void render() {
		//drawLayer.stroke(255);
		drawLayer.strokeWeight(2);
		//drawLayer.noFill();
		//noStroke();

		drawLayer.pushMatrix();
		drawLayer.translate(size.x * -0.5f, -size.y, size.z * -0.5f);

		//beginShape(TRIANGLES);
		for (int i = 0; i < particlesPosition.length; i++) {
			particlesPosition[i].add(particlesVelocity[i]);

			if (particlesPosition[i].z > size.z) {
				particlesPosition[i].set(p5.random(size.x), p5.random(size.y), 0);
			}

			drawLayer.stroke(p5.lerpColor(colorPair[0], colorPair[1], (particlesPosition[i].z / size.z)));
			drawLayer.line(particlesPosition[i].x, particlesPosition[i].y, particlesPosition[i].z, particlesPosition[i].x - particlesVelocity[i].x * 5, particlesPosition[i].y - particlesVelocity[i].y * 5, particlesPosition[i].z - particlesVelocity[i].z * 5);
			//vertex(particlesPosition[i].x, particlesPosition[i].y, particlesPosition[i].z);  
		}
		//drawLayer.endShape();
		drawLayer.popMatrix();
	}

	void setColorPair(int c1, int c2) {
		colorPair[0] = c1;
		colorPair[1] = c2;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
