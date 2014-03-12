/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import models.Account;
import models.Cmd;
import models.Element;
import models.Node;
import models.Room;
import models.SerializedPoint;

/**
 *
 * @author Administrator
 */
public class GameHandler extends AbstractHandler {
    
    private String username = null;
    private String roomName = null;
    private int element = Element.NULL;
    private int player = Node.EMPTY;
    
    public GameHandler(Socket connection, Server server, ServerMainFrame serverMainFrame) {
        super(connection, server, serverMainFrame);
        
    }
    
    @Override
    protected void processConnection() throws IOException {
        while (!dead) {
            int cmd = readInt();
            if (cmd == Cmd.LOGIN) {
                Account account = (Account) readObject();
                if (account != null && this.server.login(account, this)) {
                    username = account.getUsername();
                    sendBoolean(true);
                    sendObject(this.server.findAccount(username));
                } else {
                    sendBoolean(false);
                }
            } else if (cmd == Cmd.REGISTER) {
                Account account = (Account) readObject();
                if (account != null && this.server.register(account, this)) {
                    username = account.getUsername();
                    sendBoolean(true);
                } else {
                    sendBoolean(false);
                }
            } else if (cmd == Cmd.ALL_ROOMS) {
                this.sendInt(Cmd.ALL_ROOMS);
                this.sendObject(this.server.getRoomsList());
            } else if (cmd == Cmd.NEW_ROOM) {
                Room room = (Room) readObject();
                if (room != null && this.server.newRoom(room, username)) {
                    roomName = room.getName();
                    element = room.getHostType();
                    player = Node.PLAYER1;
                    
                    sendInt(Cmd.BOOL_NEW_ROOM);
                    sendBoolean(true);
                    this.server.broadcast(room);
                    this.server.broadcastUserItems(roomName);
                    
                } else {
                    sendInt(Cmd.BOOL_NEW_ROOM);
                    sendBoolean(false);
                }
            } else if (cmd == Cmd.FIGHT_ROOM) {
                Room room = (Room) readObject();
                if (room != null && this.server.fightRoom(room.getName(), username)) {
                    roomName = room.getName();
                    element = room.getHostType() == Element.FIRE ? Element.ICE : Element.FIRE;
                    room.setStatus(Room.PLAYING);
                    player = Node.PLAYER2;
                    
                    this.server.sendMap(roomName, username);
                    
                    sendInt(Cmd.BOOL_FIGHT_ROOM);
                    sendBoolean(true);
                    
                    this.server.broadcast(roomName, createMsg("<i>join the room and ready to fight!</i>"));
                    this.server.broadcastUserItems(roomName);
                    this.server.broadcastUpdate(room);
                } else {
                    sendInt(Cmd.BOOL_FIGHT_ROOM);
                    sendBoolean(false);
                }
            } else if (cmd == Cmd.OBSERVE_ROOM) {
                Room room = (Room) readObject();
                if (room != null && this.server.observeRoom(room.getName(), username)) {
                    roomName = room.getName();
                    element = Element.NULL;
                    
                    this.server.sendMap(roomName, username);
                    
                    sendInt(Cmd.BOOL_OBSERVE_ROOM);
                    sendBoolean(true);
                    
                    this.server.broadcast(roomName, createMsg("<i>join the room!</i>"));
                    this.server.broadcastUserItems(roomName);
                } else {
                    sendInt(Cmd.BOOL_OBSERVE_ROOM);
                    sendBoolean(false);
                }
            } else if (cmd == Cmd.SEND_MSG) {
                String msg = readMsg();
                if (msg != null) {
                    this.server.broadcast(roomName, createMsg(msg));
                }
            } else if (cmd == Cmd.GO) {
                if (player != Node.EMPTY) {
                    SerializedPoint point = (SerializedPoint) readObject();
                    if (point != null && this.server.goAt(roomName, point, player)) {
                        sendInt(Cmd.BOOL_FINISHED);
                        sendBoolean(true);//game is finished                            
                        this.server.broadcastTitle(roomName, createTitle("WON!"));
                        this.server.addUpMatchesWin(username);
                        this.server.removeRoom(roomName);
                    } else {
                        sendInt(Cmd.BOOL_FINISHED);
                        sendBoolean(false);//game is not finished
                    }
                }
            } else if (cmd == Cmd.OUT_ROOM) {
                if (this.server.outOfRoom(roomName, username)) {
                    this.server.broadcast(roomName, createMsg("<i>out of room!</i>"));
                    this.server.broadcastUserItems(roomName);
                }
            } else if (cmd == Cmd.PLAYER_OUT_ROOM) {
                boolean roomEmpty = this.server.playerOutOfRoom(roomName, username);
                this.server.broadcast(roomName, createMsg("<i>gave up!</i>"));
                this.server.broadcastUserItems(roomName);
                this.server.removeRoom(roomName);
                if (roomEmpty) {
                    this.server.endRoomExistence(roomName);
                }
            } else if (cmd == Cmd.LOG_OUT) {
                if (username != null) {
                    this.server.logOut(username);
                    sendInt(Cmd.BOOL_LOG_OUT);
                    this.dead = true;
                }
            } else if (cmd == Cmd.KICK) {
                String kickedUsername = readMsg();
                if (kickedUsername != null) {
                    this.server.kick(kickedUsername);
                }
            }
        }
    }
    
    @Override
    public void run() {
        try {
            getDataInPutStream();
            getDataOutPutStream();
            getObjectInPutStream();
            getObjectOutPutStream();
            processConnection();
        } catch (IOException ex) {
            ex.printStackTrace();
            this.serverMainFrame.printMsb("Client " + connection.getInetAddress() + " disconnected!\n");
        }
    }
    
    private String createMsg(String msg) {
        String color = element == Element.FIRE ? "red" : element == Element.ICE ? "blue" : "black";
        return "<div><b><font color='" + color + "'>" + username + ": </font></b>" + msg + "<div>";
    }
    
    public String createTitle(String msg) {
        String color = element == Element.FIRE ? "red" : element == Element.ICE ? "blue" : "black";
        return "<html><div><font color='" + color + "'>" + username + " </font>" + msg + "<div></html>";
    }
}
