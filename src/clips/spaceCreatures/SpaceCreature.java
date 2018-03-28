package clips.spaceCreatures;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PVector;
import globals.Clip;

public class SpaceCreature extends Clip {

	//PeasyCam cam;

	PVector seaSize;
	float creatureCount = 20;
	Creature bicho;

	FlowCloud seaFlow;
	Surface moonSurface;

	ArrayList<Creature> bichos;
	int bichosStartCounter = 0;

	int colorPairs[][]; // colors

	// INTERACTIVITY AND TRIGGERS
	boolean enableColorDimer = false;
	boolean enableSpikesLength = false;

	float camDistance;
	float camAngle, camAngleIncrement;

	public SpaceCreature(String _rendererType) {
		super(_rendererType);
	}

	@Override
	public void load() {
		super.load();
		
		seaSize = new PVector(drawLayer.width,drawLayer.height);

		p5.imageMode(p5.CORNER);
		//cam = new PeasyCam(p5, 3000);
		//cam.setMinimumDistance(20);
		//cam.setMaximumDistance(5000);

		generateColorPairs();

		// EL BICHO CENTRAL, EL PRIMERO
		bicho = new Creature();
		bicho.setDrawLayer(drawLayer);
		bicho.setPosition(new PVector());
		bicho.setOscillation(p5.random(100), p5.random(0.1f));
		bicho.setSize(p5.random(10, 20), p5.random(21, 100));
		bicho.setSkinType(p5.floor(p5.random(2.99f)));
		int randomColorPair = p5.floor(p5.random(colorPairs.length));
		bicho.setColors(colorPairs[randomColorPair][0], colorPairs[randomColorPair][1]);
		//println("-|| Color Pair: " + randomColorPair);

		bichos = new ArrayList<Creature>();

		for (int i = 0; i < creatureCount; i++) {
			PVector spawnPosition = new PVector(p5.random(-seaSize.x, seaSize.x), p5.random(-seaSize.y * 0.1f, seaSize.y * 0.1f), p5.random(-seaSize.z, seaSize.z));

			Creature newBicho = new Creature();
			newBicho.setDrawLayer(drawLayer);
			newBicho.setPosition(spawnPosition);
			newBicho.setOscillation(p5.random(100), p5.random(0.1f));
			newBicho.setSize(p5.random(10, 20), p5.random(21, 100));
			newBicho.setSkinType(0);
			randomColorPair = p5.floor(p5.random(colorPairs.length));
			newBicho.setColors(colorPairs[randomColorPair][0], colorPairs[randomColorPair][1]);

			bichos.add(newBicho);
		}

		setPrimaryBicho();

		seaFlow = new FlowCloud(new PVector(seaSize.x, seaSize.y, seaSize.z));
		seaFlow.setDrawLayer(drawLayer);
		seaFlow.setColorPair(colorPairs[0][0], colorPairs[0][1]);

		moonSurface = new Surface(seaSize.x);
		moonSurface.setDrawLayer(drawLayer);

		camDistance = 100;
		camAngle = 0;
		camAngleIncrement = 0.001f;
	}

	@Override
	public void render() {

		drawLayer.beginDraw();

		drawLayer.background(0);

		cameraProcedures();

		//drawLayer.box(100);

		/*
		cam.beginHUD();
		 fill(255, 255, 0);
		 drawMouseCoordinates();  
		 fill(0, 3);
		 rect(0, 0, width, height);
		 println(frameRate);
		 cam.endHUD();
		 */

		// INTERACTIVITY AND TRIGGERS. Also at keyPressed
		if (enableColorDimer) {
			for (int i = 0; i < bichos.size(); i++) {
				bichos.get(i).opacityMultiplier = p5.map(p5.mouseY, p5.height, 0, 0, 1);
			}
		}
		if (enableSpikesLength) {
			for (int i = 0; i < bichos.size(); i++) {
				bichos.get(i).spikesLengthMultiplier = p5.map(p5.mouseY, p5.height, 0, 0, 5);
			}
		}

		//drawAxisGizmo(0, 0, 0, 100);

		drawLayer.tint(p5.map(p5.mouseY, p5.height, 0, 0, 255));
		moonSurface.render();
		seaFlow.render();

		drawLayer.tint(255);

		bicho.render();

		drawLayer.pushMatrix();
		drawLayer.translate(0, 0, -seaSize.x);
		for (int i = 0; i < bichos.size(); i++) {
			bichos.get(i).update();
			bichos.get(i).render();
		}
		drawLayer.popMatrix();

		//drawGroundPlane();
		//drawSceneBoundingBox();

		drawLayer.endDraw();

		p5.image(drawLayer, 0, 0);
	}

