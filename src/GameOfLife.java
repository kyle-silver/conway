
public class GameOfLife {

	public boolean[][] board;
	public final int xSize;
	public final int ySize;
	
	public GameOfLife(boolean[][] startingBoard){
		board = startingBoard;
		xSize = board.length;
		ySize = board[0].length;
	}
	
	public void step(){
		
		int numberOfNeighbors;
		
		boolean[][] newBoard = new boolean[xSize][ySize];
		
		for(int x = 0; x < xSize; x++){
			for(int y = 0; y < ySize; y++){
				
				numberOfNeighbors = getNumberOfNeighbors(x, y);
												
				if(numberOfNeighbors == 3 && !board[x][y]){
					newBoard[x][y] = true;
				} else if ((numberOfNeighbors == 2 || numberOfNeighbors == 3) && board[x][y]) {
					newBoard[x][y] = true;
				}
				
			}
		}
		
		board = newBoard;
		
	}
	
	public int getNumberOfNeighbors(int xPos, int yPos){
		int total = 0;
		
		if(board[mod(xPos-1,xSize)][mod(yPos-1,ySize)]){
			total++;
		}
		if(board[xPos][mod(yPos-1,ySize)]){
			total++;
		}
		if(board[mod(xPos+1,xSize)][mod(yPos-1,ySize)]){
			total++;
		}
		if(board[mod(xPos-1,xSize)][yPos]){
			total++;
		}
		if(board[mod(xPos+1,xSize)][yPos]){
			total++;
		}
		if(board[mod(xPos-1,xSize)][mod(yPos+1,ySize)]){
			total++;
		}
		if(board[xPos][mod(yPos+1,ySize)]){
			total++;
		}
		if(board[mod(xPos+1,xSize)][mod(yPos+1,ySize)]){
			total++;
		}
		
		return total;
	}
	
	public String toString(){
		String toString = "";
		
		for(int x = 0; x < xSize; x++){
			toString += "\t.";
			for(int y = 0; y < ySize; y++){
				toString += board[x][y] ? "X" : " ";
			}
			toString += ".\n";
		}
		
		return toString;
	}
	
	
	private static int mod(int x, int base){
		return x >= 0 ? x % base : base + (x % base);
	}
	
}
