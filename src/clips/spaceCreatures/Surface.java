package clips.spaceCreatures;

import processing.core.PGraphics;
import globals.Main;
import globals.PAppletSingleton;

public class Surface {

	Main p5;

	float acrossNoiseStart; // noiseStart
	float acrossNoiseEnd; // noiseWidth
	float forwardNoiseStart;
	float forwardNoise;
	float forwardNoiseEnd;
	float forwardNoiseIncrement;

	int acrossRes = 50;
	int forwardRes = 100;

	float acrossLength = 3000;
	float forwardLength = 3000;

	float heightMultiplier = -1000;
	float lineLength;

	float alphaMultiplier;
	boolean starting = false;

	PGraphics drawLayer;

	Surface(float sceneSize) {
		p5 = getP5();
		
		forwardLength = sceneSize * 6;
		acrossLength = sceneSize * 6;
		/*
		radius = rad;
		 float centerY = 0 + radius + (radius * 0.1);
		 
		 center = new PVector(0, centerY, 0);
		 rotation = 0;
		 rotationIncrement = 0.01;
		 acrossResolution = 20; // line resolution (on half sphere)
		 */

		acrossNoiseStart = p5.random(10);
		acrossNoiseEnd = acrossNoiseStart + 4;
		forwardNoiseStart = p5.random(10);
		forwardNoiseEnd = forwardNoiseStart + 0.3f;
		forwardNoiseIncrement = -0.02f;

		lineLength = (forwardLength / forwardRes);
	}

	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
	}

	void render() {
		//p5.println("-|| Rendering Moon surface");
		if (starting) {
			alphaMultiplier += 0.01f;
			if (alphaMultiplier >= 1) {
				starting = false;
				alphaMultiplier = 1;
			}
		}
		//heightMultiplier = map(mouseY,height,0,0,-2000);

		//stroke(255);
		//noFill();
		//fill(255);
		drawLayer.noStroke();

		drawLayer.pushMatrix();
		drawLayer.translate(-(acrossLength * 0.5f), -heightMultiplier, -(forwardLength * 0.5f));

		for (int i = 0; i < forwardRes - 1; i++) {
			float zNorm = (i / (float) forwardRes);
			float zPos = zNorm * forwardLength;

			float zNormNext = ((i + 1) / (float) forwardRes);
			float zPosNext = zNormNext * forwardLength;
			//println(i + " Norm: " + zNormNext);
			//println(i + " pos: " + zPosNext);

			drawLayer.beginShape(p5.TRIANGLE_STRIP);
			for (int j = 0; j < acrossRes; j++) {
				float xNorm = (j / (float) acrossRes);
				float xPos = (j / (float) acrossRes) * acrossLength;

				float xInNoise = p5.map(xNorm, 0, 1, acrossNoiseStart, acrossNoiseEnd);
				float yInNoise = p5.map(zNorm, 0, 1, forwardNoise, forwardNoiseEnd);
				float yPos = p5.noise(xInNoise, yInNoise) * heightMultiplier;

				float yInNoiseNext = p5.map(zNormNext, 0, 1, forwardNoise, forwardNoiseEnd);
				float yPosNext = p5.noise(xInNoise, yInNoiseNext) * heightMultiplier;

				// CURVATURE BEND
				float bendStrength = p5.abs(p5.map(xNorm, 0, 1, -1, 1)) * -1500;

				drawLayer.fill((255 - (zNorm * 255)) * alphaMultiplier);

				drawLayer.vertex(xPos, yPos + bendStrength, zPos);
				drawLayer.vertex(xPos, yPosNext + bendStrength, zPosNext);
			}
			drawLayer.endShape();
		}

		drawLayer.popMatrix();

		forwardNoise += forwardNoiseIncrement;
		forwardNoiseEnd += forwardNoiseIncrement;
		/*
		pushStyle();
		 sphereDetail(40);
		 
		 noFill();
		 stroke(0, 255, 255);
		 
		 pushMatrix();
		 translate(center.x, center.y, center.z);
		 rotateZ(HALF_PI);
		 rotateY(rotation);
		 
		 
		 
		 
		 //sphere(radius);
		 popMatrix();
		 
		 popStyle();
		 rotation+=rotationIncrement;
		 
		 */
	}

	public void trigger() {
		starting = true;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
