package clips.spaceCreatures;

import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;


public class Creature {
	Main p5;

	PVector position;
	PVector velocity;
	PVector rotation;

	int skinType;
	int color1; //color
	int color2;

	PVector headPos;

	//PVector size;

	int bodyResRadial = 10;
	int bodyResAlong = 40;
	float radiusMax = 50;
	float radiusMin = 10;
	float tall;

	// INTERACTIVITY & TRIGGERS  
	float radiusMaxMultiplier = 1;
	float spikesLengthMultiplier = 0.1f;
	float spikesWidthMultiplier = 1;
	float opacityMultiplier = 0;
	float swimMultiplier = 1;
	boolean starting = false;

	float radiusOsc;
	float radiusOscIncrement;

	float waveOscX;
	float waveOscY;
	float waveIncrement;

	Head head;

	PGraphics drawLayer;

	public Creature() {
		p5 = getP5();
		radiusOsc = p5.random(100);
		radiusOscIncrement = p5.random(0.1f);

		waveOscX = p5.random(10);
		waveOscY = p5.random(10);
		waveIncrement = p5.random(0.3f);

		position = new PVector();
		headPos = new PVector();
		tall = p5.random(500, 1000);


		skinType = 0;

		tall = p5.random(100, 500);

		head = new Head();
		head.setFaceSize(radiusMax);
	}

	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
		head.setDrawLayer(_drawLayer);

	}

	public void update() {
		if (starting) {
			opacityMultiplier += 0.01;
			if (opacityMultiplier >= 1) {
				starting = false;
				opacityMultiplier = 1;
			}
			head.setOpacity(opacityMultiplier);
		}
	}

	public void render() {
		drawLayer.pushStyle();
		drawLayer.sphereDetail(5);
		//stroke(255,0,0);
		//strokeWeight(4);
		drawLayer.noStroke();

		// INTERACTIVITY & TRIGGERS  
		radiusMaxMultiplier *= 0.95;
		radiusMaxMultiplier = p5.constrain(radiusMaxMultiplier, 1, 999);

		spikesWidthMultiplier *= 0.9;
		spikesWidthMultiplier = p5.constrain(spikesWidthMultiplier, 1, 100);

		swimMultiplier *= 0.99;
		swimMultiplier = p5.constrain(swimMultiplier, 1, 20);

		drawLayer.pushMatrix();

		// GLOBAL CREATURE TRANSLATION, LIKE SWIMMING IN WAVES
		float xSwim = p5.map(p5.sin((radiusOsc * swimMultiplier) * 0.2f), -1, 1, -100, 100);
		float ySwim = p5.map(p5.cos((radiusOsc * swimMultiplier) * 0.2f), -1, 1, -100, 100) * 1.3f;
		//float zSwim = xSwim * ySwim * 0.2;
		float zSwim = p5.sin(waveOscX * 0.2f) * 200;

		drawLayer.translate(position.x + xSwim, position.y + ySwim, position.z + zSwim);

		// HEAD
		head.setPosition(new PVector());
		head.render();

		//noStroke();
		//fill(255);
		//sphere(30);

		drawLayer.scale(1, 1, 1 + (p5.abs(xSwim) * 0.05f)); // ABS creates a pulling gesture in the creatures

		//translate(position.x, position.y, position.z);

		for (int z = 0; z < bodyResAlong - 1; z++) {
			float zPos = (tall / bodyResAlong) * z;// + position.z
			float zPosNext = (tall / bodyResAlong) * (z + 1);// + position.z

			float ringOscillation = p5.sin(radiusOsc + (zPos * -0.05f));
			float ringOscillationNext = p5.sin(radiusOsc + (zPosNext * -0.05f));

			//float ringOscillation2 = cos((radiusOsc * 1.2) + (zPos * -0.05));

			float nowRadius = p5.map(ringOscillation, -1, 1, radiusMin, radiusMax) * radiusMaxMultiplier;
			float nowRadiusNext = p5.map(ringOscillationNext, -1, 1, radiusMin, radiusMax * 1.2f) * radiusMaxMultiplier;

			int c = p5.lerpColor(color1, color2, z / (float) bodyResAlong);
			//fill(c, ((ringOscillation + 1) * 0.5) * 255);
			//stroke(c, 255);

			// SHIFT EACH RING OVER A WAVE
			float waveShiftX = p5.sin(waveOscX + (waveIncrement * z)) * 20;
			float waveShiftY = p5.cos(waveOscY + (waveIncrement * z)) * 20;

			drawLayer.pushMatrix();
			drawLayer.translate(waveShiftX, waveShiftY);

			for (int j = 0; j < bodyResRadial; j++) {
				float x = nowRadius * (p5.cos((p5.TWO_PI / bodyResRadial) * j));
				float y = nowRadius * (p5.sin((p5.TWO_PI / bodyResRadial) * j));
				float x1 = nowRadius * (p5.cos((p5.TWO_PI / bodyResRadial) * (j + 1)));
				float y1 = nowRadius * (p5.sin((p5.TWO_PI / bodyResRadial) * (j + 1)));
				float x2 = nowRadiusNext * (p5.cos((p5.TWO_PI / bodyResRadial) * (j + 1)));
				float y2 = nowRadiusNext * (p5.sin((p5.TWO_PI / bodyResRadial) * (j + 1)));
				float x3 = nowRadiusNext * (p5.cos((p5.TWO_PI / bodyResRadial) * j));
				float y3 = nowRadiusNext * (p5.sin((p5.TWO_PI / bodyResRadial) * j));

				drawLayer.noStroke();
				drawLayer.fill(c, (((ringOscillation + 1) * 0.5f) * 255) * opacityMultiplier);
				drawSkin(skinType, x, y, x1, y1, x2, y2, x3, y3, zPos, zPosNext);

				drawLayer.stroke(((ringOscillation + 1) * 255) * opacityMultiplier);
				drawSpikes(x, y, zPos, x1, y1, zPos, x2, y2, zPosNext);
			}

			drawLayer.popMatrix();
		}

		drawLayer.popMatrix();

		radiusOsc += radiusOscIncrement;
		waveOscX += waveIncrement;
		waveOscY += waveIncrement;

		drawLayer.popStyle();
	}

	void drawSkin(int skinType, float x, float y, float x1, float y1, float x2, float y2, float x3, float y3, float zPos, float zPosNext) {

		if (skinType == 0) {
			drawLayer.beginShape(p5.QUADS);
			drawLayer.vertex(x, y, zPos);
			drawLayer.vertex(x1, y1, zPos);
			drawLayer.vertex(x2, y2, zPosNext);
			drawLayer.vertex(x3, y3, zPosNext);
			drawLayer.endShape();
		} else if (skinType == 1) {
			drawLayer.beginShape(p5.TRIANGLE_FAN);
			drawLayer.vertex(x, y, zPos);
			drawLayer.vertex(x1, y1, zPos);
			drawLayer.vertex(x2, y2, zPosNext);
			drawLayer.endShape();
		}
	}

	public void setPosition(PVector pos) {
		position.set(pos);
	}

	public void setOscillation(float seed, float oscVelocity) {
		radiusOsc = seed;
		radiusOscIncrement = oscVelocity;
	}

	public void setSize(float minRadius, float maxRadius) {
		radiusMin = minRadius;
		radiusMax = maxRadius;
		head.setFaceSize(radiusMax);
	}

	public void drawSpikes(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		// THE SPIKES ARE THE QUAD NORMALS
		PVector side1 = new PVector(x2 - x1, y2 - y1, z2 - z1);
		PVector side2 = new PVector(x3 - x1, y3 - y1, z3 - z1);

		side1.normalize();
		side2.normalize();

		PVector normalVector = side1.cross(side2);
		normalVector.mult(20 * spikesLengthMultiplier);

		drawLayer.strokeWeight(spikesWidthMultiplier);
		drawLayer.line(x1, y1, z1, normalVector.x + x1, normalVector.y + y1, normalVector.z + z1);
		drawLayer.strokeWeight(1);
	}

	public void setSkinType(int type) {
		skinType = type;
	}

	public void setColors(int c1, int c2) {
		color1 = c1;
		color2 = c2;
	}

	public void trigger() {
		starting = true;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
