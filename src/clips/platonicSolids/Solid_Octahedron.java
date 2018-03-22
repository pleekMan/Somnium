package clips.platonicSolids;

import processing.core.PVector;

public class Solid_Octahedron extends Solid {
	PVector[] vertex;

	Solid_Octahedron() {
		super();

		vertex = new PVector[6];
		vertex[0] = new PVector(0, 1, 0);
		vertex[1] = new PVector(0, -1, 0);
		vertex[2] = new PVector(1, 0, 0);
		vertex[3] = new PVector(0, 0, 1);
		vertex[4] = new PVector(-1, 0, 0);
		vertex[5] = new PVector(0, 0, -1);
	}

	PVector[] get2RandomVertex() {
		PVector[] vertices = { vertex[p5.floor(vertex.length - 0.1f)], vertex[p5.floor(vertex.length - 0.1f)] };
		return vertices;
	}

	void render() {
		super.render();

		//fill(0,0,255);
		drawLayer.pushMatrix();
		drawLayer.translate(x, y, z);

		//pushMatrix();  
		//rotateX(mouseX/100.);
		//rotateY(mouseY/100.);

		//scale((float)mouseX / width * 100); //vertex size 
		drawLayer.scale(scale * maxAbsoluteScale);

		drawLayer.beginShape(p5.TRIANGLE_FAN);
		drawLayer.vertex(vertex[1].x, vertex[1].y, vertex[1].z);

		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);
		drawLayer.vertex(vertex[3].x, vertex[3].y, vertex[3].z);
		drawLayer.vertex(vertex[4].x, vertex[4].y, vertex[4].z);
		drawLayer.vertex(vertex[5].x, vertex[5].y, vertex[5].z);
		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);//loop vertex
		drawLayer.endShape();

		drawLayer.beginShape(p5.TRIANGLE_FAN);
		drawLayer.vertex(vertex[0].x, vertex[0].y, vertex[0].z);

		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);
		drawLayer.vertex(vertex[3].x, vertex[3].y, vertex[3].z);
		drawLayer.vertex(vertex[4].x, vertex[4].y, vertex[4].z);
		drawLayer.vertex(vertex[5].x, vertex[5].y, vertex[5].z);
		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);//loop vertex
		drawLayer.endShape();

		//popMatrix();

		drawLayer.popMatrix();
	}
}
