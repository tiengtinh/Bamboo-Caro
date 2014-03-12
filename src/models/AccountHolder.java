/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import server.GameHandler;

/**
 *
 * @author Administrator
 */
public class AccountHolder {
    private Account account;
    private GameHandler gameHandler;
    
    private int element;

    public AccountHolder(Account account, GameHandler gameHandler) {
        this.account = account;
        this.gameHandler = gameHandler;
    }

    public AccountHolder(String username) {
        this.account = new Account(username);
    }
    
    public void setElement(int element){
        this.element = element;
    }
    
    public int getElement(){
        return element;
    }

    public void broadcast(Room room) {
        gameHandler.sendInt(Cmd.UPDATE_NEW_ROOM);
        gameHandler.sendObject(room);
    }

    public void broadcastUpdate(Room room) {
        gameHandler.sendInt(Cmd.UPDATE_STATUS_ROOM);
        gameHandler.sendObject(room);
    }

    public void sendMsg(String msg) {
        gameHandler.sendInt(Cmd.BROA_MSG);
        gameHandler.sendMsg(msg);
    }

    public void sendMap(GameMap map) {
        gameHandler.sendInt(Cmd.BROA_MAP);
//        System.out.println(map);
        gameHandler.sendObject(map);
    }

    public void sendTitle(String msg) {
        gameHandler.sendInt(Cmd.BROA_TITLE);
        gameHandler.sendMsg(msg);
    }

    public Account getAccount() {
        return account;
    }

    public void sendMapNos(GameMap map) {
        gameHandler.sendInt(Cmd.BROA_MAP_NO);
        gameHandler.sendInt(map.getXSize());
        gameHandler.sendInt(map.getYSize());
        gameHandler.sendInt(map.getNodeSize());
        gameHandler.sendInt(map.getPlayer1Element());
        for (int i = 0; i < map.getXSize(); i++) {
            for (int j = 0; j < map.getYSize(); j++) {
                gameHandler.sendInt(map.get(i, j).status());
            }
        }
    }

    public void broadcast(List<AccountItem> accountItems) {
        gameHandler.sendInt(Cmd.BROA_USER);
        gameHandler.sendInt(accountItems.size());
        for (int i = 0; i < accountItems.size(); i++) {
            gameHandler.sendObject(accountItems.get(i));
        }        
    }

    public void close() {
        try {
            gameHandler.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {        
        AccountHolder accHolder = (AccountHolder) obj;
        return accHolder.getAccount().getUsername().equals(this.account.getUsername());
    }

    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.account);
        return hash;
    }
    
    public void removeRoom(String roomName){
        this.gameHandler.sendInt(Cmd.REMOVE_ROOM);
        this.gameHandler.sendMsg(roomName);
        
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void kick() {
        this.gameHandler.sendInt(Cmd.KICK);
    }
    
    
}
