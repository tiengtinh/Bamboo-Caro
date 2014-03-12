/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class Room implements Serializable{
    private static final long serialVersionUID = 4869061236861030899L;        
    
    public static final int WAITTING = 1;
    public static final int PLAYING = 2;
    
    private String name;        
    private int status;
    private int hostType;
    
    private GameMap map;    

    public Room(String name, int hostType) {
        this.name = name;
        this.hostType = hostType;
        this.status = WAITTING;
    }

    public int getHostType() {
        return hostType;
    }

    public void setHostType(int hostType) {
        this.hostType = hostType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }    
}
    