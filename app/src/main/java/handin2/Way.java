package handin2;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

public class Way implements Serializable {
    float[] coords;
    float minLon = Float.MAX_VALUE; //X
    float maxLon = Float.MIN_VALUE;
    float minLat = Float.MAX_VALUE; //Y
    float maxLat = Float.MIN_VALUE;
    float thickness;

    public Way(ArrayList<Node> way) {
        coords = new float[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = (float)(0.56 * node.lon);
            coords[2 * i + 1] = -node.lat;

            if((node.lon) < minLon) {
                minLon = node.lon;
            }
            if((node.lon) > maxLon){
                maxLon = node.lon;
            }
            if((node.lat < minLat)){
                minLat = node.lat;
            } 
            if((node.lat > maxLat)){
                maxLat = node.lat;
            }
        }
    }

    
    //Returns shortest distance from way to a given point. Point is specified in a float[] in order lon lat
    public float distanceToPoint(float[] point) {
        float shortestDist = Float.MAX_VALUE;
        for(int i=0 ; i < (coords.length)/2 ; i++) {
            float dist = (float) (Math.pow(Math.abs(point[0]-(-coords[2*i+1])),2.0) + Math.pow(((point[1]*0.56)-(coords[2*i])),2.0));
            if (dist < shortestDist) {
                shortestDist = dist;
            }
        }
        return shortestDist;
    }

    public void draw(GraphicsContext gc, Colorscheme colors, float determinant) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
    }

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true 
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Way)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members
        Way w = (Way) o;
         
        // Compare the data members and return accordingly
        if(this.coords.length != w.coords.length) {
            return false;
        } else {
            float epsilon = 0.00001f;
            for(int i = 0; i< this.coords.length-1 ; i++) {
                if(Math.abs(this.coords[i]-w.coords[i])>= epsilon) {
                    return false;
                }
            }
        }
        return true;
                
    }

    public float getMinLon() {
        return minLon;
    }

	public float getMinLat() {
		return minLat;
	}

    public float getMaxLon() {
        return maxLon;
    }

	public float getMaxLat() {
		return maxLat;
	}
}