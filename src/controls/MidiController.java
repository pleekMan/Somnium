package controls;

import globals.ClipManager;
import globals.Main;
import globals.PAppletSingleton;
import themidibus.*;

public class MidiController {
	public Main p5;

	MidiBus midiInterface;
	ClipManager clipManagerRef;

	public MidiController() {
		p5 = getP5();

		MidiBus.list();
		midiInterface = new MidiBus(this, "SLIDER/KNOB", -1); // Create a new MidiBus with no input device and the default Java Sound Synthesizer as the output device.

	}
	
	public void setClipManagerReference(ClipManager cm){
		clipManagerRef = cm;
	}

	public void controllerChange(int channel, int number, int value) {
		
		clipManagerRef.recieveControllerChange(channel, number, value);
		
		/*
		p5.println();
		p5.println("Controller Change:");
		p5.println("--------");
		p5.println("Channel:" + channel);
		p5.println("Number:" + number);
		p5.println("Value:" + value);
		*/
	}

	public void noteOn(int channel, int pitch, int velocity) {
		
		clipManagerRef.recieveNoteOn(channel, pitch, velocity);
		
		/*
		p5.println();
		p5.println("Note On:");
		p5.println("--------");
		p5.println("Channel:" + channel);
		p5.println("Pitch:" + pitch);
		p5.println("Velocity:" + velocity);
		*/
	}

	public void noteOff(int channel, int pitch, int velocity) {
		
		clipManagerRef.recieveNoteOff(channel, pitch, velocity);
		
		/*
		p5.println();
		p5.println("Note Off:");
		p5.println("--------");
		p5.println("Channel:" + channel);
		p5.println("Pitch:" + pitch);
		p5.println("Velocity:" + velocity);
		*/
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
