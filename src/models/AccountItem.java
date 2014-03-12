/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class AccountItem implements Serializable {
    private static final long serialVersionUID = 2L;
    
    private Account account;
    private int element;

    public AccountItem(Account account, int element) {
        this.account = account;
        this.element = element;
    }

    public AccountItem(String username) {
        this.account = new Account(username);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    @Override
    public boolean equals(Object obj) {
        AccountItem accItem = (AccountItem) obj;
        return accItem.getAccount().getUsername().equals(this.account.getUsername());
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.account);
        return hash;
    }
    
    
}