	public void cameraProcedures() {
		float camX = camDistance * p5.cos(camAngle);
		float camZ = camDistance * p5.sin(camAngle);

		drawLayer.camera(camX, p5.mouseY * -10, camZ, 0, 0, 0, 0, 1, 0); // (Eye, Center, AxisAlign)

		camDistance += 0.5f;
		//camAngle += camAngleIncrement;
		camAngle = p5.map(p5.mouseX, 0, p5.width, 0, p5.TWO_PI);
	}

	public void generateColorPairs() {
		colorPairs = new int[4][2];

		colorPairs[0][0] = p5.color(200, 200, 0);
		colorPairs[0][1] = p5.color(0, 200, 200);
		colorPairs[1][0] = p5.color(200, 30, 0);
		colorPairs[1][1] = p5.color(255, 240, 0);
		colorPairs[2][0] = p5.color(0, 127, 255);
		colorPairs[2][1] = p5.color(255, 0, 160);
		colorPairs[3][0] = p5.color(235, 0, 200);
		colorPairs[3][1] = p5.color(120, 255, 167);
	}

	public void setPrimaryBicho() {

		Creature firstBicho = bichos.get(0);
		firstBicho.setPosition(new PVector());
		firstBicho.setOscillation(p5.random(10), 0.05f);
		firstBicho.setSize(15, 80);
		firstBicho.setSkinType(0);
		int randomColorPair = p5.floor(p5.random(colorPairs.length));
		firstBicho.setColors(colorPairs[randomColorPair][0], colorPairs[randomColorPair][1]);
		firstBicho.trigger();
		firstBicho.opacityMultiplier = 0;
	}

	void drawGroundPlane() {
		// GROUND PLANE

		drawLayer.pushMatrix();
		drawLayer.translate(0, seaSize.x * 0.6f, 0);
		drawLayer.rotateX(p5.HALF_PI);
		drawLayer.rectMode(p5.CENTER);
		drawLayer.fill(0);
		drawLayer.rect(0, 0, seaSize.x * 10, seaSize.y * 10);
		drawLayer.popMatrix();
	}

	void drawSceneBoundingBox() {
		// SCENE BOUNDING BOX
		drawLayer.noFill();
		drawLayer.stroke(50);
		drawLayer.box(seaSize.x * 2);
	}

	public void drawAxisGizmo(PVector position, float size) {
		drawAxisGizmo(position.x, position.y, position.z, size);
	}

	public void drawAxisGizmo(float xPos, float yPos, float zPos, float gizmoSize) {

		drawLayer.strokeWeight(1);

		drawLayer.pushMatrix();
		drawLayer.translate(xPos, yPos, zPos);

		drawLayer.noFill();
		drawLayer.box(gizmoSize * 0.05f);
		// X
		drawLayer.fill(255, 0, 0);
		drawLayer.stroke(255, 0, 0);
		drawLayer.line(0, 0, 0, gizmoSize, 0, 0);
		//drawLayer.box(100);
		// Y
		drawLayer.fill(0, 255, 0);
		drawLayer.stroke(0, 255, 0);
		drawLayer.line(0, 0, 0, 0, gizmoSize, 0);
		// Z
		drawLayer.fill(0, 0, 255);
		drawLayer.stroke(0, 0, 255);
		drawLayer.line(0, 0, 0, 0, 0, gizmoSize);

		drawLayer.popMatrix();
	}

	public void drawMouseCoordinates() {
		// MOUSE POSITION
		p5.fill(255, 255, 0);
		p5.text("FR: " + p5.frameRate, 20, 20);
		p5.text("X: " + p5.mouseX + " | Y: " + p5.mouseY, p5.mouseX, p5.mouseY);
	}

	@Override
	public void onKeyPressed(char key) {

		if (key == 'f') {
			bichos.get(p5.floor(p5.random(bichos.size()))).radiusMaxMultiplier = p5.random(8, 15);
		}

		if (key == 's') {
			for (int i = 0; i < bichos.size(); i++) {
				bichos.get(i).spikesWidthMultiplier = 50;
			}
		}

		if (key == 'c') {
			enableColorDimer = !enableColorDimer;
		}
		if (key == 'x') {
			enableSpikesLength = !enableSpikesLength;
		}

		if (key == 'w') {
			int randomBicho = p5.floor(p5.random(bichos.size()));
			bichos.get(randomBicho).swimMultiplier = 5;
			bichos.get(randomBicho).radiusMaxMultiplier = p5.random(8, 15);
		}

		if (key == 'p') {
			int randomColor = p5.floor(p5.random(colorPairs.length * 2));
			int selected = colorPairs[p5.floor(randomColor * 0.5f)][randomColor % 2];
			seaFlow.setColorPair(seaFlow.colorPair[1], selected);
		}

		if (key == ' ') {
			p5.println("-|| Unhiding next Bicho");
			bichos.get(bichosStartCounter % bichos.size()).trigger();
			bichosStartCounter++;
		}

		if (key == '3') {
			moonSurface.trigger();
		}
	}

}
