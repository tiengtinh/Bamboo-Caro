/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Account;
import models.AccountHolder;
import models.AccountItem;
import models.Element;
import models.Room;
import models.RoomHolder;
import models.SerializedPoint;

/**
 *
 * @author Administrator
 */
public class Server {

    private static final String DB_FILENAME = "accs.txt";
    private static final int GAME_PORT = 6000;
    private ServerSocket gameServerSocket;
    private ServerMainFrame serverMainFrame;
    private HashMap<String, Account> accounts = new HashMap<>();
    private HashMap<String, AccountHolder> accountsOnline = new HashMap<>();
    private HashMap<String, RoomHolder> roomHolders = new HashMap<>();
    private List<GameHandler> gameHandlers = new ArrayList<>();
    private boolean dead = false;

    public Server(ServerMainFrame serverMainFrame) {
        this.serverMainFrame = serverMainFrame;
    }

    public void startServer() {
        try {
            this.dead = false;
            gameServerSocket = new ServerSocket(GAME_PORT);

            loadDb();

            serverMainFrame.printMsb("Server started at: " + new Date() + "\n");
            serverMainFrame.printMsb("Waiting for connection at port " + GAME_PORT + "...\n");
            this.serverMainFrame.changeStatusTS(ServerMainFrame.CONNECTED);
        } catch (Exception e) {
            serverMainFrame.printMsb("The ports is being used!\n");
            this.serverMainFrame.changeStatusTS(ServerMainFrame.DISCONNECTED);
        }
    }

    public void waitForConnections() {
        this.waitForGameConnection();
//        this.waitForChatConnection();
    }

    private void loadDb() {
        try {
            accounts = DBProcessor.loadDb(DB_FILENAME);
        } catch (Exception ex) {
            serverMainFrame.printMsb("Error loading DB!\n");
        }
    }

    private void saveDb() {
        DBProcessor.saveDb(accounts, DB_FILENAME);
    }

    private void waitForGameConnection() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!dead) {
                    try {
                        Socket connection = gameServerSocket.accept();
                        serverMainFrame.printMsb("Connection received from: "
                                + connection.getInetAddress() + connection.getInetAddress() + " on port " + GAME_PORT + "\n");
                        GameHandler gameHandler = new GameHandler(connection, Server.this, serverMainFrame);
                        gameHandlers.add(gameHandler);
                        new Thread(gameHandler).start();
                    } catch (IOException ex) {
                        dead = true;
                    }
                }
                serverMainFrame.printMsb("Game processing terminated\n");
            }
        }).start();
    }

