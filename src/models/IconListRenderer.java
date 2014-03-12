/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 *
 * @author TinhNgo
 */
public class IconListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        Room room = (Room)value;        
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if(isSelected)
            jp.setBackground(Color.lightGray);
        else
            jp.setBackground(Color.white);
        if (room.getHostType() == Element.FIRE) {
            jp.add(new JLabel(new ImageIcon("src/images/fire.png")));
        } else if (room.getHostType() == Element.ICE) {
            jp.add(new JLabel(new ImageIcon("src/images/ice.png")));
        }
        StringBuilder msg = new StringBuilder("<html>").append(room.getStatus() == Room.WAITTING ? "<font size='3' color='green'>Waiting..</font>" : "<font size='3' color='blue'>Playing..</font>").append(" - <font size='4'><b>").append(room.getName()).append("</b></font></html>");

        jp.add(new JLabel(msg.toString()));
        return jp;
    }
    
    
}