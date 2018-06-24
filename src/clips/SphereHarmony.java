package clips;

import globals.Clip;

public class SphereHarmony extends Clip {
	int circlesCount = 4;

	float[][] vertXPos;
	float[][] vertYPos;
	float[][] oscTiempo;
	int[] colors;
	float oscIncrementoGlobal;

	int cantidadDeVertices = p5.floor(p5.TWO_PI * 3) * 100; // TO COINCIDE WITH COLOR %s

	float[] centroX;
	float[] centroY;
	float radio = 500;
	float radioInterno;
	float radio4Circulos = 0;
	float rotacion4Circulos = 0;
	float rotacion4CirculosIncr = 0.01f;

	float angulo = 0;
	float anguloVel = 0;

	float oscilationControl;

	int funcionTrigUsada;
	float brillo;
	float pointSize = 1;

	float circulosJump = 0;

	public SphereHarmony(String rendererType) {
		super(rendererType);
	}

	@Override
	public void load() {
		super.load();

		centroX = new float[circlesCount];
		centroY = new float[circlesCount];
		colors = new int[circlesCount];

		vertXPos = new float[circlesCount][cantidadDeVertices];
		vertYPos = new float[circlesCount][cantidadDeVertices];
		oscTiempo = new float[circlesCount][cantidadDeVertices];
		oscIncrementoGlobal = 0.01f;

		for (int j = 0; j < circlesCount; j++) {

			for (int i = 0; i < cantidadDeVertices; i++) {
				vertXPos[j][i] = 0;
				vertYPos[j][i] = 0;
				//oscTiempo[i] = i / PI;
				oscTiempo[j][i] = (p5.TWO_PI / cantidadDeVertices) * i; // GETS WRITTEN OVER BY controlIncrOsc FUNCTION
			}
		}

		colors[0] = p5.color(25, 110, 0); // EARTH GREEN BROWN
		colors[1] = p5.color(0, 25, 128); // WATER BLUE
		colors[2] = p5.color(128, 25, 0); // FIRE RED ORANGE YELLOW
		colors[3] = p5.color(128, 128, 0); // AIR YELLOW

		radioInterno = 0;
		brillo = 255;
	}

	@Override
	public void start() {
		super.start();
		p5.imageMode(p5.CORNER);
		p5.println("-|| STARTING: " + name);
	}

	@Override
	public void stop() {
		super.stop();
		p5.println("-|| STOPPING: " + name);
	}

	@Override
	public void render() {

		drawLayer.beginDraw();
		//background(0);
		drawLayer.fill(0, 5);
		drawLayer.noStroke();
		drawLayer.rect(0, 0, drawLayer.width, drawLayer.height);

		drawLayer.noStroke();
		//drawLayer.fill(255);

		//displaceRotation();
		circulosJump = audioTrigger ? p5.frameCount % 4 : 1;

		calcularPosiciones();
		//dibujarCirculo();

		drawLayer.endDraw();

		p5.image(drawLayer, 0, 0);

	}

	private void calcularPosiciones() {

		//radio4Circulos = p5.mouseX * 0.5f;

		//fill(brillo, 127);
		float rotationUnit = p5.TWO_PI / centroX.length;

		for (int j = 0; j < centroX.length; j++) {

			float currentRotation = (rotationUnit * j) + rotacion4Circulos;
			centroX[j] = (drawLayer.width * 0.5f) + ((radio4Circulos * circulosJump) * p5.cos(currentRotation));
			centroY[j] = (drawLayer.height * 0.5f) + ((radio4Circulos * circulosJump) * p5.sin(currentRotation));

			//toTint(j);

			for (int i = 0; i < cantidadDeVertices; i++) {

				float anguloTrig = calcularConFuncion(funcionTrigUsada, j, i);
				float rangoPantalla = p5.map(anguloTrig, -1, 1, radioInterno, radio);

				angulo = (p5.TWO_PI / cantidadDeVertices) * i;

				vertXPos[j][i] = centroX[j] + (rangoPantalla * p5.cos(angulo));
				vertYPos[j][i] = centroY[j] + (rangoPantalla * p5.sin(angulo));

				// DRAW POINTS
				float r = p5.abs(p5.sin(i * 0.01f)) * 255;
				float g = 0;//abs(cos(i * 0.03)) * (j*50);
				float b = p5.abs(p5.sin(i * 0.02f)) * 255;
				drawLayer.fill(r, g, b, brillo);
				drawLayer.ellipse(vertXPos[j][i], vertYPos[j][i], pointSize, pointSize);

				oscTiempo[j][i] += (anguloVel * (i / (float) cantidadDeVertices));

				//if (controlRotacion.getBooleanValue()) {
				oscTiempo[j][i] += oscIncrementoGlobal;
				//}
			}

			//text(j, centroX[j], centroY[j]);
		}

		//fill(255);
		//text(oscTiempo[0][0],10,10);
		rotacion4Circulos += rotacion4CirculosIncr;
	}

