package models;


import java.io.Serializable;
import javax.swing.JPanel;


public class Node extends JPanel implements Serializable{	
	private static final long serialVersionUID = -5643934986870766705L;
        
        public static final int EMPTY = 0;
        public static final int PLAYER1 = 1;
        public static final int PLAYER2 = 2;
        public static final int PLAYER1_WIN = 3;
        public static final int PLAYER2_WIN = 4;
        
	private int status = EMPTY;
	private int x, y;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void winnify() {
		 this.status = this.status == PLAYER1? PLAYER1_WIN : this.status == PLAYER2 ? PLAYER2_WIN : EMPTY;
	}
	
	public int status(){
		return this.status;
	}
	
	public void status(int status){
		this.status = status;
	}
	
	public boolean like(int status){
		return this.status == status;
	}
}

