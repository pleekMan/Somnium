package globals;

import java.util.ArrayList;

import clips.SphereHarmony;
import clips.lunarDrone.LunarDrone;
import clips.platonicSolids.PlatonicSolids;
import clips.spaceCreatures.SpaceCreature;
//import processing.core.PGraphics;
//import processing.core.PImage;
import clips.transition.Fader;

public class ClipManager {

	Main p5;
	ArrayList<Clip> clips;
	public int selectedClip;
	public int playingClip;

	boolean editMode;

	//PImage mask;
	
	Fader fader;

	public ClipManager() {
		p5 = getP5();

		clips = new ArrayList<Clip>();

		selectedClip = playingClip = 0;

		editMode = false;

		//mask = p5.loadImage("OctagonaMask.png");
		
		fader = new Fader();
	}


	public void update() {
		for (Clip clip : clips) {
			if (clip.isPlaying()) {
				clip.update();
				clip.resetTriggers();

				// PGraphics layer1 = clip.getProjectionLayer();
				// p5.image(layer1,0,0);
			}
		}
	}

	public void render() {
		for (Clip clip : clips) {
			if (clip.isPlaying()) {
				clip.render();
			}
		}

		// RENDER MASK
		//p5.image(mask, LightsManager.center.x, LightsManager.center.y, LightsManager.getBoundingBoxDimension(), LightsManager.getBoundingBoxDimension());
		
		fader.render();
		
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

			// DISPLAY WHICH LAYER IS BEING USED FOR THE LIGHTS
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
			p5.fill(200, 0, 0);
			p5.stroke(200, 0, 0);
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

	public void onKeyPressed(char key) {

		if (clips.size() != 0)
			getSelectedClip().onKeyPressed(key);

		// SELECT AND LOAD CLIPS
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
		default:
			// System.out.println("No Clip Found at: " + selectedClip);
			break;
		}

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

		// TRIGGERS
		if (key == 'z') {
			clips.get(playingClip).trigger(0);
		}
		if (key == 'x') {
			clips.get(playingClip).trigger(1);
		}
		if (key == 'c') {
			clips.get(playingClip).trigger(2);
		}
		if (key == 'v') {
			clips.get(playingClip).trigger(3);
		}
		if (key == 'b') {
			clips.get(playingClip).trigger(4);
		}

	}

	private void triggerClip(int selectedClip) {

		if (selectedClip < clips.size()) {

			for (Clip clip : clips) {
				clip.stop();
			}

			getSelectedClip().start();
			playingClip = selectedClip;

		} else {
			System.out.println("No Clip Found at: " + selectedClip);
		}
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
	}

	public void goToPreviousClip() {
		selectedClip--;
		if (selectedClip < 0) {
			selectedClip = 0;
		}
	}

	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------

	public void recieveControllerChange(int channel, int number, int value) {
		clips.get(selectedClip).recieveControllerChange(channel, number, value);
		fader.recieveControllerChange(channel, number, value);
	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {
		clips.get(selectedClip).recieveNoteOn(channel, pitch, velocity);
	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {
		clips.get(selectedClip).recieveNoteOff(channel, pitch, velocity);
	}

	// EVENTS FROM A MIDI CONTROLLER - END ------------

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
