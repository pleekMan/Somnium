package clips.platonicSolids;

import processing.core.PVector;

public class Solid_Dodecahedron extends Solid {
	final int FACES = 12; // number of faces 
	final int VERTICES = 5; // VERTICES per face
	final float A = 1.618033989f; // (1 * sqr(5) / 2) - wikipedia
	final float B = 0.618033989f; // 1 / (1 * sqr(5) / 2) - wikipedia

	PVector[] vert = new PVector[20]; // list of vertices
	int[][] faces = new int[FACES][VERTICES]; // list of faces (joining vertices)

	public Solid_Dodecahedron() {
		super();

		vert[0] = new PVector(1, 1, 1);
		vert[1] = new PVector(1, 1, -1);
		vert[2] = new PVector(1, -1, 1);
		vert[3] = new PVector(1, -1, -1);
		vert[4] = new PVector(-1, 1, 1);
		vert[5] = new PVector(-1, 1, -1);
		vert[6] = new PVector(-1, -1, 1);
		vert[7] = new PVector(-1, -1, -1);

		vert[8] = new PVector(0, B, A);
		vert[9] = new PVector(0, B, -A);
		vert[10] = new PVector(0, -B, A);
		vert[11] = new PVector(0, -B, -A);

		vert[12] = new PVector(B, A, 0);
		vert[13] = new PVector(B, -A, 0);
		vert[14] = new PVector(-B, A, 0);
		vert[15] = new PVector(-B, -A, 0);

		vert[16] = new PVector(A, 0, B);
		vert[17] = new PVector(A, 0, -B);
		vert[18] = new PVector(-A, 0, B);
		vert[19] = new PVector(-A, 0, -B);

		faces[0] = new int[] { 0, 16, 2, 10, 8 };
		faces[1] = new int[] { 0, 8, 4, 14, 12 };
		faces[2] = new int[] { 16, 17, 1, 12, 0 };
		faces[3] = new int[] { 1, 9, 11, 3, 17 };
		faces[4] = new int[] { 1, 12, 14, 5, 9 };
		faces[5] = new int[] { 2, 13, 15, 6, 10 };
		faces[6] = new int[] { 13, 3, 17, 16, 2 };
		faces[7] = new int[] { 3, 11, 7, 15, 13 };
		faces[8] = new int[] { 4, 8, 10, 6, 18 };
		faces[9] = new int[] { 14, 5, 19, 18, 4 };
		faces[10] = new int[] { 5, 19, 7, 11, 9 };
		faces[11] = new int[] { 15, 7, 19, 18, 6 };
	}

	PVector[] get2RandomVertex() {
		PVector[] vertices = { vert[p5.floor(vert.length - 0.1f)], vert[p5.floor(vert.length - 0.1f)] };
		return vertices;
	}

	void render() {
		super.render();

		//fill(255,255,0);

		drawLayer.pushMatrix();
		drawLayer.translate(x, y, z);
		drawLayer.scale(scale * maxAbsoluteScale);

		/*
		x  = map (mouseX, 0, width, 0, 2*PI);  
		 rotateY(x);
		 
		 y  = map (mouseY, 0, height, 0, 2*PI);
		 rotateX(y);
		 */

		for (int i = 0; i < FACES; i = i + 1) {

			//fill(map(i, 0, FACES, 0, 255));

			drawLayer.beginShape();
			//texture(tex);
			for (int i2 = 0; i2 < VERTICES; i2 = i2 + 1) {
				drawLayer.vertex(vert[faces[i][i2]].x, vert[faces[i][i2]].y, vert[faces[i][i2]].z);
			}
			drawLayer.endShape(p5.CLOSE);
		}
		drawLayer.popMatrix();
	}

}
