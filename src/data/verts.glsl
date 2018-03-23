// VARIABLES REQUIRED BY PROCESSING (AS THEY ARE, NO NAME CHANGING!!)
uniform mat4 transform; // MODEL-VIEW
attribute vec4 position; // VERTEX POSITION (TRANSLATION)

void main(){
   //gl_Position = (transform * position);
   gl_Position = (transform * (position + vec4(0.0,-200.0,0.0,0.0)));
}
