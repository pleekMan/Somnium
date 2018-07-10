package globals;

import java.util.ArrayList;

import lights.PixelPicker;
import processing.core.PVector;
import tools.TempoManager;
import controls.GuiControllers;
import clips.SphereHarmony;
import clips.distantSignal.DistantSignal;
import clips.test.TestClip;
import clips.transition.Fader;

public class ClipManager {

	Main p5;
	ArrayList<Clip> clips;
	public int selectedClip;
	public int playingClip;
	public static PVector canvasSize;

	// GLOBAL POSITION OF CLIPS. FOR NOW LET'S KEEP IT SIMPLE: ALWAYS ANCHORED TO 0,0.
	// SO AS NOT TO HAVE TO MODIFY PixelPicker SAMPLING, AND KEEP MOUSE POS MAPPINGS
	public static PVector clipViewPosition;

	boolean editMode;
	public static boolean displayClips = true;

	Fader fader;
	//AudioController audioIn;

	GuiControllers guiControllers;
	TempoManager tempoManager;

	public ClipManager() {
		p5 = getP5();

		canvasSize = new PVector(600, 600);

		clips = new ArrayList<Clip>();
		selectedClip = playingClip = 0;

		guiControllers = new GuiControllers(this);
		tempoManager = new TempoManager();

		clipViewPosition = new PVector(0, 0);

		editMode = false;

		fader = new Fader();
		//audioIn = new AudioController();

		loadClipSequence();
		selectedClip = 2;
		triggerClip(selectedClip);

	}

	private void loadClipSequence() {
		SphereHarmony sphereHarmony = new SphereHarmony(p5.P2D);
		sphereHarmony.load();
		sphereHarmony.setName("SPHERE HARMONY");
		sphereHarmony.setViewPositioner(clipViewPosition);
		clips.add(sphereHarmony);
		System.out.println("-|| Loaded :> " + sphereHarmony.getName());

		TestClip testClip = new TestClip(p5.P2D);
		testClip.load();
		testClip.setName("TEST CLIP");
		testClip.setViewPositioner(clipViewPosition);
		clips.add(testClip);
		System.out.println("-|| Loaded :> " + testClip.getName());
		
		DistantSignal distantSignal = new DistantSignal(p5.P2D);
		distantSignal.load();
		distantSignal.setName("DISTANT SIGNAL");
		distantSignal.setViewPositioner(clipViewPosition);
		clips.add(distantSignal);
		System.out.println("-|| Loaded :> " + distantSignal.getName());

		/*
		PlatonicSolids platonicSolids = new PlatonicSolids(p5.P3D);
		platonicSolids.load();
		platonicSolids.setName("PLATONIC SOLIDS");
		clips.add(platonicSolids);
		System.out.println("-|| Loaded :> " + platonicSolids.getName());
		
		
		MoonEclipse moonEclipse = new MoonEclipse(p5.P2D);
		moonEclipse.load();
		moonEclipse.setName("MOON ECLIPSE");
		clips.add(moonEclipse);
		System.out.println("-|| Loaded :> " + moonEclipse.getName());	
		
		LunarDrone lunarDrone = new LunarDrone(p5.P2D);
		lunarDrone.load();
		lunarDrone.setName("LUNAR DRONE");
		clips.add(lunarDrone);
		System.out.println("-|| Loaded :> " + lunarDrone.getName());

		SpaceCreature spaceCreatures = new SpaceCreature(p5.P3D);
		spaceCreatures.load();
		spaceCreatures.setName("SPACE CREATURES");
		clips.add(spaceCreatures);
		System.out.println("-|| Loaded :> " + spaceCreatures.getName());
		*/

		for (int i = 0; i < clips.size(); i++) {
			guiControllers.addClipItem(clips.get(i).getName());
		}
	}

	public void update() {

		//audioIn.update();

		/*
		if (clips.get(selectedClip) != null) {
			clips.get(selectedClip).setAudioTrigger(audioIn.isAboveThreshold());

		}
		*/

		// TEMPO
		tempoManager.update();
		if (tempoManager.isOnBeat()) {
			getPlayingClip().beatEvent(tempoManager.getBeatDivision());
		}

		// CLIPS
		getPlayingClip().update();
		//getPlayingClip().resetTriggers();
		/*
		for (Clip clip : clips) {
			if (clip.isPlaying()) {
				clip.update();
				clip.resetTriggers();
			}
		}
		*/
	}

