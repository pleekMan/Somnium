package clips.transition;

import globals.Main;
import globals.PAppletSingleton;

public class Fader {
	Main p5;
	float opacity;
	float opacityVel;
	int color;
	boolean isPlaying;
	boolean goingUp;

	boolean controlledWithController;

	public Fader() {
		p5 = getP5();
		
		reset();
		color = p5.color(0);
		goingUp = true;
		controlledWithController = false;
	}

	public void update() {

		if (!controlledWithController) {
			// MOVE
			if (isPlaying)
				opacity += opacityVel;

			// CHECK TO START DE-FADING
			if (opacity >= 1) {
				opacity = 0.99f;
				opacityVel *= -1;
				goingUp = false;
			}

			// STOP AFTER FADE -> DE-FADE
			if (opacity <= 0 && goingUp == false) {
				reset();
			}
		}
	}

	public void render() {
		p5.fill(color, opacity * 255);
		p5.rect(0, 0, p5.width, p5.height);
	}

	public void start() {
		reset();
		isPlaying = true;
	}

	public void stop() {
		isPlaying = false;
	}

	public void reset() {
		opacity = 0;
		isPlaying = false;
	}

	public void setColor(int c) {
		color = c;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setControlledWithController(boolean state) {
		controlledWithController = state;
	}
	
	
	public void recieveControllerChange(int channel, int number, int value){
		if(channel == 7){
			if(number == 1){
				opacity = p5.map(value, 0, 127, 0, 1);
			}
			
			if(number == 2){
				setColor(p5.color(0));
			} else if(number == 3){
				setColor(p5.color(255));
			}
		}
		
	
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
