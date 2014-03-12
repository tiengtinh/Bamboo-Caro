/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class RoomHolder {
    private Room room;
    private List<AccountHolder> accountHolders = new ArrayList<>();
    private List<AccountItem> accountItems = new ArrayList<>();
    private GameCore gameCore;

    public RoomHolder(Room room) {
        this.room = room;        
        gameCore = new GameCore(room.getMap(), room.getMap().getNodeSize());
    }
    

    public void addAccount(AccountHolder accountHolder, AccountItem accountItem) {
        accountHolders.add(accountHolder);
        accountItems.add(accountItem);
    }

    public void broadcast(String msg) {
        for (AccountHolder accountHolder : accountHolders) {
            accountHolder.sendMsg(msg);
        }
    }
    
    public GameMap getMap(){
        return gameCore.getMap();
    }

    public boolean goAt(SerializedPoint point, int player) {
        boolean result = false;
        if(gameCore.goAt(point, player)){
            Point clickedPoint = gameCore.getClickedCoor(point);
            result = gameCore.isWin(clickedPoint.x, clickedPoint.y, player);        

            for (AccountHolder accountHolder : accountHolders) {            
                accountHolder.sendMapNos(gameCore.getMap());
            }
        }
        return result;
    }

    public void broadcastTitle(String msg) {
        for (AccountHolder accountHolder : accountHolders) {
            accountHolder.sendTitle(msg);
        }
    }

    public Room getRoom() {
        return this.room;
    }

    public void broadcastUserItems() {
        for (AccountHolder accountHolder : accountHolders) {
            accountHolder.broadcast(accountItems);
        }
    }

    public boolean removeAccount(String username) {
        AccountItem accItem = new AccountItem(username);
        AccountHolder accHolder = new AccountHolder(username);
        this.accountHolders.remove(accHolder);
        this.accountItems.remove(accItem);
        return isRoomEmpty();
    }

    public void removeRoom() {
        for (AccountHolder accountHolder : accountHolders) {
            accountHolder.removeRoom(room.getName());
        }
        
    }       

    public String gaveUp(String username) {
        String winningMsg = "";
        if(accountHolders.size() > 1){
            AccountHolder accHolder1 = accountHolders.get(0);
            AccountHolder accHolder2 = accountHolders.get(1);
            if(accHolder1.getAccount().getUsername().equals(username)){                
                accHolder2.getGameHandler().sendInt(Cmd.BOOL_FINISHED);
                accHolder2.getGameHandler().sendBoolean(true);
                accHolder2.getAccount().addUpMatchsWin();
                winningMsg = accHolder2.getGameHandler().createTitle("WON!");
            }else{
                accHolder1.getGameHandler().sendInt(Cmd.BOOL_FINISHED);
                accHolder1.getGameHandler().sendBoolean(true);
                accHolder1.getAccount().addUpMatchsWin();
                winningMsg = accHolder1.getGameHandler().createTitle("WON!");
            }
        }
        removeRoom();
        removeAccount(username);        
        return winningMsg;
    }

    public boolean isRoomEmpty(){
        return accountHolders.size() <= 0;
    }
}