	public void render() {

		//CLIPS
		getPlayingClip().render();

		/*
		for (Clip clip : clips) {
			if (clip.isPlaying()) {
				clip.render();
			}
		}
		*/

		// RENDER MASK
		//p5.image(mask, LightsManager.center.x, LightsManager.center.y, LightsManager.getBoundingBoxDimension(), LightsManager.getBoundingBoxDimension());

		fader.render();

		tempoManager.render();

		// EDIT MODE DISPLAY -------------------------------

		if (editMode) {

			p5.textSize(12);

			// BACKPLANE
			p5.fill(0, 200);
			p5.stroke(255);
			p5.rect(0, 0, 250, p5.height);

			p5.fill(255);
			p5.noStroke();

			p5.text("FR: " + p5.frameRate, 20, 20);
			p5.text("Selected Clip: " + selectedClip, 20, 40);
			p5.text("Playing Clip: " + playingClip + "/" + clips.size(), 20, 60);

			drawClipNavigator();

			// RENDER AUDIO VUMETERS
			//audioIn.render();

			guiControllers.render();

		}

	}

	private void drawClipNavigator() {
		int originX = 20;
		int originY = 140;

		if (clips.size() > 0) {

			float boxSize = 200f / clips.size();

			p5.stroke(0, 200, 200);
			for (int i = 0; i < clips.size(); i++) {

				if (i == playingClip) {
					p5.fill(200, 0, 0);
				} else if (i == selectedClip) {
					p5.fill(200, 200, 0);
				} else {
					p5.fill(127);
				}

				float x = originX + (boxSize * i);
				p5.rect(x, originY, boxSize, 40);
			}

			// PLAYING CLIP NAME
			p5.fill(255, 255, 0);
			p5.stroke(255, 255, 0);
			String playingClipName = getPlayingClip().getName();
			p5.text(playingClipName, 20, originY - 20);

			p5.line(originX + (playingClip * boxSize), originY, originX + (playingClip * boxSize), originY - 18);
			p5.line(originX, originY - 18, originX + (playingClip * boxSize), originY - 18);

			// SELECTED CLIP NAME
			String selectedClipName = getSelectedClip().getName();
			p5.text(selectedClipName, 20, originY + 60);

		} else {
			p5.text("-- NO CLIPS LOADED --", originX, originY);
		}
	}

	public void triggerClip(int selectedClip) {

		if (selectedClip < clips.size()) {

			for (Clip clip : clips) {
				clip.stop();
			}

			getSelectedClip().start();
			playingClip = selectedClip;

			PixelPicker.setSamplingSurface(getPlayingClip().getDrawLayer());
			guiControllers.setPlayingClip(getPlayingClip().getName());

			// This works cuz ClipViewPOsition PVector in ClipManager and Clip are the same (linked on load)
			// LET'S KEEP IT SIMPLE: ALWAYS ANCHORED TO 0,0.
			clipViewPosition.set(0, 0);

			//p5.println("-|| MANAGER :: Playing Clip ->\t" + playingClip + " = " + clips.get(playingClip).getName());
			//p5.println("-|| MANAGER :: Selected Clip ->\t" + selectedClip + " = " + clips.get(selectedClip).getName());
			//p5.println("-|| -----");

		} else {
			System.out.println("No Clip Found at: " + selectedClip);
		}
	}

	public void stopClip() {
		getSelectedClip().stop();
	}

	public void toggleEditMode() {
		editMode = !editMode;
	}

	public Clip getPlayingClip() {
		return clips.get(playingClip);
	}

	public Clip getSelectedClip() {
		return clips.get(selectedClip);
	}

	public void goToNextClip() {
		selectedClip++;
		if (selectedClip > clips.size() - 1) {
			selectedClip = clips.size() - 1;
		}
		p5.println("-|| MANAGER :: Selected Clip ->\t" + selectedClip + " = " + clips.get(selectedClip).getName());
		p5.println("-|| MANAGER :: Playing Clip ->\t" + playingClip + " = " + clips.get(playingClip).getName());
		p5.println("-|| -----");
	}

	public void goToPreviousClip() {
		selectedClip--;
		if (selectedClip < 0) {
			selectedClip = 0;
		}
		p5.println("-|| MANAGER :: Selected Clip ->\t" + selectedClip + " = " + clips.get(selectedClip).getName());
		p5.println("-|| MANAGER :: Playing Clip ->\t" + playingClip + " = " + clips.get(playingClip).getName());
		p5.println("-|| -----");

	}

	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------

