package models;

import java.io.Serializable;


//    xxxxxxxx
//   y
//   y
//   y  (x,y)
//   y
public class GameMap implements Serializable{
    private static final long serialVersionUID = -5643934986870766705L;
	private Node[][] nodes;
        private int nodeSize;
        private int player1Element;
	
	public GameMap(int xSize, int ySize){
		this.nodes = new Node[xSize][ySize];
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				this.nodes[i][j] = new Node(i, j);
			}
		}
	}
	
	public Node[][] getMap(){
		return this.nodes;
	}
	
	public int getXSize(){
		return this.nodes.length;
	}
	
	public int getYSize(){
		return this.nodes[0].length;
	}
		
	public void winnifyNode(int x, int y){
		this.nodes[x][y].winnify();
	}
	
	public Node get(int x, int y){
		return this.nodes[x][y];
	}
	
	public void set(int x, int y, int status){
		this.nodes[x][y].status(status);
	}
	
	public void setPlayer1(int x, int y){
		this.set(x, y, Node.PLAYER1);
	}
	
	public void setPlayer2(int x, int y){
		this.set(x, y, Node.PLAYER2);
	}
	
	public void setPlayer1_Win(int x, int y){
		this.set(x, y, Node.PLAYER1_WIN);
	}
	
	public void setPlayer2_Win(int x, int y){
		this.set(x, y, Node.PLAYER2_WIN);
	}

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    public int getPlayer1Element() {
        return player1Element;
    }

    public void setPlayer1Element(int player1Element) {
        this.player1Element = player1Element;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < getXSize(); i++) {
            for (int j = 0; j < getYSize(); j++) {
                s += nodes[i][j].status() + "\t";
            }
            s += "\n";
        }
        return s;
    }
		
    
}
