package clips.lunarDrone;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;
import processing.opengl.PShader;
import globals.Clip;

public class LunarDrone extends Clip {

	PVector cameraVel;

	LunarCamera cam;

	ArrayList<Crater> craters;
	PShape cratersFrozen;
	//boolean enableCameraControl;

	PGraphics canvasLunar;
	PShader shader;

	float lightDarkControl = 0;
	float lightDark = 0;
	float camFov;
	float camFovControlVel = 0;


	int selectedCrater = 0;
	float selectedCraterRotation;
	PVector selectedCraterVel;

	public LunarDrone(String _rendererType) {
		super(_rendererType);
	}

	@Override
	public void load() {
		super.load();

		p5.noStroke();
		p5.fill(255, 255, 0);
		p5.strokeWeight(1);
		p5.textureMode(p5.NORMAL);
		p5.imageMode(p5.CORNER);

		canvasLunar = p5.createGraphics(drawLayer.width, drawLayer.height, p5.P3D);
		canvasLunar.beginDraw();
		canvasLunar.clear();
		canvasLunar.endDraw();

		shader = p5.loadShader("invert.glsl");

		cameraVel = new PVector(0, 0, -0.3f);

		cam = new LunarCamera();
		cam.setDrawLayer(drawLayer);

		craters = new ArrayList<Crater>();
		cratersFrozen = canvasLunar.createShape(p5.GROUP);

		for (int i = 0; i < 100; i++) {
			createNewCrater();
		}

		//enableCameraControl = true;
		lightDarkControl = 0;

		//vertShader = loadShader("frags.glsl", "verts.glsl");
		//shader(vertShader);

		selectedCrater = 0;
		selectedCraterRotation = 0;
		selectedCraterVel = new PVector();

	}

	@Override
	public void render() {

		//background(255 - (255 * lightDarkControl));

		//if (frameCount % 15 == 0)frame.setTitle("FPS: " + frameRate);

		//lights();

		//camera.dolly(cameraVel.z);

		/*
		for (int i=0; i < craters.size (); i++) {
		 craters.get(i).render();
		 }
		 */

		// CAM TRAJECTORY
		//stroke(255, 255, 0);
		//line(0, -1, 0, 0, -1, -10000);

		//lightDarkControl = p5.norm(p5.mouseX, 0, p5.width);
		lightDark = lightDarkControl;
		cam.update();
		cam.render();

		//FOV control
		//float fov = p5.PI/3.0f;
		//float camFov = p5.map(p5.mouseY, 0, p5.height, 0.1f, p5.TWO_PI);
		
		//camFov += camFovControlVel;
		//camFov = p5.constrain(camFov, 0.1f, p5.PI);
		
		float camZ = (p5.height / 2.0f) / p5.tan(camFov / 2.0f);
		canvasLunar.perspective(camFov, p5.width / (float) p5.height, camZ * 0.1f, camZ * 10);

		// CAM
		canvasLunar.camera(cam.camPosition.x, cam.camPosition.y, cam.camPosition.z, cam.target.x, cam.target.y, cam.target.z, 0, 1, 0);

		//drawGround();

		// SELECTED CRATER SHIT
		//canvasLunar.pushMatrix();
		PShape selectedCraterShape = cratersFrozen.getChild(selectedCrater);
		selectedCraterShape.rotateZ(selectedCraterRotation);
		//selectedCraterShape.translate(selectedCraterVel.x, selectedCraterVel.z);
		//canvasLunar.popMatrix();

		canvasLunar.beginDraw();
		//canvasLunar.background(255 - (255 * lightDarkControl));

		if (lightDark < 0.5f) {
			canvasLunar.background(255);
		}

		//canvasLunar.background(0,255,0);

		canvasLunar.shape(cratersFrozen);

		//canvasLunar.fill(255, 0, 0);
		//canvasLunar.rect(10, 10, 800, 800);

		//canvasLunar.filter(shader);
		canvasLunar.endDraw();

		// DO STUFF ON EACH CRATER
		/*
		stroke(255);
		 //fill(255, 255, 0);
		 for (int i=0; i<cratersFrozen.getChildCount (); i++) {
		 PVector shapePos = cratersFrozen.getChild(i).getVertex(0);
		 
		 
		 pushMatrix();
		 translate(shapePos.x, shapePos.y, shapePos.z);
		 //text(i,0,0);
		 box(50);
		 
		 //line(cam.target.x, cam.target.y, cam.target.z,shapePos.x, shapePos.y, shapePos.z);
		 popMatrix();
		 }
		 */

		drawLayer.beginDraw();
		drawLayer.background(0);

		shader.set("resolution", (float) drawLayer.width, (float) drawLayer.height);
		shader.set("multiplier", lightDark);

		//drawLayer.shader(shader);

		drawLayer.image(canvasLunar, 0, 0);

		drawLayer.resetShader();

		//hint(DISABLE_DEPTH_TEST);
		//showFPS();
		//hint(ENABLE_DEPTH_TEST);

		//camera.feed();
		drawLayer.endDraw();

		p5.pushMatrix();
		//p5.translate(p5.width * 0.5f, p5.height * 0, 5f);
		p5.image(drawLayer, 0, 0);
		p5.popMatrix();
	}

