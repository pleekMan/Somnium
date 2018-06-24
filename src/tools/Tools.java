package tools;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Tools {
	static Main p5 = PAppletSingleton.getInstance().getP5Applet();
	
	public Tools(){
	}

	static public float distanceBetween(PVector pos1, PVector pos2) {
		return p5.dist(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
	}

	static public void drawAxisGizmo(){
		drawGizmo(0f,0f,0f,50f);
	}

	static public void drawAxisGizmo(float size){
		drawGizmo(0f,0f,0f,size);
	}
	static public void drawGizmo(float xPos, float yPos, float zPos, float gizmoSize) {

		p5.pushMatrix();
		p5.translate(xPos, yPos, zPos);

		p5.noFill();
		p5.box(gizmoSize * 0.05f);

		// X
		p5.fill(255, 0, 0);
		p5.stroke(255, 0, 0);
		p5.line(0, 0, 0, gizmoSize, 0, 0);
		// box(100);

		// Y
		p5.fill(0, 255, 0);
		p5.stroke(0, 255, 0);
		p5.line(0, 0, 0, 0, gizmoSize, 0);

		// Z
		p5.fill(0, 0, 255);
		p5.stroke(0, 0, 255);
		p5.line(0, 0, 0, 0, 0, gizmoSize);

		p5.popMatrix();
	}

	static public void drawMouseCoordinates() {
		// MOUSE POSITION
		p5.fill(255, 0, 0);
		p5.text("FR: " + p5.frameRate, 20, 20);
		p5.text("X: " + p5.mouseX + " / Y: " + p5.mouseY, p5.mouseX, p5.mouseY);
	}
	
	static public void drawBackLines() {
		p5.stroke(200);
		float offset = p5.frameCount % 40;
		for (int i = 0; i < p5.width; i += 40) {
			p5.line(i + offset, 0, i + offset, p5.height);
		}
	}
	
	public static void translate(PVector p){
		p5.translate(p.x, p.y, p.z);
	}
	
	public static PVector lerpPVector(PVector a, PVector b, float amount){
		return new PVector(p5.lerp(a.x, b.x, amount),p5.lerp(a.y, b.y, amount),p5.lerp(a.z, b.z, amount));
	}



}