//    private void waitForChatConnection() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (!dead) {
//                    try {
//                        Socket connection = chatServerSocket.accept();
//                        serverMainFrame.printMsb("Connection received from: "
//                                + connection.getInetAddress() + connection.getInetAddress() + " on port " + CHAT_PORT + "\n");
//                        new Thread(new GameHandler(connection, Server.this, serverMainFrame)).start();
//                    } catch (IOException ex) {
//                        dead = true;
//                    }
//                    serverMainFrame.printMsb("Chat processing terminated");
//                }
//            }
//        }).start();  
//    }
    synchronized public boolean login(Account account, GameHandler gameHandler) {
        if (!accountsOnline.containsKey(account.getUsername())) {
            Account serverAccount = accounts.get(account.getUsername());
            if (serverAccount != null && serverAccount.getPassword().equalsIgnoreCase(account.getPassword())) {
                accountsOnline.put(account.getUsername(), new AccountHolder(serverAccount, gameHandler));
                return true;
            }
            return false;
        }
        return false;
    }

    synchronized public boolean register(Account account, GameHandler gameHandler) {
        if (!accounts.containsKey(account.getUsername())) {
            accounts.put(account.getUsername(), account);
            //log this account on
            accountsOnline.put(account.getUsername(), new AccountHolder(account, gameHandler));
            return true;
        }
        return false;
    }

    synchronized public boolean newRoom(Room room, String username) {
        if (!roomHolders.containsKey(room.getName())) {
            RoomHolder roomHolder = new RoomHolder(room);
            AccountHolder accHolder = accountsOnline.get(username);
            accHolder.getAccount().addUpMatchsTaken();
            roomHolder.addAccount(accHolder, new AccountItem(accHolder.getAccount(), room.getHostType()));
            roomHolders.put(room.getName(), roomHolder);

            return true;
        }
        return false;
    }

    public void broadcast(Room room) {
        for (AccountHolder accountHolder : accountsOnline.values()) {
            accountHolder.broadcast(room);
        }
    }

    public void broadcastUpdate(Room room) {
        for (AccountHolder accountHolder : accountsOnline.values()) {
            accountHolder.broadcastUpdate(room);
        }
    }

    synchronized public boolean fightRoom(String roomName, String username) {
        if (roomHolders.containsKey(roomName)) {
            RoomHolder roomHolder = roomHolders.get(roomName);
            roomHolder.getRoom().setStatus(Room.PLAYING);
            AccountHolder accHolder = accountsOnline.get(username);
            accHolder.getAccount().addUpMatchsTaken();
            int element = roomHolder.getRoom().getHostType() == Element.FIRE ? Element.ICE : Element.FIRE;
            roomHolder.addAccount(accHolder, new AccountItem(accHolder.getAccount(), element));
            return true;
        }
        return false;
    }

    synchronized public boolean observeRoom(String roomName, String username) {
        if (roomHolders.containsKey(roomName)) {
            RoomHolder roomHolder = roomHolders.get(roomName);
            AccountHolder accHolder = accountsOnline.get(username);
            roomHolder.addAccount(accHolder, new AccountItem(accHolder.getAccount(), Element.NULL));
            return true;
        }
        return false;
    }

    public boolean broadcast(String roomName, String msg) {
        if (roomHolders.containsKey(roomName)) {
            roomHolders.get(roomName).broadcast(msg);
            return true;
        }
        return false;
    }

    public boolean goAt(String roomName, SerializedPoint point, int player) {
        if (roomHolders.containsKey(roomName)) {
            boolean result = roomHolders.get(roomName).goAt(point, player);
            return result;
        }
        return false;
    }

    public boolean broadcastTitle(String roomName, String msg) {
        if (roomHolders.containsKey(roomName)) {
            roomHolders.get(roomName).broadcastTitle(msg);
            return true;
        }
        return false;
    }

    public void shutdown() {
        this.saveDb();
        dead = true;
        try {
            if (gameServerSocket != null) {
                gameServerSocket.close();
            }
            for (AccountHolder accHolder : accountsOnline.values()) {
                accHolder.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Account findAccount(String username) {
        return this.accountsOnline.get(username).getAccount();
    }

    public List<Room> getRoomsList() {
        List<Room> rooms = new ArrayList<>();
        for (RoomHolder roomHolder : roomHolders.values()) {
            rooms.add(roomHolder.getRoom());
        }
        return rooms;
    }

    public void sendMap(String roomName, String username) {
        accountsOnline.get(username).sendMapNos(roomHolders.get(roomName).getMap());
    }

    public void broadcastUserItems(String roomName) {
        RoomHolder roomHolder = roomHolders.get(roomName);
        if (roomHolder != null) {
            roomHolder.broadcastUserItems();
        }
    }

    public boolean outOfRoom(String roomName, String username) {
        RoomHolder roomHolder = roomHolders.get(roomName);
        if (roomHolder != null) {
            if(roomHolder.removeAccount(username)){
                endRoomExistence(roomName);
            }            
        }
        return true;
    }

    public void addUpMatchesWin(String username) {
        accounts.get(username).addUpMatchsWin();
    }

    public void removeRoom(String roomName) {
        for (AccountHolder acc : accountsOnline.values()) {
            acc.removeRoom(roomName);
        }
//        RoomHolder roomHolder = roomHolders.get(roomName);
//        if (roomHolder != null) {
//            roomHolder.removeRoom();
////            roomHolders.remove(roomName);
//        }
    }

    public boolean playerOutOfRoom(String roomName, String username) {
        RoomHolder roomHolder = roomHolders.get(roomName);
        if (roomHolder != null) {
            String winningMsg = roomHolder.gaveUp(username);
            roomHolder.broadcastTitle(winningMsg);
            if(roomHolder.isRoomEmpty()){
                return true;
            }
        }
        return false;
    }

    public void logOut(String username) {
        accountsOnline.remove(username);
    }

    public void kick(String kickedUsername) {
        accountsOnline.get(kickedUsername).kick();
    }
    
    public void endRoomExistence(String roomName){        
        roomHolders.remove(roomName);        
    }
}
