package tools;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PVector;

public class TempoManager {

	HEY;
	//EVERYTHING WORKS BUT I'M STILL TESTING IN PROCESSING IDE.
	//SOMETIMES IT SKIPS A BEAT
	
	Main p5;

	float bpm;
	float beatDivision;
	Timer timer;
	boolean onBeat;

	boolean onTapTempoMode = false;
	boolean tapped = false;
	long lastTapTime = 0;
	long tapDifference = 0;

	// VIZ
	PVector beatMarkerPos;
	float beatMarkerSize;

	public TempoManager() {
		p5 = getP5();

		timer = new Timer();

		beatDivision = 4; // 1 = ON QUARTER NOTE (NEGRA) -> LO NORMAL
		setBPM(60);
		onBeat = false;

		beatMarkerPos = new PVector(p5.width * 0.5f, 100);
		beatMarkerSize = 50;
	}

	public void update() {

		// TAP TEMPO MODE - BEGIN
		if (tapped) {
			if (!onTapTempoMode) {
				onTapTempoMode = true;
				lastTapTime = System.currentTimeMillis();
			} else {
				tapDifference = System.currentTimeMillis() - lastTapTime;
				lastTapTime = System.currentTimeMillis();
				p5.println("-|| TAP DIFF: " + tapDifference);
				setBpmFromMillis((int) tapDifference);
			}

			tapped = false;
		}
		// IF MORE THAN 5 SECONDS WENT BY, EXIT onTapTempoMode
		if (onTapTempoMode && (System.currentTimeMillis() - lastTapTime) > 5000) {
			onTapTempoMode = false;
			p5.println("-|| EXITING TAP TEMPO MODE");
		}

		// TAP TEMPO MODE - END

		// NORMAL TIMER
		if (timer.isFinished()) {
			onBeat = true;
			//p5.println("--------O");
			start();
		} else {
			onBeat = false;
		}
	}

	public void render() {

		p5.stroke(127);
		p5.noFill();
		p5.ellipse(beatMarkerPos.x, beatMarkerPos.y, beatMarkerSize, beatMarkerSize);

		p5.noStroke();
		if (isOnBeat()) {
			p5.fill(0, 255, 127);
		} else {
			p5.noFill();
		}
		p5.ellipse(beatMarkerPos.x, beatMarkerPos.y, beatMarkerSize * 0.8f, beatMarkerSize * 0.8f);
	}

	public void setBPM(int _bpm) {
		bpm = _bpm;
		calculateTimerDuration();
		start();
	}

	public int getBPM() {
		return (int) bpm;
	}

	public void setBeatDivision(int division) {
		beatDivision = division * 4.0f;
		calculateTimerDuration();
	}

	public int getBeatDivision() {
		return (int) beatDivision;
	}

	private void calculateTimerDuration() {
		float millisInMinute = 60000;
		int timerDuration = (int) ((millisInMinute / bpm) / (beatDivision / 4.0));
		timer.setDuration(timerDuration);
	}

	private void setBpmFromMillis(int millisecs) {
		float toBpm = (1000.0f / millisecs) * 60;
		setBPM((int) toBpm);
		p5.println("-|| NEW BPM: " + toBpm);
	}

	public void start() {
		timer.start();
	}

	public boolean isOnBeat() {
		return onBeat;
	}

	// TAP TEMPO
	public void tap() {
		tapped = true;
	}

	public boolean isOverTapMarker(float x, float y) {
		return p5.dist(x, y, beatMarkerPos.x, beatMarkerPos.y) < beatMarkerSize * 0.5;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
