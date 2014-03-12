/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.*;

/**
 *
 * @author TinhNgo
 */
public class CanvasPanel extends javax.swing.JPanel {

    private BoardPainter boardPainter;
    private DashBoardPanel dashBoardPanel;

    private boolean isFinished = false;
    private boolean isPlayer = false;
    
    public CanvasPanel(GameMap map, int nodeSize, int player1Element, DashBoardPanel dashBoardPanel) {
        initComponents();
        this.dashBoardPanel = dashBoardPanel;
        int firePlayer = player1Element == Element.FIRE ? Node.PLAYER1 : Node.PLAYER2;
        int icePlayer = player1Element == Element.ICE ? Node.PLAYER1 : Node.PLAYER2;
        this.boardPainter = new BoardPainter(map, nodeSize,firePlayer , icePlayer);
        this.setPreferredSize(new Dimension(map.getXSize() * nodeSize, map.getYSize() * nodeSize));
        this.setSize(new Dimension(map.getXSize() * nodeSize, map.getYSize() * nodeSize));        
    }

    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        this.boardPainter.paint(g2);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 292, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(isFinished || !isPlayer)
            return;
     
        SerializedPoint point =  new SerializedPoint(evt.getPoint());
        Client client = this.dashBoardPanel.getClient();
        try {
            client.sendInt(Cmd.GO);
            client.sendObject(point);      
        } catch (IOException ex) {
            Logger.getLogger(CanvasPanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_formMouseClicked

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public boolean isFinished() {        
        return isFinished;        
    }
    
    public boolean isPlayer() {        
        return isPlayer;        
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;        
    }

    public void updateScreen(GameMap receivedMap) {
        this.boardPainter.setMap(receivedMap);
        this.repaint();        
    }
}
