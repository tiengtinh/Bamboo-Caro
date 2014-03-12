package models;

import java.io.Serializable;

public class Account implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 4869061236861030860L;
   private String username;
   private String password;
   private String fullname;   
   private int matchsTaken;
   private int matchsWin;

    public Account(String username) {
        this.username = username;
    }

    public Account(String username, String password, String fullname) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        matchsTaken = 0;
        matchsWin = 0;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
    

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getMatchsTaken() {
        return matchsTaken;
    }

    public void setMatchsTaken(int matchsTaken) {
        this.matchsTaken = matchsTaken;
    }

    public int getMatchsWin() {
        return matchsWin;
    }

    public void setMatchsWin(int matchsWin) {
        this.matchsWin = matchsWin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
   
    public void addUpMatchsWin(){
        this.matchsWin++;
    }
    
    public void addUpMatchsTaken(){
        this.matchsTaken++;
    }
}