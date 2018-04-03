package clips.platonicSolids;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PImage;

public class Sun {
	Main p5;

	float x, y;
	float velX, velY;
	float scale;
	float scaleIncrement;

	int tintColor; // color

	PImage sunImage;

	PGraphics drawLayer;

	Sun(PImage _image) {
		p5 = getP5();
		sunImage = _image;

		x = 0;//random(width);
		y = 0;//random(height);
		scale = 0.01f;
		scaleIncrement = 1.2f;

		velX = p5.random(p5.random(-30, -20), p5.random(20, 30));
		velY = p5.random(p5.random(-30, -20), p5.random(20, 30));
		tintColor = p5.color(255);
	}

	public void setDrawLayer(PGraphics dLayer) {
		drawLayer = dLayer;
	}

	void render() {

		drawLayer.imageMode(p5.CENTER);
		drawLayer.blendMode(p5.ADD);
		//drawLayer.tint(tintColor);

		drawLayer.pushMatrix();
		drawLayer.translate(drawLayer.width * 0.5f, drawLayer.height * 0.5f);
		drawLayer.image(sunImage, 0, 0, sunImage.width * scale, sunImage.height * scale);
		drawLayer.popMatrix();

		drawLayer.imageMode(p5.CORNER);

		//scale *= scaleIncrement;
	}
	
	void renderOld() {

		x += velX;
		y += velY;

		drawLayer.imageMode(p5.CENTER);
		drawLayer.blendMode(p5.ADD);
		drawLayer.tint(tintColor);

		drawLayer.pushMatrix();
		drawLayer.translate(x, y, x);
		drawLayer.image(sunImage, 0, 0, sunImage.width * scale, sunImage.height * scale);
		drawLayer.popMatrix();

		drawLayer.imageMode(p5.CORNER);

		scale *= scaleIncrement;
	}

	void setVelocity(float x, float y) {
		velX = x;
		velY = y;
	}

	void setPosition(float _x, float _y) {
		x = _x;
		y = _y;
	}

	void setScale(float s) {
		scale = s;
	}

	void setColor(int _c) {
		tintColor = _c;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
