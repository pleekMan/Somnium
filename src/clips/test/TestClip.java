package clips.test;

import globals.Clip;

public class TestClip extends Clip {

	public TestClip(String _rendererType) {
		super(_rendererType);
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
	public void stop() {
		super.stop();
	}

	@Override
	public void render() {
		drawLayer.beginDraw();
		//drawLayer.background(0);
		//drawLayer.fill((p5.frameCount % 255), (p5.frameCount % 200), (p5.frameCount % 230));
		drawLayer.fill(255);
		drawLayer.noStroke();
		drawLayer.ellipse(p5.mouseX, p5.mouseY, 50, 50);
		drawLayer.endDraw();

		// DRAW LAYER IS DRAWN ON SUPERCLASS
		//p5.image(drawLayer, viewPosition.x, viewPosition.y);
		//p5.image(drawLayer, 0, 0);
		
		super.render();
	}
}
