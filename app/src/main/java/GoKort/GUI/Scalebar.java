package GoKort.GUI;

public class Scalebar {
    //the amount of meters on the scalebar in meters
    float scale;

    //sets limits of zoomlevel in meters
    float minZoomlevel = 5;
    float maxZoomlevel = 100000;

    public Scalebar(float totalLat, float canvasHeight, float scaleBarsize) {
        int latInMeters = 111000;
        this.scale = (totalLat*latInMeters)/canvasHeight*scaleBarsize;
    }
    public float getScale() {
        return scale;
    }

    public float setScale(float zoomspeed) {
        //checks if trying to zoom past minZoomlevel
        if(scale/zoomspeed < minZoomlevel) {  
            zoomspeed = scale/minZoomlevel;
            scale = minZoomlevel; 
            return  zoomspeed; 
        }
        //checks if trying to zoom past maxZoomlevel
        else if (scale/zoomspeed > maxZoomlevel) {
            zoomspeed = scale/maxZoomlevel;
            scale = maxZoomlevel; 
            return  zoomspeed; 
        }
        
        scale = scale/zoomspeed; return zoomspeed;
    }

    public String getScaleLabel() {     
        if(scale<1000) {
            return (int) scale + "m";
        }
        else if (scale >= 1000 && scale < 10000){
            float temp = (int) scale/10;    
            return temp/100+ "km"; 
        } 
        else {
            float temp = (int) scale/100;
            return temp/10+ "km"; 
        }
    }

}