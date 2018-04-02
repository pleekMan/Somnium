package controls;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import globals.Main;
import globals.PAppletSingleton;

public class AudioController {
	Main p5;

	Minim minim;
	AudioInput audioIn;
	float audioLevelMultiplier;
	float amplifiedAudioLevel;
	float umbral;
	

	public AudioController() {
		p5 = getP5();

		minim = new Minim(p5);
		audioIn = minim.getLineIn(Minim.MONO);
		umbral = 0.5f;
		audioLevelMultiplier = 5;
	}

	public void render() {

		amplifiedAudioLevel = audioIn.left.level() * audioLevelMultiplier;
		//umbral = p5.map(p5.mouseY, p5.height, p5.height * 0.5f, 0, 1);
		
		// VUMETERS
		p5.stroke(0, 127, 127);
		p5.line(0, p5.height * 0.5f, p5.width, p5.height * 0.5f);

		p5.fill(0, 255, 255);
		p5.rect(0, p5.height, 30, -(audioIn.left.level() * (p5.height * 0.5f)));
		p5.text("In Level -> " + p5.nf(audioIn.left.level(), 0, 2), 40, p5.height - 20);

		p5.rect(p5.width, p5.height, -50, -(amplifiedAudioLevel * (p5.height * 0.5f)));
		p5.text("Amplified -> " + audioLevelMultiplier + "x  ->  " + p5.nf(audioIn.left.level(), 0, 2), p5.width - 250, p5.height - 20);
		
		// UMBRAL
		p5.rect(0, p5.height - ( (p5.height * 0.5f) * umbral ), p5.width, 2);
		p5.text(umbral, p5.width * 0.5f, p5.height - ( (p5.height * 0.5f) * umbral ) - 2);
	}
	
	public boolean isAboveThreshold(){
		return amplifiedAudioLevel > umbral;
	}
	
	public void setThreshold(float value){
		umbral = value;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
