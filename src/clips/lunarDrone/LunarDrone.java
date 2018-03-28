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
	boolean enableCameraControl;

	PGraphics canvasLunar;
	PShader shader;

	float lightDarkControl = 0;

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

		enableCameraControl = true;

		//vertShader = loadShader("frags.glsl", "verts.glsl");
		//shader(vertShader);

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

		lightDarkControl = p5.norm(p5.mouseX, 0, p5.width);

		cam.update();
		cam.render();

		//FOV control
		float camFov = p5.map(p5.mouseY, 0, p5.height, 0.1f, p5.TWO_PI);
		float camZ = (p5.height/2.0f) / p5.tan(camFov/2.0f);
		canvasLunar.perspective(camFov, p5.width/(float)p5.height, camZ * 0.1f, camZ * 10);

		// CAM
		canvasLunar.camera(cam.camPosition.x, cam.camPosition.y, cam.camPosition.z, cam.target.x, cam.target.y, cam.target.z, 0, 1, 0);

		//drawGround();

		canvasLunar.beginDraw();
		//canvasLunar.background(255 - (255 * lightDarkControl));
		
		
		if (p5.mouseX < p5.width * 0.75f) {
			canvasLunar.background(0);
		}
		
		
		//canvasLunar.background(0,255,0);
		
		
		
		canvasLunar.shape(cratersFrozen);

		//canvasLunar.fill(255,0,0);
		//canvasLunar.rect(10,10,800,800);

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
		shader.set("multiplier", p5.mouseX / (float) drawLayer.width);

		drawLayer.shader(shader);

		drawLayer.image(canvasLunar, 0, 0);

		drawLayer.resetShader();

		//hint(DISABLE_DEPTH_TEST);
		//showFPS();
		//hint(ENABLE_DEPTH_TEST);

		//camera.feed();
		drawLayer.endDraw();

		p5.pushMatrix();
		p5.translate(p5.width * 0.5f, p5.height * 0, 5f);
		p5.image(drawLayer, 0, 0);
		p5.popMatrix();
	}

	void createNewCrater() {
		Crater crater = new Crater();

		//crater.setDrawLayer(canvasLunar);
		crater.setCenter(p5.random(-2000, 200), 0, p5.random(-10000, 0));
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

	@Override
	public void onKeyPressed(char key) {
		if (key == 'z') {
			enableCameraControl = !enableCameraControl;
			//camera.setActive(enableCameraControl);
		}

		if (key == 'x') {
			int randomCrater = p5.floor(p5.random(craters.size()));
			//craters.get(randomCrater)..gsetFill(color(255));
			cratersFrozen.getChild(randomCrater).setFill(p5.color(255, 0, 0));
		}
	}
}
