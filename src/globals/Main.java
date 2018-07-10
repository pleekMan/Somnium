package globals;

//import controls.MidiController;
import clips.rockyPickers.RockyPickers;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Tools;
import lights.PixelPicker;
import controlP5.ControlEvent;
import controls.ComputerVisionManager;

public class Main extends PApplet {

	ClipManager clipManager;
	PixelPicker pixelPicker;
	//ComputerVisionManager cvManager;
	//MidiController midiController;
	RockyPickers rockyPickers;


	public void settings() {
		size(1200, 800, P2D);
		//size(600,600, P2D);
		//fullScreen(P2D,1);
	}

	public void setup() {

		setPAppletSingleton();

		frame.setBackground(new java.awt.Color(0, 0, 0));

		textureMode(NORMAL);
		//imageMode(CENTER);

		//cvManager = new ComputerVisionManager();
		clipManager = new ClipManager();
		pixelPicker = new PixelPicker();
		pixelPicker.setupPickersFromFile("rockyData.csv");

		//midiController = new MidiController();
		//midiController.setClipManagerReference(clipManager);

		rockyPickers = new RockyPickers(P2D);
		rockyPickers.load();
		rockyPickers.setName("ROCKY PICKERS");
		//rockyPickers.setViewPositioner(clipViewPosition);
		rockyPickers.createRocks(pixelPicker.getAllPickers());

	}

	public void draw() {
		//surface.setTitle(Integer.toString(((int) frameRate)) + " fps");
		background(0);
		//background(25, 25, 50);
		//drawBackLines();

		/*
		if (cvManager.detectsSomething()) {
			cvManager.getAllCentroids();
		}
		*/

		clipManager.update();
		clipManager.render();

		pixelPicker.update();

		if (rockyPickers.display) {
			rockyPickers.setRockColors(pixelPicker.getAllPickers());
			rockyPickers.render();
		}

		Tools.drawMouseCoordinates();
	}

	public void keyPressed() {

		clipManager.onKeyPressed(key);

		if (key == 'r') {
			rockyPickers.display = !rockyPickers.display;
		}
		
		if (key == 'e') {
			clipManager.toggleEditMode();
		}

		if (key == 'l') {
			pixelPicker.toggleCalibrationMode();
		}

		if (key == CODED) {
			if (keyCode == UP) {
			}
		}

	}

	public void mousePressed() {
		clipManager.onMousePressed(mouseButton); // CONSTANT -> LEFT, RIGHT OR CENTER
	}

	public void mouseReleased() {
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
	}

	public void mouseMoved() {
		// ship.onMouseMoved();
	}

	// *******************************************
	// *******************************************

	public void controlEvent(ControlEvent event) {
		// THIS IS FORWARDED TO GuiControllers
		clipManager.guiControllers.controlEvent(event);
	}

	// *******************************************
	// *******************************************

	public static void main(String args[]) {
		/*
		 * if (args.length > 0) { String memorySize = args[0]; }
		 */

		PApplet.main(new String[] { Main.class.getName() });
		//PApplet.main(new String[] { "--present","--hide-stop",Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

}
