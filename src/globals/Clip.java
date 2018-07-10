package globals;

import processing.core.PGraphics;
import processing.core.PVector;

public class Clip {

	protected Main p5;
	boolean isPlaying;

	public String name;

	protected PGraphics drawLayer;
	private String rendererType;
	public PVector viewPosition;

	protected boolean[] triggers;

	protected boolean audioTrigger = false;

	public Clip(String _rendererType) {
		p5 = getP5();
		rendererType = _rendererType;
	}

	public void load() {
		isPlaying = false;

		name = "??";

		PVector layerBoxSize = ClipManager.canvasSize; // RESOLUCION Q SE LA BANCA
		drawLayer = p5.createGraphics((int) layerBoxSize.x, (int) layerBoxSize.y, rendererType); // SI PONGO P2D, EL FRAMERATE DROPPEA MAAAAALL..!!
		viewPosition = ClipManager.clipViewPosition;

		triggers = new boolean[5];
		for (int i = 0; i < triggers.length; i++) {
			triggers[i] = false;
		}

	}

	public void start() {
		isPlaying = true;
		p5.println("-|| STARTING: " + name);
	}

	public void stop() {
		isPlaying = false;
		p5.println("-|| STOPPING: " + name);
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
		for (int i = 0; i < triggers.length; i++) {
			triggers[i] = false;
		}
	}

	public String getName() {
		return name;
	}

	public void setViewPositioner(PVector positionObject) {
		viewPosition = positionObject;
	}

	public PVector getViewPosition() {
		return viewPosition;
	}

	public void update() {
	}

	public void render() {
		if (ClipManager.displayClips) {
			p5.image(drawLayer, viewPosition.x, viewPosition.y);
		}
		
		resetTriggers();
		p5.fill(255);
		p5.text("--|| TRIGGERS:\n0 = " + triggers[0] + "\n1 = " + triggers[1] + "\n2 = " + triggers[2] + "\n3 = " + triggers[3] + "\n4 = " + triggers[4], 620, 20);
		
	}

	public PGraphics getDrawLayer() {
		return drawLayer;
	}

	public void onKeyPressed(char key) {

	}

	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------

	public void recieveControllerChange(int channel, int number, int value) {

	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {

	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {

	}

	// EVENTS FROM A MIDI CONTROLLER - END ------------

	public void setAudioTrigger(boolean state) {
		audioTrigger = state;
	}

	public void beatEvent(int beatDivision) {
		// beatDivision ESTA POR SI SI QUIERE USAR

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}