	void createNewCrater() {
		Crater crater = new Crater();

		//crater.setDrawLayer(canvasLunar);
		crater.setCenter(p5.random(-2000, 500), 0, p5.random(-10000, 0));
		//crater.setCenter(0,0,0);
		crater.setStages(p5.random(0, 0.25f), p5.random(0.25f, 0.5f), p5.random(0.5f, 0.75f), p5.random(0.75f, 1));
		crater.setRadius(p5.random(10, 300));
		crater.setHeight(p5.random(3, 20));

		craters.add(crater);

		cratersFrozen.addChild(crater.getShape());
	}

	void drawGround() {

		drawLayer.stroke(255);
		drawLayer.beginShape();
		drawLayer.fill(255, 255, 0);
		drawLayer.vertex(-1000, 0, 0);
		drawLayer.vertex(1000, 0, 0);
		drawLayer.fill(255, 0, 255);
		drawLayer.vertex(1000, 0, -2000);
		drawLayer.vertex(-1000, 0, -2000);
		drawLayer.endShape(p5.CLOSE);
	}

	void showFPS() {
		drawLayer.hint(p5.DISABLE_DEPTH_TEST);
		drawLayer.textMode(p5.MODEL);
		drawLayer.textAlign(p5.LEFT);
		//pushMatrix();
		//translate(0, 0);
		drawLayer.noStroke();
		drawLayer.fill(0, 0, 100, 100);
		drawLayer.rect(0, 0, 100, 20);
		drawLayer.fill(255);
		drawLayer.text("FPS: " + p5.nf(p5.frameRate, 2, 2), 10, 20);
		//popMatrix();
		drawLayer.hint(p5.ENABLE_DEPTH_TEST);
	}

	void freakOutCrater(int craterIndex) {

		selectedCrater = craterIndex;
		selectedCraterRotation = p5.random(p5.QUARTER_PI);
		selectedCraterVel.set(p5.random(2), p5.random(2), p5.random(2));

		int[] craterColors = { p5.color(0, 0, 155, 127), p5.color(10, 0, 224, 127), p5.color(100, 10, 255, 127) };

		PShape crater = cratersFrozen.getChild(craterIndex);
		crater.setFill(craterColors[p5.floor(p5.random(craterColors.length))]);

		int vertexCountToFreak = p5.floor(crater.getVertexCount() * p5.random(0.5f));

		for (int i = 0; i < vertexCountToFreak; i++) {
			int randomVertex = p5.floor(p5.random(crater.getVertexCount()));
			crater.setVertex(randomVertex, crater.getVertex(randomVertex).x, p5.random(-20, -2000), crater.getVertex(randomVertex).z);
		}

	}

	@Override
	public void onKeyPressed(char key) {
		if (key == 'z') {
			//enableCameraControl = !enableCameraControl;
			//camera.setActive(enableCameraControl);
		}

		if (key == 'x') {

		}
	}

	@Override
	public void recieveControllerChange(int channel, int number, int value) {
		if (channel == 0) {

			// CAM FOV
			if (number == 0) {
				/*
				camFovControlVel = p5.map(value, 0, 127, -0.1f, 0.1f);
				if(camFovControlVel > -0.001f && camFovControlVel < 0.001f){
					camFovControlVel = 0;
				}
				*/
				
				camFov = p5.map(value, 0, 127, p5.PI, 1.0f);
				
				//p5.println("-|| LUNAR DRONE : Cam FOV VEL = " + camFovControlVel);
				p5.println("-|| LUNAR DRONE : Cam FOV = " + camFov);
				//p5.println("-||");


			}

			// CAM VELOCITY
			if (number == 1) {
				cam.setVelocity(p5.map(value, 0, 127, 0, 1));
				p5.println("-|| LUNAR DRONE : Cam Velocity = " + cam.masterTransformVel.z);
			}
			
			if (number == 2) {
				int randomCrater = p5.floor(p5.random(craters.size()));
				freakOutCrater(randomCrater);
				p5.println("-|| LUNAR DRONE : Freaking Crater # = " + randomCrater);
			}
		}
		if (channel == 1) {
			if (number == 1) {
				// CAM ALTITUDE
				
				cam.setAltitude(p5.map(value, 0, 127, -3, 3));
				p5.println("-|| LUNAR DRONE : AltitudeVel = " + cam.altitudeControlVel);
				p5.println("-|| LUNAR DRONE : Altitude = " + cam.camOffset.y);
				p5.println("-||");

			}
		}

		// CAM ALTITUDE
		if (channel == 2) {
			if (number == 1) {
				// LIGHT DARK CONTROL
				lightDarkControl = p5.map(value, 0, 127, 0, 1);
				p5.println("-|| LUNAR DRONE : LIGHT | DARK = " + lightDarkControl);
			}
		}

	}
}
