package clips.platonicSolids;

import processing.core.PVector;

public class Solid_Cube extends Solid {

	PVector[] vertex;

	public Solid_Cube() {
		super();

		vertex = new PVector[8];
		vertex[0] = new PVector(-1, -1, 1);
		vertex[1] = new PVector(1, -1, 1);
		vertex[2] = new PVector(1, 1, 1);
		vertex[3] = new PVector(-1, 1, 1);
		vertex[4] = new PVector(1, -1, -1);
		vertex[5] = new PVector(-1, -1, -1);
		vertex[6] = new PVector(-1, 1, -1);
		vertex[7] = new PVector(1, 1, -1);
	}

	PVector[] get2RandomVertex() {
		PVector[] vertices = { vertex[p5.floor(vertex.length - 0.1f)], vertex[p5.floor(vertex.length - 0.1f)] };
		return vertices;
	}

	void render() {

		super.render();
		//fill(255,0,0);

		drawLayer.pushMatrix();
		drawLayer.translate(x, y, z);

		//pushMatrix();  
		//rotateX(mouseX/100.);
		//rotateY(mouseY/100.);

		//scale((float)mouseX / width * 100); //vertex size 
		drawLayer.scale(scale * maxAbsoluteScale);

		drawLayer.beginShape(p5.QUADS);
		//front z
		drawLayer.vertex(vertex[0].x, vertex[0].y, vertex[0].z);
		drawLayer.vertex(vertex[1].x, vertex[1].y, vertex[1].z);
		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);
		drawLayer.vertex(vertex[3].x, vertex[3].y, vertex[3].z);
		//back z
		drawLayer.vertex(vertex[4].x, vertex[4].y, vertex[4].z);
		drawLayer.vertex(vertex[5].x, vertex[5].y, vertex[5].z);
		drawLayer.vertex(vertex[6].x, vertex[6].y, vertex[6].z);
		drawLayer.vertex(vertex[7].x, vertex[7].y, vertex[7].z);
		//bottom y
		drawLayer.vertex(vertex[3].x, vertex[3].y, vertex[3].z);
		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);
		drawLayer.vertex(vertex[7].x, vertex[7].y, vertex[7].z);
		drawLayer.vertex(vertex[6].x, vertex[6].y, vertex[6].z);
		//top y
		drawLayer.vertex(vertex[5].x, vertex[5].y, vertex[5].z);
		drawLayer.vertex(vertex[4].x, vertex[4].y, vertex[4].z);
		drawLayer.vertex(vertex[1].x, vertex[1].y, vertex[1].z);
		drawLayer.vertex(vertex[0].x, vertex[0].y, vertex[0].z);
		//right x
		drawLayer.vertex(vertex[1].x, vertex[1].y, vertex[1].z);
		drawLayer.vertex(vertex[4].x, vertex[4].y, vertex[4].z);
		drawLayer.vertex(vertex[7].x, vertex[7].y, vertex[7].z);
		drawLayer.vertex(vertex[2].x, vertex[2].y, vertex[2].z);
		//left x
		drawLayer.vertex(vertex[5].x, vertex[5].y, vertex[5].z);
		drawLayer.vertex(vertex[0].x, vertex[0].y, vertex[0].z);
		drawLayer.vertex(vertex[3].x, vertex[3].y, vertex[3].z);
		drawLayer.vertex(vertex[6].x, vertex[6].y, vertex[6].z);
		drawLayer.endShape();

		//popMatrix();

		drawLayer.popMatrix();
	}
}
