package models;

import java.awt.Point;

public class GameCore {
	private static final int WIN_SCORE = 5;
	private GameMap map;
	private int nodeSize;
	
	private int turn = Node.PLAYER1;
        
        private boolean isFinished = false;
	
	public GameCore(GameMap map, int nodeSize) {
		this.map = map;
		this.nodeSize = nodeSize;
	}

	public boolean goAt(SerializedPoint point, int status){
                if(isFinished)
                    return false;
		Point clickedCoor = getClickedCoor(point);
		if(isValidGo(status, clickedCoor)){
			map.get(clickedCoor.x, clickedCoor.y).status(status);
                        alterTurn();
                        return true;
		}
                return false;
		
	}
	
	public Point getClickedCoor(SerializedPoint point){		
		return new Point((int)point.x/nodeSize, (int)point.y/nodeSize);
	}
	
	private boolean isValidGo(int status, Point point) {
                int sta = map.get(point.x, point.y).status();
		return (status == turn && sta == Node.EMPTY);
	}

	public void alterTurn(){
		this.turn = this.turn == Node.PLAYER1 ? Node.PLAYER2 : Node.PLAYER1; 
	}
	
	public boolean isWin(int x, int y, int status){
                if(isFinished)
                    return false;
		boolean isWin = false;
		if((this.countSouth(x, y, status) + this.countNorth(x, y, status)) >= (WIN_SCORE - 1)){
			winnifySouth(x, y, status);
			winnifyNorth(x, y, status);
			isWin = true;
		}else if((this.countEast(x, y, status) + this.countWest(x, y, status)) >= (WIN_SCORE - 1)){
			winnifyEast(x, y, status);
			winnifyWest(x, y, status);
			isWin = true;
		}else if((this.countNorthEast(x, y, status) + this.countSouthWest(x, y, status)) >= (WIN_SCORE - 1)){
			winnifyNorthEast(x, y, status);
			winnifySouthWest(x, y, status);
			isWin = true;
		}else if((this.countNorthWest(x, y, status) + this.countSouthEast(x, y, status)) >= (WIN_SCORE - 1)){
			winnifySouthEast(x, y, status);
			winnifyNorthWest(x, y, status);
			isWin = true;
		}
		if(isWin)
			map.winnifyNode(x, y);
                isFinished = isWin;
		return isWin;	
	}
	
	private void winnifyNorthWest(int x, int y, int status) {
		while(true){
			y--;
			x--;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifyWest(int x, int y, int status) {
		while(true){
			x--;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;		
                        map.get(x, y).winnify();
		}
	}

	private void winnifySouthWest(int x, int y, int status) {
		while(true){
			y++;
			x--;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifySouth(int x, int y, int status) {
		while(true){
			y++;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifySouthEast(int x, int y, int status) {
		while(true){
			y++;
			x++;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifyEast(int x, int y, int status) {
		while(true){
			x++;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifyNorthEast(int x, int y, int status) {
		while(true){
			y--;
			x++;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private void winnifyNorth(int x, int y, int status) {
		while(true){
			y--;
			if(isOutOfBound(x, y) || !map.get(x, y).like(status))
				break;	
                        map.get(x, y).winnify();
		}
	}

	private int countNorth(int x, int y, int status){
		int count = 0;		
		while(true){
			y--;
			if(isInvalidCount(x, y, status))
				break;
			count++;			
		}
		return count;
	}
	
	private int countNorthEast(int x, int y, int status){
		int count = 0;		
		while(true){
			x++;
			y--;
			if(isInvalidCount(x, y, status))
				break;
			count++;
		}
		return count;
	}
	
	private int countEast(int x, int y, int status){
		int count = 0;		
		while(true){
			x++;
			if(isInvalidCount(x, y, status))
				break;
			count++;
		}
		return count;
	}
	
	private int countSouthEast(int x, int y, int status){
		int count = 0;		
		while(true){
			x++;	
			y++;
			if(isInvalidCount(x, y, status))
				break;
			count++;
		}
		return count;
	}
	
	private int countSouth(int x, int y, int status){
		int count = 0;		
		while(true){
			y++;
			if(isInvalidCount(x, y, status))
				break;
			count++;				
		}
		return count;
	}
	
	private int countSouthWest(int x, int y, int status){
		int count = 0;		
		while(true){
			y++;
			x--;
			if(isInvalidCount(x, y, status))
				break;
			count++;				
		}
		return count;
	}
	
	private int countWest(int x, int y, int status){
		int count = 0;		
		while(true){
			x--;
			if(isInvalidCount(x, y, status))
				break;
			count++;			
		}
		return count;
	}
	
	private int countNorthWest(int x, int y, int status){
		int count = 0;		
		while(true){
			x--;
			y--;
			if(isInvalidCount(x, y, status))
				break;
			count++;		
		}
		return count;
	}

	private boolean isInvalidCount(int x, int y, int status) {                 
		return isOutOfBound(x, y) || !map.get(x, y).like(status);
	}

	private boolean isOutOfBound(int x, int y) {                
		return (x < 0 || x >= map.getXSize()  || y <0 || y >= map.getYSize() ) ;
	}

	public GameMap getMap() {
		return map;
	}	
        
        public static int deWinnify(int status){
            if(status == Node.PLAYER1_WIN)
                return Node.PLAYER1;
            else if(status == Node.PLAYER2_WIN)
                return Node.PLAYER2;
            else
                return Node.EMPTY;
        }
}
