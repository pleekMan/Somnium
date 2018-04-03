package clips.moonEclipse;

import processing.core.PShape;
import processing.core.PVector;
import globals.Clip;

public class MoonEclipse extends Clip {

	float moonDiameter;
	PVector moonPos;
	float eclipseTransition;
	float eclipseTransitionVel;
	boolean enableEclipseTransition;
	PShape moon;

	boolean enableBigRay;
	
	float earthSize;
	PVector earthPos;
	PVector earthVel;

	public MoonEclipse(String _rendererType) {
		super(_rendererType);
	}

	@Override
	public void load() {
		super.load();
		moonDiameter = drawLayer.height * 0.75f;
		moonPos = new PVector(drawLayer.width * 0.5f, drawLayer.height * 0.5f);
		eclipseTransition = eclipseTransitionVel = 0;
		enableEclipseTransition = false;
		
		earthSize = drawLayer.height * 0.25f;
		earthPos = new PVector(-earthSize * 0.5f, drawLayer.height * 0.5f);
		earthVel = new PVector();
		p5.imageMode(p5.CORNER);

		createMoon();

	}

	@Override
	public void update() {
		if (enableEclipseTransition) {
			if (eclipseTransition <= 1) {
				eclipseTransition += eclipseTransitionVel;
			}
		}
		
		earthPos.add(earthVel);
		
	}

	@Override
	public void render() {
		
		drawLayer.beginDraw();
		
		drawLayer.background(0);

		// BLACK MOON
		drawLayer.fill(0);
		drawLayer.noStroke();
		drawLayer.ellipse(moonPos.x, moonPos.y, moonDiameter, moonDiameter);

		// HALF MOON WHITE
		drawLayer.shape(moon);

		//image(fullMoon,moonPos.x, moonPos.y, moonDiameter, moonDiameter);

		//MOON OUTLINE
		drawLayer.noFill();
		drawLayer.stroke(127);
		drawLayer.ellipse(moonPos.x, moonPos.y, moonDiameter, moonDiameter);

		// BLACK ECLIPSE MOON
		drawLayer.pushMatrix();
		drawLayer.translate(moonPos.x, moonPos.y);
		drawLayer.scale(1 - eclipseTransition, 1);
		drawLayer.fill(0);
		drawLayer.noStroke();
		drawLayer.ellipse(0, 0, moonDiameter + 2, moonDiameter + 2);
		drawLayer.popMatrix();

		// BLACK RECT TO HIDE WHAT IS SEEN WHEN SCALING THE ELLIPSE
		//rect(width * 0.5, 0, (moonDiameter * 0.5) + 2, height);
		
		// A CROSS... yeah... just a simple cross
//		drawLayer.stroke(255,0,0);
//		drawLayer.noFill();
//		drawLayer.line(0, 0, drawLayer.width, drawLayer.height);
//		drawLayer.line(drawLayer.width, 0, 0, drawLayer.height);
		
		//EARTH 
		drawLayer.fill(50,0,255);
		drawLayer.noStroke();
		drawLayer.ellipse(earthPos.x, earthPos.y, earthSize, earthSize);
		
		drawLayer.endDraw();
		
		p5.image(drawLayer, 0, 0);

	}

	public void createMoon() {

		int moonRes = 50;
		float angleStep = p5.TWO_PI / moonRes;

		moon = p5.createShape();
		moon.beginShape();
		moon.fill(255);
		moon.noStroke();

		for (int i = 0; i < 50; i++) {
			float x = (moonDiameter * 0.5f) * p5.cos(angleStep * i);
			float y = (moonDiameter * 0.5f) * p5.sin(angleStep * i);

			x = x > 0 ? 0 : x;

			moon.vertex(x, y);
		}
		moon.endShape();

		moon.translate(drawLayer.width * 0.5f, drawLayer.height * 0.5f);
	}

	public void onKeyPressed(char key) {
		if (key == 't') {
			
		}
	}
	
	@Override
	public void recieveControllerChange(int channel, int number, int value) {
		if (channel == 0) {

		    // OSCILATION DISPLACEMENT (POSITION)
		    if (number == 0) {
		    	eclipseTransitionVel = p5.map(value, 0,127, 0,0.001f);
		    }

		    if (number == 1) {
		    	earthVel.x = p5.map(value, 0, 127, 0, 0.2f);
		    }
		    
		    if (number == 2 && value >= 127) {
		    	p5.println("||- MOON ECLIPSE: Start Eclipse");
				enableEclipseTransition = !enableEclipseTransition;		    }
		}
	}

}
