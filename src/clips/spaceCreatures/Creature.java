package clips.spaceCreatures;

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
	float tall = p5.random(500, 1000);

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

	public Creature() {
		p5 = getP5();
		radiusOsc = p5.random(100);
		radiusOscIncrement = p5.random(0.1f);

		waveOscX = p5.random(10);
		waveOscY = p5.random(10);
		waveIncrement = p5.random(0.3f);

		position = new PVector();
		headPos = new PVector();

		skinType = 0;

		tall = p5.random(100, 500);

		head = new Head();
		head.setFaceSize(radiusMax);
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
		pushStyle();
		sphereDetail(5);
		//stroke(255,0,0);
		//strokeWeight(4);
		noStroke();

		// INTERACTIVITY & TRIGGERS  
		radiusMaxMultiplier *= 0.95;
		radiusMaxMultiplier = constrain(radiusMaxMultiplier, 1, 999);

		spikesWidthMultiplier *= 0.9;
		spikesWidthMultiplier = constrain(spikesWidthMultiplier, 1, 100);

		swimMultiplier *= 0.99;
		swimMultiplier = constrain(swimMultiplier, 1, 20);

		pushMatrix();

		// GLOBAL CREATURE TRANSLATION, LIKE SWIMMING IN WAVES
		float xSwim = map(sin((radiusOsc * swimMultiplier) * 0.2), -1, 1, -100, 100);
		float ySwim = map(cos((radiusOsc * swimMultiplier) * 0.2), -1, 1, -100, 100) * 1.3;
		//float zSwim = xSwim * ySwim * 0.2;
		float zSwim = sin(waveOscX * 0.2) * 200;

		translate(position.x + xSwim, position.y + ySwim, position.z + zSwim);

		// HEAD
		head.setPosition(new PVector());
		head.render();

		//noStroke();
		//fill(255);
		//sphere(30);

		scale(1, 1, 1 + (abs(xSwim) * 0.05)); // ABS creates a pulling gesture in the creatures

		//translate(position.x, position.y, position.z);

		for (int z = 0; z < bodyResAlong - 1; z++) {
			float zPos = (tall / bodyResAlong) * z;// + position.z
			float zPosNext = (tall / bodyResAlong) * (z + 1);// + position.z

			float ringOscillation = sin(radiusOsc + (zPos * -0.05));
			float ringOscillationNext = sin(radiusOsc + (zPosNext * -0.05));

			//float ringOscillation2 = cos((radiusOsc * 1.2) + (zPos * -0.05));

			float nowRadius = map(ringOscillation, -1, 1, radiusMin, radiusMax) * radiusMaxMultiplier;
			float nowRadiusNext = map(ringOscillationNext, -1, 1, radiusMin, radiusMax * 1.2) * radiusMaxMultiplier;

			color c = lerpColor(color1, color2, z / (float) bodyResAlong);
			//fill(c, ((ringOscillation + 1) * 0.5) * 255);
			//stroke(c, 255);

			// SHIFT EACH RING OVER A WAVE
			float waveShiftX = sin(waveOscX + (waveIncrement * z)) * 20;
			float waveShiftY = cos(waveOscY + (waveIncrement * z)) * 20;

			pushMatrix();
			translate(waveShiftX, waveShiftY);

			for (int j = 0; j < bodyResRadial; j++) {
				float x = nowRadius * (cos((TWO_PI / bodyResRadial) * j));
				float y = nowRadius * (sin((TWO_PI / bodyResRadial) * j));
				float x1 = nowRadius * (cos((TWO_PI / bodyResRadial) * (j + 1)));
				float y1 = nowRadius * (sin((TWO_PI / bodyResRadial) * (j + 1)));
				float x2 = nowRadiusNext * (cos((TWO_PI / bodyResRadial) * (j + 1)));
				float y2 = nowRadiusNext * (sin((TWO_PI / bodyResRadial) * (j + 1)));
				float x3 = nowRadiusNext * (cos((TWO_PI / bodyResRadial) * j));
				float y3 = nowRadiusNext * (sin((TWO_PI / bodyResRadial) * j));

				noStroke();
				fill(c, (((ringOscillation + 1) * 0.5) * 255) * opacityMultiplier);
				drawSkin(skinType, x, y, x1, y1, x2, y2, x3, y3, zPos, zPosNext);

				stroke(((ringOscillation + 1) * 255) * opacityMultiplier);
				drawSpikes(x, y, zPos, x1, y1, zPos, x2, y2, zPosNext);
			}

			popMatrix();
		}

		popMatrix();

		radiusOsc += radiusOscIncrement;
		waveOscX += waveIncrement;
		waveOscY += waveIncrement;

		popStyle();
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
