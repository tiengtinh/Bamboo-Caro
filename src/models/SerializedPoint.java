/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SerializedPoint implements Serializable{
    public int x;
    public int y;

    public SerializedPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public SerializedPoint(Point point){
        this.x = point.x;
        this.y = point.y;
    }
}
