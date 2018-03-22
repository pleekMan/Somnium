package clips.platonicSolids;

import processing.core.PVector;

public class Solid_Tetrahedron extends Solid {

	PVector[] vert = { new PVector(1, 1, 1), new PVector(-1, -1, 1), new PVector(-1, 1, -1), new PVector(1, -1, -1) };

	Solid_Tetrahedron() {
		super();

		vert = new PVector[4];
		vert[0] = new PVector(1, 1, 1);
		vert[1] = new PVector(-1, -1, 1);
		vert[2] = new PVector(-1, 1, -1);
		vert[3] = new PVector(1, -1, -1);
	}

	PVector[] get2RandomVertex() {
		PVector[] vertices = { vert[p5.floor(vert.length - 0.1f)], vert[p5.floor(vert.length - 0.1f)] };
		return vertices;
	}

	void render() {
		super.render();

		//fill(0,255,0);

		drawLayer.pushMatrix();

		drawLayer.translate(x, y, z);
		drawLayer.scale(scale * maxAbsoluteScale);

		createFace(0, 1, 2);
		createFace(1, 2, 3);
		createFace(2, 3, 0);
		createFace(1, 3, 0);

		drawLayer.popMatrix();
	}

	void createFace(int A, int B, int C) {
		float p = 1.1f	;
		int i = 0;

		drawLayer.beginShape(); // QUAD 
		//texture(tex);
		i = A;
		drawLayer.vertex(vert[i].x * p, vert[i].y * p, vert[i].z * p, 0, 0);
		i = B;
		drawLayer.vertex(vert[i].x * p, vert[i].y * p, vert[i].z * p, 0, 1);
		i = C;
		drawLayer.vertex(vert[i].x * p, vert[i].y * p, vert[i].z * p, 1, 0);
		drawLayer.endShape(p5.CLOSE);
	}
}
