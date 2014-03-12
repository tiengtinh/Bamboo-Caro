/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import models.Account;

/**
 *
 * @author Administrator
 */
public class DBProcessor {

    public static HashMap<String, Account> loadDb(String dbName) throws Exception {
        FileInputStream fis;
        ObjectInputStream ois = null;

        HashMap<String, Account> accounts = new HashMap<>();

        try {
            fis = new FileInputStream(dbName);
            ois = new ObjectInputStream(fis);
            accounts = (HashMap<String, Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            File file = new File(dbName);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
            }
        }
        return accounts;        
    }
    
    public static boolean saveDb(HashMap<String, Account> accounts, String dbName) {        
        if (accounts != null) {
            try {
                FileOutputStream fos = new FileOutputStream(dbName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(accounts);
                oos.flush();
                oos.close();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }
}
