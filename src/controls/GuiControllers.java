package controls;

import processing.core.PVector;
import globals.ClipManager;
import globals.Main;
import globals.PAppletSingleton;
import controlP5.*;

public class GuiControllers {
	Main p5;
	ClipManager clipManager;
	ControlP5 controllers;
	PVector position;
	
	ScrollableList clipSelector;

	public GuiControllers(ClipManager cm) {
		p5 = getP5();
		clipManager = cm;
		controllers = new ControlP5(p5);
		position = new PVector(p5.width - 300, 0);
		
		createControllers();
		

	}

	public void render() {
		p5.fill(0, 127);
		p5.stroke(255);
		p5.rect(position.x, position.y, 300, p5.height);
	}

	private void createControllers() {

		clipSelector = controllers.addScrollableList("clipSelector")
		.setLabel("   - CLIPS -")
		.setPosition(position.x + 10, position.y + 20)
		.setSize(200, 100)
		.setBarHeight(20)
		.setItemHeight(20)
		.setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
		.setColorActive(p5.color(255,0,0))

		;
		

	}
	
	public void addClipItem(String clipName){
		clipSelector.addItem(clipName, clipSelector.getItems().size());
		
		CColor backColor = new CColor();
		backColor.setBackground(p5.color(50));
		clipSelector.getItem(clipName).put("color", backColor);
	}
	
	public void controlEvent(ControlEvent event){
	    // ControlEvent FORWARDED FROM PApplet Main (SINCE ControlP5 REGISTERS THERE)
		p5.println("-|| ControlEvent: " + event.getName() + " = " + event.getValue());
		
		if (event.isFrom("clipSelector")) {
			clipManager.selectedClip = (int)event.getValue();
		}
	}
	
	public void setPlayingClip(String clipName){
		
		for (int i = 0; i < clipSelector.getItems().size(); i++) {
			CColor backColor = new CColor();
			if (clipSelector.getItem(i).get("name").toString().equals(clipName)) {
				backColor.setBackground(p5.color(255,0,0));
				clipSelector.getItem(clipName).put("color", backColor);
			} else {
				backColor.setBackground(p5.color(50));
				clipSelector.getItem(i).put("color", backColor);
			}
		}
		
		//controllers.update();
	}
	

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
