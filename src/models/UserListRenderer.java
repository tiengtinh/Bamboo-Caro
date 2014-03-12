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
public class UserListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        AccountItem accountItem = (AccountItem)value;        
        JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        jp.setBackground(Color.white);
        JButton jb = new JButton(accountItem.getAccount().getUsername());
        jb.setBackground(Color.WHITE);
        if(accountItem.getElement() == Element.FIRE)
            jb.setIcon(new ImageIcon("src/images/fire-i.png"));
        else if (accountItem.getElement() == Element.ICE)
            jb.setIcon(new ImageIcon("src/images/ice-i.png"));
        else
            jb.setIcon(new ImageIcon("src/images/icon_clover12.png"));
        jp.add(jb);
        return jp;
    }
    
    
}