	private float calcularConFuncion(int numeroDeFuncion, int circulo, int vertice) {

		float oscTrig = 0;

		switch (numeroDeFuncion) {
		case 0:
			oscTrig = p5.sin(oscTiempo[circulo][vertice]);
			break;
		case 1:
			oscTrig = 1 - p5.cos(oscTiempo[circulo][vertice]);
			break;
		case 2:
			oscTrig = p5.tan(oscTiempo[circulo][vertice]);
			break;
		default:
			oscTrig = 1;
			break;
		}

		return oscTrig;
	}

	public void displaceRotation() {
		for (int i = 0; i < oscTiempo[0].length; i++) {
			oscilationControl = p5.map(p5.mouseY, 0, drawLayer.height, 0, p5.TWO_PI);
			oscTiempo[0][i] = oscilationControl * i;
			oscTiempo[1][i] = oscilationControl * i;
			oscTiempo[2][i] = oscilationControl * i;
			oscTiempo[3][i] = oscilationControl * i;
		}
	}

	// EVENTS FROM A MIDI CONTROLLER - BEGIN ------------

	public void recieveControllerChange(int channel, int number, int value) {
		if (channel == 0) {

			// OSCILATION DISPLACEMENT (POSITION)
			if (number == 0) {
				for (int i = 0; i < oscTiempo[0].length; i++) {
					oscilationControl = p5.map(value, 0, 127, 0, p5.TWO_PI);
					//THIS IS NOT WORKING
					oscTiempo[0][i] = oscilationControl * i;
					oscTiempo[1][i] = oscilationControl * i;
					oscTiempo[2][i] = oscilationControl * i;
					oscTiempo[3][i] = oscilationControl * i;
				}
			}

			if (number == 1) {
				brillo = p5.map(value, 0, 127, 0, 255);
			}

			// SELECT TRIG FUNCTION
			switch (number) {
			case 2:
				funcionTrigUsada = 0;
				break;
			case 3:
				funcionTrigUsada = 1;
				break;
			case 4:
				funcionTrigUsada = 2;
				break;
			default:
				break;
			}
		}

		if (channel == 1) {
			// OSCILATION DISPLACEMENT (VELOCITY FORWARD)
			if (number == 0) {
				for (int i = 0; i < oscTiempo.length; i++) {
					anguloVel = p5.map(value, 0, 127, 0, 0.2f);
				}
			}

			if (number == 1) {
				pointSize = p5.map(value, 0, 127, 1, 200);
			}
		}

		if (channel == 2) {
			// 4 CIRCLES CENTER OFFSET
			if (number == 0) {
				radio4Circulos = p5.map(value, 0, 127, 0, drawLayer.height * 0.5f);
			}
			// INNER RADIUS
			if (number == 1) {
				radioInterno = p5.map(value, 0, 127, 0, drawLayer.height);
			}
		}

		if (channel == 3) {

			// OUTER RADIUS
			if (number == 1) {
				radio = p5.map(value, 0, 127, 0, drawLayer.height);
			}
		}

		if (channel == 4) {

			if (number == 1) {
				// 4 CIRCLES ROTATION VELOCITY
				rotacion4CirculosIncr = p5.map(value, 0, 127, 0, 1.0f);
			}
		}

	}

	public void recieveNoteOn(int channel, int pitch, int velocity) {

	}

	public void recieveNoteOff(int channel, int pitch, int velocity) {

	}

	// EVENTS FROM A MIDI CONTROLLER - END ------------

	@Deprecated
	void dibujarCirculo() {

		//stroke(brillo);
		p5.fill(brillo, 127);
		for (int j = 0; j < centroX.length; j++) {

			for (int i = 0; i < cantidadDeVertices; i++) {
				//line(vertXPos[i],vertYPos[i], vertXPos[(i+1) % cantidadDeVertices], vertYPos[(i+1) % cantidadDeVertices]);
				//line(vertXPos[i], vertYPos[i], centroX, centroY);
				p5.fill(p5.abs(p5.sin(i * 0.01f)) * 255, 0, p5.abs(p5.sin(i * 0.02f)) * 255, 127);
				p5.ellipse(vertXPos[j][i], vertYPos[j][i], pointSize, pointSize);
				//point(vertXPos[i], vertYPos[i]);
			}
		}
	}

}
