package lights;

import globals.Main;
import globals.PAppletSingleton;
import java.util.ArrayList;
import processing.core.PImage;
import processing.serial.*;

public class PixelPicker {
	Main p5;

	Serial serialPort;

	ArrayList<Picker> pickers;
	static public PImage samplingSurface;
	//float surfaceWidth, surfaceHeight;

	int waitFramesToStart = 120;
	boolean enableSendOut;

	boolean calibrateMode = false;
	

	public PixelPicker() {
		p5 = getP5();

		pickers = new ArrayList<Picker>();
		//samplingSurface = createGraphics(_surfaceWidth, _surfaceHeight, P2D);

		setupPickers(0);
		resetSender();
	}

	public void setupPickers(int pickerCount) {
		p5.println("-|| SETTING UP PICKERS -> WILL START SENDING AFTER " + waitFramesToStart + " FRAMES");

		for (int i = 0; i < pickerCount; i++) {
			Picker newPicker = new Picker(0, 0);
			pickers.add(newPicker);
		}

		enableSendOut = false;
	}

	public void setupPickersFromFile(String _fileName) {

		String[] eachPickerData = p5.loadStrings(_fileName);

		if (eachPickerData == null) {
			p5.println("-|| NO DATA COULD BE LOADED FROM FILE. DEFAULTING TO 1 PICKER at 0,0");
		} else {
			pickers.clear();
			for (int i = 0; i < eachPickerData.length; i++) {
				//println(eachPickerData[i]);
				String[] dataSplit = p5.split(eachPickerData[i], ','); // 0=id, 1=x, 2=y
				if (!dataSplit[0].equals("")) { // IF WE ARE IN AN EMPTY LINE (CREATED BY savePickers WAY OF SAVING)
					Picker newPicker = new Picker(Float.parseFloat(dataSplit[1]), Float.parseFloat(dataSplit[2]));
					pickers.add(newPicker);
				}
			}
		}
	}

	public void update() {

		if (samplingSurface != null) {

			pick();

			if (calibrateMode) {
				drawPickers();
			}
		}
		
	}

	public void savePickersToFile(String _fileName) {
		String allPickersData = "";

		for (int i = 0; i < pickers.size(); i++) {
			allPickersData += i + "," + pickers.get(i).getX() + "," + pickers.get(i).getY() + ":";
		}

		String[] asList = p5.split(allPickersData, ":");
		p5.saveStrings(p5.dataPath("") + "/" + _fileName, asList);
	}

	public void pick() {
		samplingSurface.loadPixels();
		for (int i = 0; i < pickers.size(); i++) {
			Picker p = pickers.get(i);
			
			p.setColor(getColorAt(p.getX(), p.getY()));
		}

		//sendOut();
	}

	public int getColorAt(float x, float y) {
		//println("DS.width -> " + drawSurface.width);
		int pixelSlot = ((int)(x * samplingSurface.width) + (samplingSurface.width * (int)(y * samplingSurface.height)));
		return samplingSurface.pixels[pixelSlot];
	}

	public Picker getPicker(int pickerNum) {
		return pickers.get(pickerNum);
	}

	public void toggleCalibrationMode() {
		calibrateMode = !calibrateMode;
	}

	public boolean isCalibrating() {
		return calibrateMode;
	}

	public void addPicker(float _x, float _y) {
		pickers.add(new Picker(_x / samplingSurface.width, _y / samplingSurface.height));
	}

	public void sendOut() {

		if (enableSendOut) {

			if (serialPort != null) {

				if (p5.frameCount > waitFramesToStart) {
					// IF SENDING STARTS STRAIGHT AWAY, THE LEDS (or data sent?) ARE SOMEHOW SHIFTED FORWARD +1

					//serialPort.write(byte(101));

					for (int i = 0; i < pickers.size(); i++) {
						int c = pickers.get(i).getColor();
						int r = (c >> 16) & 0xFF;
						int g = (c >> 8) & 0xFF;
						int b = c & 0xFF;
						byte[] toSend = { mapByteToRange(r), mapByteToRange(g), mapByteToRange(b) };
						serialPort.write(toSend);
					}
					//serialPort.write(byte(101)); // 201 => CODE FOR "FINISHED SENDING ALL LEDS"
					serialPort.clear();
				}
			}
		}
	}

	//TODO CHANGE THIS MAPPING ON THE ARDUINO CODE
	public byte mapByteToRange(int value) {
		// SYSTEM TO ONLY USE VALUES FROM 0 -> 200, AND THEN LEAVE OTHER VALUES AS CONTROL CODES
		return (byte) ((value / (float) 255) * 199);
	}

	public void drawPickers() {

		for (int i = 0; i < pickers.size(); i++) {
			p5.fill(255);
			p5.noStroke();
			p5.text(i, (pickers.get(i).getX() * samplingSurface.width) + 10, (pickers.get(i).getY() * samplingSurface.height));

			p5.fill(pickers.get(i).getColor());
			p5.stroke(30);
			//p5.noStroke();
			p5.ellipse(pickers.get(i).getX() * samplingSurface.width, pickers.get(i).getY() * samplingSurface.height, 10, 10);
		}
	}
	
	public void drawRockyPickers(){
		for (int i = 0; i < pickers.size(); i++) {
			
		}
	}

	public void renderDrawSurface() {
		p5.image(samplingSurface, 0, 0);
	}

	static public void setSamplingSurface(PImage surface) {
		samplingSurface = surface;
	}

	public PImage getSamplingSurface() {
		return samplingSurface;
	}

	public void removeAllPickers() {
		pickers.clear();
	}

	public ArrayList<Picker> getAllPickers() {
		return pickers;
	}

	public void resetSender() {

		if (serialPort != null) {
			p5.println("-|| RESETING PICKERS...");
			serialPort.clear();
			serialPort.write((byte) (101));
			p5.delay(150);

			p5.println("-|| DONE");
		}
	}

	public int getPickerCount() {
		return pickers.size();
	}

	public void setEnableSendOut(boolean state) {
		enableSendOut = state;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}