package clips.distantSignal;

import processing.core.PVector;
import processing.video.Movie;
import globals.Clip;

public class VideoClip extends Clip {

	Movie movieClip;

	public VideoClip(String _rendererType) {
		super(_rendererType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void load() {
		super.load();

		movieClip = new Movie(p5, p5.sketchPath() + "/bin/data/movieClips/TOWARDS THE COSMIC CRYSTAL.mov");
	}

	@Override
	public void start() {
		super.start();
		p5.println("-|| STARTING: " + name);
		
		movieClip.jump(0);
		movieClip.play();
	}

	@Override
	public void stop() {
		super.stop();
		p5.println("-|| STOPPING: " + name);
		
		movieClip.pause();
	}

	@Override
	public void render() {

		drawLayer.beginDraw();
		drawLayer.background(0);
		
		if (movieClip.available()) {
			movieClip.read();
		}
		drawLayer.image(movieClip, 0, 0);
		
		drawLayer.text(movieClip.time(),10,10);
		
		drawLayer.endDraw();

		super.render(); // AT THE END OF THIS FUNCTION
	}


	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------
	public void recieveControllerChange(int channel, int number, int value) {
		if (channel == 0) {
			if (number == 0) {
			}
			if (number == 1) {
			}
		}

		if (channel == 1) {
			if (number == 0) {
			}

			if (number == 1) {
			}
		}

		if (channel == 2) {
			if (number == 0) {
			}
			if (number == 1) {
			}
		}

		if (channel == 3) {
			if (number == 1) {
			}
		}

		if (channel == 4) {
			if (number == 1) {
			}
		}
	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {
	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {
	}

	// EVENTS FROM A MIDI CONTROLLER - END ------------

	@Override
	public void beatEvent(int beatDivision) {
		//interferenceOpacity = 1;
		//interferenceCenter.set(p5.random(drawLayer.width), p5.random(drawLayer.height));
	}
}
