package globals;

import processing.core.PGraphics;
import processing.core.PVector;

public class Clip {

	protected Main p5;
	boolean isPlaying;

	public String name;

	protected PGraphics drawLayer;
	private String rendererType;
	
	protected boolean[] triggers;

	public Clip(String _rendererType) {
		p5 = getP5();
		rendererType = _rendererType;
	}

	public void load() {
		isPlaying = false;

		name = "??";

		PVector layerBoxSize = new PVector(p5.width,p5.height); // RESOLUCION Q SE LA BANCA
		
		drawLayer = p5.createGraphics((int)layerBoxSize.x, (int)layerBoxSize.y, rendererType); // SI PONGO P2D, EL FRAMERATE DROPPEA MAAAAALL..!!

		triggers = new boolean[5];
		for (int i = 0; i < triggers.length; i++) {
			triggers[i] = false;
		}

	}

	public void start() {
		isPlaying = true;
	}

	public void stop() {
		isPlaying = false;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setName(String _name) {
		name = _name;
	}

	public void trigger(int triggerId) {
		triggers[triggerId] = true;
	}

	public void resetTriggers() {
		for (int i=0; i < triggers.length; i++) {
			triggers[i] = false;
		}
	}

	public String getName() {
		return name;
	}


	public void update() {
	}


	public void render() {
	}


	public PGraphics getDrawLayer() {
		return drawLayer;
	}
	
	public void onKeyPressed(char key){
		
	}
	
	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------
	
	public void recieveControllerChange(int channel, int number, int value) {

	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {


	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {

	}
	
	// EVENTS FROM A MIDI CONTROLLER - END ------------


	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}