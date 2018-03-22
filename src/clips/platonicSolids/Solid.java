package clips.platonicSolids;

import processing.core.PGraphics;
import globals.Main;
import globals.PAppletSingleton;

public class Solid {
	Main p5;
	
	float x, y, z;
	float scale;
	float maxScale;
	float growVel;
	float maxAbsoluteScale;

	int c; // color
	float fillAlpha;
	float lineWeight;
	float maxLineWeight;

	boolean visible, hasFill, hasStroke;

	//PImage tex;
	
	PGraphics drawLayer;
	
	public Solid(){
		p5 = getP5();
		
	    z = y = x = 0;
	    scale = 0.01f;
	    growVel = 1.05f;
	    maxScale = 1.0f;
	    maxAbsoluteScale = 500;

	    c = p5.color(255, 0, 0);
	    fillAlpha = 100;
	    lineWeight = 0.0f;
	    maxLineWeight = 0.08f;
	    
	    visible = true;
	    hasFill = false;
	    hasStroke = true;
	}
	
	public void setDrawLayer(PGraphics dLayer){
		drawLayer = dLayer;
	}
	
	void update() {
	    scale *= growVel;
	    
	    if ((scale * maxAbsoluteScale) > maxAbsoluteScale) {
	      scale = p5.random(0.001f,0.01f);
	      
	      visible = p5.random(1) > 0.5 ? true : false;
	    }
	  }

	  void render() {
	    if(hasStroke){drawLayer.stroke(c);drawLayer.strokeWeight(lineWeight);} else {drawLayer.noStroke();}
	    if(hasFill){drawLayer.fill(c,fillAlpha);}else{drawLayer.noFill();};
	    // THEN IT GOES ON RENDERING subClasses.render();
	  }
	  
	  /*
	  void setTexture(PImage img){
	   tex = img; 
	  }
	  */

	  void setPosition(float _x, float _y, float _z) {
	    x = _x;
	    y = _y;
	    z = _z;
	  }

	  void setScale(float _scale) {
	    scale = _scale;
	    //println("Scale: " + scale);
	  }
	  
	  void setMaxAbsoluteScale(float _max){
	    maxAbsoluteScale = _max;
	  }

	  void setColor(int _c) {
	    c = p5.color(_c); 
	    //println("Color: " + red(c));
	  }
	  
	  void setOpacity(float alpha){
	   fillAlpha = alpha; 
	  }
	  
	  void setLineWeight(float _lineWeight){
	    lineWeight = _lineWeight;
	  }
	  
	  /*
	  PVector[] get2RandomVertex(){
	   return null; 
	  }
	  */
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
