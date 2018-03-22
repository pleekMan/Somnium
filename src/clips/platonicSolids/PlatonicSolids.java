package clips.platonicSolids;

import java.util.ArrayList;

import globals.Clip;
//import clips.platonicSolids.*;

public class PlatonicSolids extends Clip {

	ArrayList<Solid> solids;
	final int SOLID_COUNT = 4;

	//PImage backPaper;
	int writeColor;

	//Sun sun;
	//PImage sunImage;
	boolean showSun;

	float camX, camY, camZ;
	float camAnim;
	float camAnimIncrement;
	float maxCamRadius;

	int atStage;

	public PlatonicSolids(String rendererType) {
		super(rendererType);
	}

	@Override
	public void load() {
		super.load();
		drawLayer.textureMode(p5.NORMAL);
		//stroke(0,0,255);
		drawLayer.noStroke();
		//fill(0,255,0);
		//strokeWeight(1);

		//backPaper = loadImage("paper_2.jpg");
		//writeColor = color(75, 75, 60);
		writeColor = drawLayer.color(250);

		//sunImage = loadImage("sun.jpg");
		//sun = new Sun(sunImage);
		showSun = true;

		camX = camY = camZ = 0;
		camAnim = 0;
		camAnimIncrement = 0.01f;
		maxCamRadius = 500;

		solids = new ArrayList<Solid>();

		initScene();

		atStage = 0;
		
		p5.imageMode(p5.CORNER);
	}
	
	void initScene() {

		atStage = 0;

		createSolids(4);
		for (Solid solido : solids) {
			solido.growVel = 1.005f;
			solido.hasFill = false;
			solido.hasStroke = true;
			solido.maxAbsoluteScale = 200;
			//solido.setTexture(sunImage);
		}
	}
	
	@Override
	public void render() {
		//background(backPaper);
		drawLayer.beginDraw();
		drawLayer.background(0);
		
		if (!showSun) {
			drawLayer.background(0);
		} else {
			drawLayer.fill(0, 10);
			drawLayer.resetMatrix();
			drawLayer.rect(0, 0, drawLayer.width, drawLayer.height);
		}
		drawLayer.lights();

		//camX = map(mouseX, 0, width, -500, 500);
		//camY = map(mouseY, 0, height, 500, -500);
		camX = p5.cos(camAnim) * maxCamRadius;
		camY = p5.sin(camAnim) * maxCamRadius;
		drawLayer.camera(camX, camY, 500, 0, 0, 0, 0, 1, 0);
		camAnim += camAnimIncrement;
		//camera(0, -50, -100, 0, 0, 0, 0, 1, 0);

		//fill(250, 0, 0);
		drawLayer.noFill();
		//strokeWeight(0.08);
		//strokeJoin(MITER);
		//stroke(0, 0, 255);
		//noStroke();

		//sphere(100);

		if (atStage == 1) {
			if (p5.frameCount % 5 == 0) {
				for (Solid solido : solids) {
					solido.maxAbsoluteScale = p5.floor(p5.random(100, 500));
				}
			}
		}

		for (int i = 0; i < solids.size(); i++) {
			Solid actualSolid = solids.get(i);

			actualSolid.update();
			actualSolid.setLineWeight((1 - actualSolid.scale) * actualSolid.maxLineWeight);
			actualSolid.setOpacity((1 - actualSolid.scale) * 255);
			//if (actualSolid.visible) {
			actualSolid.render();
			//}
		}

		// MISC
		if (p5.random(1) > 0.9) {
			//int randomSolid = floor(random(solids.size() - 0.01));
			//Solid solido = solids.get(randomSolid);

			drawLayer.noFill();
			drawLayer.strokeWeight(1);

			//PVector[] vertices = solido.get2RandomVertex();
			float lineLimit = 2000;
			drawLayer.stroke(writeColor);
			drawLayer.line(p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit));
			drawLayer.line(p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit));

			float diameter = p5.random(-lineLimit, lineLimit);
			drawLayer.ellipse(p5.random(-lineLimit, lineLimit), p5.random(-lineLimit, lineLimit), diameter, diameter);
		}

		//hint(DISABLE_DEPTH_TEST);
		
		// SUN
		/*
		if (showSun) {
			//resetMatrix();
			sun.setColor(color(random(255), random(255), random(255)));
			sun.render();
		}
		*/
		
		
		
		//showSun = false;

		//hint(ENABLE_DEPTH_TEST);

		//drawAxisGizmo();

		//hint(DISABLE_DEPTH_TEST);
		//text(solids.get(0).maxAbsoluteScale, 20, 20);
		//hint(ENABLE_DEPTH_TEST);

		//noLoop();
		
		drawLayer.endDraw();
		p5.image(drawLayer, 0, 0);
	}

	void createSolids(int count) {

		//int futureSolidCount = solids.size() + count;
		//println("Solids Count: " + solids.size());
		for (int i = 0; i < count; i++) {
			Solid newSolid;
			if (i % 4 == 0) {
				newSolid = new Solid_Cube();
			} else if (i % 4 == 1) {
				newSolid = new Solid_Tetrahedron();
			} else if (i % 4 == 2) {
				newSolid = new Solid_Octahedron();
			} else {
				newSolid = new Solid_Dodecahedron();
			}

			newSolid.setPosition(0, 0, 0);
			newSolid.setScale(p5.random(0.001f, 0.05f));
			//newSolid.setScale( 2000.0 / (i+1));
			//newSolid.setColor((int)(i * (200.0 / futureSolidCount)));
			newSolid.setColor(writeColor);
			newSolid.setLineWeight(0.08f);
			//newSolid.setMaxAbsoluteScale(solids.get(0).maxAbsoluteScale * 0.5);
			newSolid.setDrawLayer(drawLayer);
			
			solids.add(newSolid);
		}
	}

	@Override
	public void onKeyPressed(char key) {
		//redraw();

		if (key == 'q') {
			for (int i = 0; i < solids.size(); i++) {
				solids.get(i).maxAbsoluteScale += 100;
			}
			//println("Last Scale: " + solids.get(solids.size() - 1 ).maxAbsoluteScale);
		}

		if (key == 'a') {
			for (int i = 0; i < solids.size(); i++) {
				solids.get(i).maxAbsoluteScale -= 100;
			}
			//println("Last Scale: " + solids.get(solids.size() - 1).maxAbsoluteScale);
		}
		if (key == 'f') {
			for (Solid solido : solids) {
				solido.hasFill = !solido.hasFill;
			}
		}
		if (key == 's') {
			for (Solid solido : solids) {
				solido.hasStroke = !solido.hasStroke;
			}
		}

		if (key == 'z') {
			atStage = 0;

			for (Solid solido : solids) {
				solido.growVel = 1.005f;
				solido.hasFill = false;
				solido.hasStroke = true;
				solido.maxAbsoluteScale = 200;
			}
		}

		if (key == 'x') {
			atStage = 1;

			createSolids(5);

			for (Solid solido : solids) {
				solido.growVel = 1.08f;
				solido.hasFill = true;
				solido.hasStroke = true;
				solido.maxAbsoluteScale = 400;
				solido.setLineWeight(10);
				solido.setColor(drawLayer.color(p5.random(255)));
			}
		}

		if (key == 'c') {
			atStage = 2;

			createSolids(5);

			for (Solid solido : solids) {
				solido.growVel = 1.1f;
				solido.hasFill = false;
				solido.hasStroke = true;
				solido.maxAbsoluteScale = 1000;
				solido.setLineWeight(40);
			}
		}
	}

}
