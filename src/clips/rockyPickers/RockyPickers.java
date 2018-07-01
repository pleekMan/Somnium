package clips.rockyPickers;

import java.util.ArrayList;

import lights.Picker;
import processing.core.PVector;
import globals.Clip;
import globals.ClipManager;

public class RockyPickers extends Clip {

	ArrayList<PVector[]> rocks;
	int[] rockColors;
	public boolean display = true;

	
	public RockyPickers(String _rendererType) {
		super(_rendererType);
		
		rocks = new ArrayList<PVector[]>();
	}

	public void createRocks(ArrayList<Picker> pickers){
		
		rockColors = new int[pickers.size()];

		float rockRadius = 20;
		int vertexCount = 10;
		
		for (int i = 0; i < pickers.size(); i++) {
			PVector[] newRock = new PVector[vertexCount];
			for (int j = 0; j < newRock.length; j++) {
				 
				float x = p5.random(rockRadius * 0.5f, rockRadius) * p5.cos((p5.TWO_PI / vertexCount) * j);
				float y = p5.random(rockRadius * 0.5f, rockRadius) * p5.sin((p5.TWO_PI / vertexCount) * j);
				x += pickers.get(i).getX() * ClipManager.canvasSize.x;
				y += pickers.get(i).getY() * ClipManager.canvasSize.y;

				//float x = (pickers.get(i).getX() * ClipManager.canvasSize.x) + p5.random(-rockRadius, rockRadius);
				//float y = (pickers.get(i).getY() * ClipManager.canvasSize.y) + p5.random(-rockRadius, rockRadius);
				newRock[j] = new PVector(x,y);
			}
			rocks.add(newRock);
			rockColors[i] = 0;
		}
		
	}
	
	@Override
	public void load() {
		super.load();
	}

	@Override
	public void start() {
		super.start();
	}
	

	@Override
	public void update() {
		super.update();
	}
	
	public void setRockColors(ArrayList<Picker> pickers){
		for (int i = 0; i < rockColors.length; i++) {
			rockColors[i] = pickers.get(i).getColor();
		}
	}

	@Override
	public void render() {
		
		drawLayer.beginDraw();
		drawLayer.clear();
		//drawLayer.background(0);
		
		for (int i = 0; i < rocks.size(); i++) {
			PVector[] thisRock = rocks.get(i);
			
			drawLayer.fill(rockColors[i]);
			drawLayer.beginShape();
			for (int j = 0; j < thisRock.length; j++) {
				drawLayer.vertex(thisRock[j].x,thisRock[j].y);
			}
			drawLayer.endShape(p5.CLOSE);
		}
		
		drawLayer.endDraw();
		
		//p5.blendMode(p5.ADD);
		p5.image(drawLayer, viewPosition.x, viewPosition.y); // cuz of the blend mode, not using super.render();
		//p5.blendMode(p5.BLEND);
		
		//super.render(); // THIS GOES AT THE END OF render(). (SUPERCLASS DRAWS Pgraphics)


	}
}
