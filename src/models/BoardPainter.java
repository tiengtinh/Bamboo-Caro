package models;


import models.GameMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BoardPainter {

    private static final String BG_IMG_PATH = "src/images/bamboo.png";
    private static final String HOR_BAR_IMG_PATH = "src/images/bar_hor.png";
    private static final String VER_BAR_IMG_PATH = "src/images/bar_ver.png";
    private static final String FIRE_PLAYER_IMG_PATH = "src/images/fire.png";
    private static final String ICE_PLAYER_IMG_PATH = "src/images/ice.png";       
    private static final String FIRE_DEAD_IMG_PATH = "src/images/fire_dead.png";
    private static final String ICE_DEAD_IMG_PATH = "src/images/ice_dead.png";
    
    private Image imgFirePlayer;
    private Image imgIcePlayer;
    private Image imgFireDead;
    private Image imgIceDead;
    
    private GameMap map;
    private BufferedImage imgBg;
    private int nodeSize;
    
    private int firePlayer;
    private int icePlayer;
    
    private MediaTracker tracker;

    public BoardPainter(GameMap map, int nodeSize, int firePlayer, int icePlayer) {
        this.firePlayer = firePlayer;
        this.icePlayer = icePlayer;
        this.map = map;
        this.nodeSize = nodeSize;
        createBoardBG();
        loadImages();
    }
    
    private void loadImages(){
        try {
            imgFirePlayer = ImageIO.read(new File(FIRE_PLAYER_IMG_PATH));
            imgIcePlayer = ImageIO.read(new File(ICE_PLAYER_IMG_PATH));
            imgFireDead = ImageIO.read(new File(FIRE_DEAD_IMG_PATH));
            imgIceDead = ImageIO.read(new File(ICE_DEAD_IMG_PATH));
        } catch (IOException ex) {
            System.err.println("Error loading images!");
        }
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(imgBg, 0, 0, null);
        drawNodes(g);
    }

    private void createBoardBG() {
        int width = map.getXSize() * nodeSize;
        int height = map.getYSize() * nodeSize;
        imgBg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) imgBg.getGraphics();   
        
        int diffVar = 5;

        try {
            g.drawImage(ImageIO.read(new File(BG_IMG_PATH)), 0, 0, null);
            g.setColor(Color.white);
            for (int i = 1; i < map.getXSize(); i++) {
                //g.fillRect(i * nodeSize, 0, line, height);
                for (int j = 0; j < map.getYSize(); j++) {
                    g.drawImage(ImageIO.read(new File(VER_BAR_IMG_PATH)), i * nodeSize - diffVar, j * nodeSize, null);
                    g.drawImage(ImageIO.read(new File(HOR_BAR_IMG_PATH)), j * nodeSize, i * nodeSize - diffVar, null);
                }                
            }            
        } catch (IOException e) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
        }
    }

    private void drawNodes(Graphics2D g) {        
        for (int i = 0; i < map.getXSize(); i++) {
            for (int j = 0; j < map.getYSize(); j++) {
                int node = map.get(i, j).status();
//                System.out.println(node);
                if(node == icePlayer)
                    g.drawImage(imgIcePlayer, i * nodeSize, j * nodeSize, null);
                else if(node == firePlayer)                
                    g.drawImage(imgFirePlayer, i * nodeSize, j * nodeSize, null);
                else if(GameCore.deWinnify(node) == icePlayer)
                    g.drawImage(imgIceDead, i * nodeSize, j * nodeSize, null);
                else if(GameCore.deWinnify(node) == firePlayer)
                    g.drawImage(imgFireDead, i * nodeSize, j * nodeSize, null);
            }
        }
    }

    public void setMap(GameMap receivedMap) {
        this.map = receivedMap;
    }
}