	public void recieveControllerChange(int channel, int number, int value) {
		//p5.println("Controller :: " + channel + " | " + number + " | " + value);

		// AUDIO CONTROL
		if (channel == 6) {
			if (number == 0) {
				//audioIn.setAmpMultiplier(p5.map(value, 0, 127, 0.2f, 4));
			}
			if (number == 1) {
				//audioIn.setThreshold(p5.norm(value, 0, 127));
			}
		}

		// CLIP GLOBAL CONTROLS
		if (channel == 9) {
			if (number == 10 && value >= 127) {
				goToPreviousClip();
			}
			if (number == 11 && value >= 127) {
				goToNextClip();
			}
			if (number == 12 && value >= 127) {
				stopClip();
			}
			if (number == 13 && value >= 127) {
				triggerClip(selectedClip);
			}
			if (number == 14 && value >= 127) {
				toggleEditMode();
			}
		} else {
			// CLIP INNER CONTROLS
			clips.get(selectedClip).recieveControllerChange(channel, number, value);
			fader.recieveControllerChange(channel, number, value);
		}

	}

	public void onKeyPressed(char key) {

		if (clips.size() != 0)
			getSelectedClip().onKeyPressed(key);

		// SELECT AND LOAD CLIPS
		/*
		switch (key) {

		case '1':
			SphereHarmony sphereHarmony = new SphereHarmony(p5.JAVA2D);
			sphereHarmony.load();
			sphereHarmony.setName("SPHERE HARMONY");
			clips.add(sphereHarmony);
			System.out.println("-|| Loaded :> " + sphereHarmony.getName());
			break;
		case '2':
			PlatonicSolids platonicSolids = new PlatonicSolids(p5.P3D);
			platonicSolids.load();
			platonicSolids.setName("PLATONIC SOLIDS");
			clips.add(platonicSolids);
			System.out.println("-|| Loaded :> " + platonicSolids.getName());
			break;
		case '3':
			LunarDrone lunarDrone = new LunarDrone(p5.P2D);
			lunarDrone.load();
			lunarDrone.setName("LUNAR DRONE");
			clips.add(lunarDrone);
			System.out.println("-|| Loaded :> " + lunarDrone.getName());
			break;
		case '4':
			SpaceCreature spaceCreatures = new SpaceCreature(p5.P3D);
			spaceCreatures.load();
			spaceCreatures.setName("SPACE CREATURES");
			clips.add(spaceCreatures);
			System.out.println("-|| Loaded :> " + spaceCreatures.getName());
			break;
		case '5':
			MoonEclipse moonEclipse = new MoonEclipse(p5.P2D);
			moonEclipse.load();
			moonEclipse.setName("MOON ECLIPSE");
			clips.add(moonEclipse);
			System.out.println("-|| Loaded :> " + moonEclipse.getName());
			break;
		default:
			// System.out.println("No Clip Found at: " + selectedClip);
			break;
		}
		*/

		// START SELECTED CLIP
		if (key == 'a') {
			if (clips.size() > 0) {
				triggerClip(selectedClip);
			}
		}
		if (key == 's') {
			if (clips.size() > 0) {
				getSelectedClip().stop();
			}
		}

		if (key == 'w') {
			goToNextClip();
		}
		if (key == 'q') {
			goToPreviousClip();
		}

		if (key == 'd') {
			displayClips = !displayClips;
		}

		// TRIGGERS
		if (key == '1') {
			//clips.get(playingClip).trigger(0);
			//clipViewPosition.set(p5.mouseX, p5.mouseY);
			getPlayingClip().trigger(0);

		}
		if (key == '2') {
			getPlayingClip().trigger(1);
		}
		if (key == '3') {
			getPlayingClip().trigger(2);
		}
		if (key == '4') {
			getPlayingClip().trigger(3);
		}
		if (key == '5') {
			getPlayingClip().trigger(4);
		}

	}

	public void onMousePressed(int button) {
		if (tempoManager.isOverTapMarker(p5.mouseX, p5.mouseY)) {
			tempoManager.tap();
		}
	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {
		clips.get(selectedClip).recieveNoteOn(channel, pitch, velocity);
		//p5.println("Note ON :: " + channel + " | " + pitch + " | " + velocity);

	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {
		clips.get(selectedClip).recieveNoteOff(channel, pitch, velocity);
		//p5.println("Note OFF :: " + channel + " | " + pitch + " | " + velocity);

	}

	// EVENTS FROM A MIDI CONTROLLER - END ------------

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
