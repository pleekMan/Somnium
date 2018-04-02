package clips.lunarDrone;

import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class LunarCamera {
	Main p5;
	PVector masterTransform, masterTransformVel;
	PVector target;
	PVector camOffset;
	PVector camPosition;

	PGraphics drawLayer;

	public LunarCamera() {
		p5 = getP5();

		masterTransform = new PVector();
		masterTransformVel = new PVector();
		target = new PVector();
		camPosition = new PVector();
		camOffset = new PVector(200, -150, 100);
	}

	void update() {
		masterTransform.add(masterTransformVel);

		camPosition.set(PVector.add(masterTransform, camOffset));
		target.set(masterTransform);
	}

	void render() {

		drawLayer.noFill();
		drawLayer.stroke(0, 255, 0);

		//MASTER
		drawLayer.pushMatrix();
		drawLayer.translate(masterTransform.x, masterTransform.y, masterTransform.z);
		drawLayer.box(50);
		drawLayer.popMatrix();

		//TARGET
		drawLayer.pushMatrix();
		drawLayer.translate(target.x, target.y, target.z);
		drawLayer.box(20);
		drawLayer.popMatrix();

		//CAMERA
		drawLayer.pushMatrix();
		drawLayer.translate(camPosition.x, camPosition.y, camPosition.z);
		//sphere(20);
		drawLayer.popMatrix();

		drawLayer.line(camPosition.x, camPosition.y, camPosition.z, target.x, target.y, target.z);
	}
	
	public void setDrawLayer(PGraphics dLayer){
		drawLayer = dLayer;
	}

	public void setAltitude(float alt) {
		camOffset.y = alt;
	}
	
	public void setVelocity(float forwardVel){
		masterTransformVel.set(0, 0, -forwardVel);
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
