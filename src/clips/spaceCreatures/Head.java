package clips.spaceCreatures;

import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Head {

	Main p5;

	PVector pos;
	int iterations;
	int startCount;

	float faceSize;
	int faceVertices;
	int faceSkins;
	float faceRotation;
	float faceRotationVel;

	Antennae antennae;
	int antennaeCount;

	float alphaMultiplier = 0;
	
	PGraphics drawLayer;

	public Head() {
		p5 = getP5();
		
		pos = new PVector();
		iterations = 3;
		startCount = p5.floor(p5.random(5));

		faceVertices = p5.floor(p5.random(3, 8));
		faceSkins = 5;
		faceRotation = 0;
		faceRotationVel = p5.random(0.05f);
		faceSize = 50;

		antennae = new Antennae();
		antennaeCount = p5.floor(p5.random(10, 30));
	}
	
	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
		antennae.setDrawLayer(_drawLayer);
	}

	void render() {

		drawLayer.pushMatrix();

		drawLayer.translate(pos.x, pos.y, pos.z);

		drawLayer.noStroke();
		//eyes(startCount, 100, pos, 0);
		drawAntennae();
		ball();

		drawLayer.popMatrix();
	}

	void ball() {

		//noStroke();
		drawLayer.noFill();

		drawLayer.pushMatrix();

		float rotationUnit = p5.TWO_PI / (faceSkins * 2);

		drawLayer.translate(pos.x, pos.y, pos.z + (200 * p5.tan(faceRotation)));

		for (int s = 0; s < faceSkins; s++) {

			drawLayer.pushMatrix();
			drawLayer.rotateY(faceRotation + (s * rotationUnit));

			float vertexUnit = p5.TWO_PI / faceVertices;

			//fill(255 * (s / (float)faceSkins * 2),0,0);
			drawLayer.stroke((255 * (s / (float) faceSkins * 2)) * alphaMultiplier, 0, 0);

			drawLayer.beginShape();
			for (int v = 0; v < faceVertices; v++) {

				float x = faceSize * p5.cos(vertexUnit * v);
				float y = faceSize * p5.sin(vertexUnit * v);

				//vertex(x, y, pos.z + (200 * sin(faceRotation))); // HACE COMO UN PARAPENTE
				drawLayer.vertex(x, y, pos.z);
			}

			drawLayer.endShape(p5.CLOSE);

			drawLayer.popMatrix();
		}

		drawLayer.popMatrix();

		faceRotation += faceRotationVel;
	}

	void drawAntennae() {

		float rotationUnit = p5.TWO_PI / (float) antennaeCount;
		drawLayer.pushMatrix();
		drawLayer.scale(1, 1, 0.3f);
		drawLayer.rotateZ(faceRotation);

		for (int i = 0; i < antennaeCount; i++) {

			drawLayer.pushMatrix();
			drawLayer.rotateZ(rotationUnit * i);

			antennae.render();

			drawLayer.popMatrix();
		}

		drawLayer.popMatrix();
	}

	void eyes(int count, float radius, PVector pos, int iter) {
		// RECURSIVE FUNCTION, WATCH OUT..!!

		if (iter < iterations) {

			float xPos = 0;
			float yPos = 0;

			float angleSeparation = p5.TWO_PI / count;

			PVector newPos = new PVector();

			drawLayer.fill(50 + (iter * 50));

			for (int i = 0; i < count; i++) {
				xPos = radius * p5.cos((angleSeparation * i));
				yPos = radius * p5.sin((angleSeparation * i));

				newPos.set(xPos, yPos, pos.z);

				drawLayer.pushMatrix();
				drawLayer.translate(newPos.x, newPos.y, newPos.z);
				drawLayer.ellipse(0, 0, radius, radius);
				//sphere(radius);
				drawLayer.popMatrix();

				//println(iter);
				int newIter = iter + 1;
				eyes(count * 2, radius * 0.5f, PVector.add(newPos, new PVector(0, 0, -radius * 2)), newIter);
			}
		}
	}

	void setPosition(PVector _pos) {
		pos = _pos;
	}

	void setFaceSize(float _s) {
		faceSize = _s * 0.5f;
	}

	void setOpacity(float value) {
		alphaMultiplier = value;
		antennae.setOpacity(alphaMultiplier);
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
