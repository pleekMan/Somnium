package clips.lunarDrone;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class Crater {
	Main p5;
	
	PGraphics drawLayer;

	PVector[] craterVertices; // ONlY PROFILE, NOT THE ENTIRE REVOLUTION
	float outerPlateauStart;
	float outerRampStart;
	float innerRampStart;
	float innerPlateauStart;

	PVector center;
	float heightMultiplier;
	float widthMultiplier;
	float underGroundLevel;

	int revolveResolution;

	public Crater() {
		p5 = getP5();

		center = new PVector(0, p5.height * 0.5f, 0);
		craterVertices = new PVector[50];

		for (int i = 0; i < craterVertices.length; i++) {
			craterVertices[i] = new PVector(i / (float) craterVertices.length, 0, 0);
		}

		innerPlateauStart = 0;
		innerRampStart = 0.3f;
		outerRampStart = 0.6f;
		outerPlateauStart = 0.9f;

		heightMultiplier = 10;
		widthMultiplier = 200;
		underGroundLevel = -0.0f;

		revolveResolution = 40;

		//generateProfile();

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
	
	public void setDrawLayer(PGraphics dLayer){
		drawLayer = dLayer;
	}
	
	void setCenter(float x, float y, float z) {
	    center.set(x, y, z);
	  }

	  void generateProfile() {
	    // NOT USED (CONTRUCTED AT getShape())
	    //innerRampStart = mouseX / float(width);
	    //outerRampStart = innerRampStart + 0.1;

	    // INNER PLATEAU
	    int innerPlateauVertexStart = p5.floor(craterVertices.length * 0);
	    int innerPlateauVertexEnd = p5.floor(craterVertices.length * innerRampStart);

	    for (int i=innerPlateauVertexStart; i<innerPlateauVertexEnd; i++) {
	      craterVertices[i].y = underGroundLevel;
	    }


	    // INNER RAMP = pow(x,5);
	    int innerRampVertexStart = p5.floor(craterVertices.length * innerRampStart);
	    int innerRampVertexEnd = p5.floor(craterVertices.length * outerRampStart);

	    for (int i=innerRampVertexStart; i<innerRampVertexEnd; i++) {
	      float vertexY = p5.pow( p5.map(i, innerRampVertexStart, innerRampVertexEnd, 0, 1), 5);
	      craterVertices[i].y = p5.map(vertexY, 0, 1, underGroundLevel, 1);
	      //print(map(i, innerRampVertexStart, innerRampVertexEnd, 0, 1) + " -> ");
	      //println(vertexY);
	    }

	    // OUTER RAMP = pow(x,2);
	    int outerRampVertexStart = p5.floor(craterVertices.length * outerRampStart);
	    int outerRampVertexEnd = p5.floor(craterVertices.length * outerPlateauStart);

	    for (int i=outerRampVertexStart; i<outerRampVertexEnd; i++) {
	      float vertexY = p5.pow( p5.map(i, outerRampVertexStart, outerRampVertexEnd, 1, 0), 2);
	      craterVertices[i].y = vertexY;
	      //print(map(i, innerRampVertexStart, innerRampVertexEnd, 0, 1) + " -> ");
	      //println(vertexY);
	    }

	    // OUTER PLATEAU
	    int outerPlateauVertexStart = p5.floor(craterVertices.length * outerPlateauStart);
	    int outerPlateauVertexEnd = p5.floor(craterVertices.length * 1);

	    for (int i=outerPlateauVertexStart; i<outerPlateauVertexEnd; i++) {
	      craterVertices[i].y = 0;
	    }
	  }

	  void render() {
	    // NOT USED (DIRECTLY RENDERING THE SHAPE OBJECT (WITH ALL THE CRATERS AS ADDED AS CHILDS))
	    //stroke(127, 127, 255);
	    //noStroke();
		  drawLayer.fill(255, 255, 0);

	    float angleUnit = p5.TWO_PI / revolveResolution;

	    drawLayer.pushMatrix();
	    drawLayer.translate(center.x, center.y, center.z);

	    for (int r=0; r < revolveResolution; r++) {

	    	drawLayer.pushMatrix(); 
	    	drawLayer.rotateY(angleUnit * r);

	    	drawLayer.beginShape(p5.QUADS);

	      for (int i=1; i < craterVertices.length; i++) {

	        float x = craterVertices[i].x * widthMultiplier;
	        float y =  craterVertices[i].y * -heightMultiplier;

	        drawLayer.vertex(x, y);

	        //fill(255, 255, 0);
	        drawLayer.point(x, y);
	        //line(x, y, x +1000, y);

	        //println("|" + i + "| X: " + x + " | Y: " + y);
	      }

	      drawLayer.endShape();

	      drawLayer.popMatrix();
	    }

	    drawLayer.popMatrix();
	  }

	  void setStages (float ips, float irs, float ors, float ops) {
	    setInnerPlateauStart(ips);
	    setInnerRampStart(irs);
	    setOuterRampStart(ors);
	    setOuterPlateauStart(ops);

	    generateProfile();
	  }

	  void setInnerPlateauStart(float percentStart) {
	    // percent means 0 -> 1
	    innerPlateauStart = p5.constrain(percentStart, 0 + 0.01f, innerRampStart);
	  }
	  void setInnerRampStart(float percentStart) {
	    // percent means 0 -> 1
	    innerRampStart = p5.constrain(percentStart, innerPlateauStart + 0.01f, outerRampStart - 0.01f);
	  }
	  void setOuterRampStart(float percentStart) {
	    // percent means 0 -> 1
	    outerRampStart = p5.constrain(percentStart, innerRampStart + 0.01f, outerPlateauStart - 0.01f);
	  }
	  void setOuterPlateauStart(float percentStart) {
	    // percent means 0 -> 1
	    outerPlateauStart = p5.constrain(percentStart, outerRampStart + 0.01f, 1 - 0.01f);
	  }

	  void setHeight(float h) {
	    heightMultiplier = h;
	  }
	  void setRadius(float r) {
	    widthMultiplier = r;
	  }

	  PShape getShape() {

	    PShape crater = drawLayer.createShape();

	    //noFill();
	    //stroke(127, 127, 255);
	    //fill(0, 127, 255);

	    float angleUnit = p5.TWO_PI / revolveResolution;
	    float tinyHeightOffset = p5.random(1); // TO AVOID Z-FIGHT ON OVERLAPPING CRATERS;

	    //float randomForHeights = random(0.5);
	    //float randomForHeights2 = random(0.5);

	    for (int r=0; r < revolveResolution; r++) {

	      crater.beginShape(p5.QUADS);


	      for (int i=1; i < craterVertices.length; i++) {

	        float x = craterVertices[i].x * (p5.cos(angleUnit * r));
	        float z = craterVertices[i].x * (p5.sin(angleUnit * r));
	        float y = craterVertices[i].y;// + -randomForHeights;
	        float x2 = craterVertices[i-1].x * (p5.cos(angleUnit * r));
	        float z2 = craterVertices[i-1].x * (p5.sin(angleUnit * r));
	        float y2 = craterVertices[i-1].y;// + -randomForHeights2;
	        float x3 = craterVertices[i-1].x * (p5.cos((angleUnit * r) + angleUnit));
	        float z3 = craterVertices[i-1].x * (p5.sin((angleUnit * r) + angleUnit));
	        float x4 = craterVertices[i].x * (p5.cos((angleUnit * r) + angleUnit));
	        float z4 = craterVertices[i].x * p5.sin((angleUnit * r) + angleUnit);

	        x = center.x + (x * widthMultiplier);
	        z = center.z + (z * widthMultiplier);
	        y = center.y + (y * -heightMultiplier) - tinyHeightOffset; 
	        y2 = center.y + (y2 * -heightMultiplier) - tinyHeightOffset; 


	        x2 = center.x + (x2 * widthMultiplier);
	        z2 = center.z + (z2 * widthMultiplier);
	        x3 = center.x + (x3 * widthMultiplier);
	        z3 = center.z + (z3 * widthMultiplier);
	        x4 = center.x + (x4 * widthMultiplier);
	        z4 = center.z + (z4 * widthMultiplier);

	        //float x = craterVertices[i].x * widthMultiplier;
	        //float y =  craterVertices[i].y * -heightMultiplier;

	        crater.fill(((i/(float)craterVertices.length) * 255));
	        //crater.fill(i * 10);

	        crater.vertex(x, y, z);
	        crater.vertex(x2, y2, z2);
	        crater.vertex(x3, y2, z3);
	        crater.vertex(x4, y, z4);

	        //println("|" + i + "| X: " + x + " | Y: " + y);
	      }
	      
	      //randomForHeights = randomForHeights2;
	      //randomForHeights2 = random(0.2);
	      crater.endShape();
	    }


	    return crater;
	  }
}
