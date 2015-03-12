import java.util.*;
import java.io.*;
public class Checker {

	private String goal;
	private HashMap<Coordinate, Block> blocks;
	private HashSet<String> goals;
	private Coordinate[][] board;
	private HashSet<Block> allBlocks;
	public HashMap<String,Integer> dim; 

	public boolean win;

	public Checker(int x, int y){

		allBlocks = new HashSet<Block>();
		board = new Coordinate[x][y];
		blocks = new HashMap<Coordinate,Block>();
		goals = new HashSet<String>();
		dim = new HashMap<String,Integer>();

	}
	public Checker(String init, boolean isGoal, int x , int y) {
		if(!isGoal) {
			dim = new HashMap<String,Integer>();
			int [] numsSet;
			numsSet = exitFive(init);
			Tray thai = new Tray(numsSet);
			win = false;
		}
		else {
			int [] numsSet;
			numsSet = exitGoal(init,x,y);
			dim = new HashMap<String,Integer>();
			Tray thai = new Tray(numsSet);
		}
	}
	public Checker copy(Checker other, int x_top, int y_top, int x_to, int y_to) {
		this.board = new Coordinate[other.getBoard().length][other.getBoard()[0].length];
		this.blocks = new HashMap<Coordinate, Block>();
		this.allBlocks = new HashSet<Block>();
		this.dim = other.dim;
		int dx = other.getBoard().length;
		int dy = other.getBoard()[0].length;
		int [] numsSet = new int[other.allBlocks.size()*4 + 2];
		numsSet[0] = dx;
		numsSet[1] = dy;
		int i = 2;
		int k = 0;

		for(Block bl : other.allBlocks) {
			int x = bl.getTop().getX();
			int y = bl.getTop().getY();
			int xi = bl.getBottom().getX();
			int yi = bl.getBottom().getY();
			numsSet[i++] = x;
			numsSet[i++] = y;
			numsSet[i++] = xi;
			numsSet[i++] = yi;
			k++;

		}

		Tray temp2 = new Tray(numsSet);

		this.blocks.get(this.board[x_top][y_top]).move(this.board[x_to][y_to], false);

		this.allBlocks = new HashSet<Block>(this.getAllBlocks());

		return this;
	}

	public Coordinate[][] getBoard() {
		return board;
	}
	public HashMap<Coordinate, Block> getBlocks() {
		return blocks;
	}
	public HashSet<Block> getAllBlocks() {
		HashSet<Block> newAllBlocks = new HashSet<Block>();
		for (Coordinate key : blocks.keySet()) {
			if(!newAllBlocks.contains(blocks.get(key))) {
				newAllBlocks.add(blocks.get(key));
			}	
		}
		return newAllBlocks;
	}

	public Checker(String initialConfiguration , String moveList, String goal) {
		int [] numsSet;
		numsSet = exitFive(initialConfiguration);
		dim = new HashMap<String, Integer>();
		Tray thai = new Tray(numsSet);

		numsSet = exitGoal(goal,numsSet[0],numsSet[1]);

		readGoal(numsSet);

	}
	public int[] exitFive(String file) {
		int[] numSet = new int[5];
		int i = 0;
		int x = 0,y = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String current = "";
			boolean flag = false;
			while((current = in.readLine()) != null) { 
				String[] nums = current.split("\\s+");
				if(i == 0) {
						if(nums.length == 2 ){
							x = Integer.parseInt(nums[0]);
							y = Integer.parseInt(nums[1]);
							if((x < 0 || y < 0)  || (x > 256 || y > 256)) {
								System.exit(5);
							}

							numSet = new int[x*y*4 + 2];
							numSet[i++] = x;
							numSet[i++] = y;
							flag = false;
						}
						else {
							System.exit(5);
						}
				}

				if(i != 2 && nums.length != 4) {
					System.exit(5);
				}
				if(nums.length != 2) {
					for(int j = 0; j < nums.length ; j++) {
						try {
							int n = Integer.parseInt(nums[j]);
						 	if(n < 0 || n >= 256 ) {

								System.exit(5);
							}
							if(j%2 == 0 && n >= x) {
								System.exit(5);
							}
							if(j%2 == 1 && n >= y) {
								System.exit(5);
							}
						} catch (NumberFormatException e) {
							System.exit(5);
						}
					}

				if(flag) {
					numSet[i++] = Integer.parseInt(nums[0]);
					numSet[i++] = Integer.parseInt(nums[1]);
					numSet[i++] = Integer.parseInt(nums[2]);
					numSet[i++] = Integer.parseInt(nums[3]);
				}
			}
			flag = true;
			}
			
		}catch( IOException e) {
			System.exit(3);
		}
		int[] temp = new int[i];
		System.arraycopy(numSet, 0, temp, 0, temp.length);
		return temp;
	}

	public int[] exitGoal(String file, int x, int y) {
		int[] numSet = new int[x * y *4 + 2];
		int i = 0;
		numSet[i++] = x;
		numSet[i++] = y;

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String current = "";

			while((current = in.readLine()) != null) { 

				String[] nums = current.split("\\s+");

				if(nums.length != 4) {
					System.exit(5);
				}
				for(int j = 0; j < nums.length ; j++) {
					try {
						int n = Integer.parseInt(nums[j]);
						if(n < 0 || n >=256) {
							System.exit(5);
						}
						if(n >= x && j%2 == 0) {
							System.exit(5);
						}
						if(n >= y && j%2 == 1) {
							System.exit(5);
						}
					} catch (NumberFormatException e) {
						System.exit(5);
					}
				}
			
					numSet[i++] = Integer.parseInt(nums[0]);
					numSet[i++] = Integer.parseInt(nums[1]);
					numSet[i++] = Integer.parseInt(nums[2]);
					numSet[i++] = Integer.parseInt(nums[3]);
				

			}
			
		}catch( IOException e) {
			System.exit(3);
		}
		int[] temp = new int[i];
		System.arraycopy(numSet, 0, temp, 0, temp.length);
		return temp;
	}


	public void readGoal(int[] numSet) {
		int z = 0;
		goals = new HashSet<String>();

		for(int i = 2; i < numSet.length - 3; i +=4) {	
			int x = numSet[i];
			int y = numSet[i + 1];
			int k = numSet[i + 2];
			int v = numSet[i + 3];

			String s = x + " " + y + " " + k + " "+ v;
			goals.add(s);
			if(board[x][y].isOccupied()) {
				int xi = blocks.get(board[x][y]).getBottom().getX();
				int yi = blocks.get(board[x][y]).getBottom().getY();
				if(xi == k && yi == v) {
					z++;
				}

			}
		}
		if(goals.size() == z) {

			System.exit(0);
		}
	}


	public void play() {
		Scanner in = new Scanner(System.in);
		
		while(in.hasNextLine()) { // check if is legal move later
			String temp = in.nextLine();

			String[] nums = temp.split("\\s+");
			if(nums.length != 4) {
				System.out.println(temp);
				System.exit(4);
			}
			for(int i = 0; i < 4 ; i++) {
				try {
					int n = Integer.parseInt(nums[i]);
					if(n < 0) {
						System.exit(4);
					}

				} catch (NumberFormatException e) {
					System.exit(4);
				}
			}

			int x = Integer.parseInt(nums[0]);
			int y = Integer.parseInt(nums[1]);
			int i = Integer.parseInt(nums[2]);
			int j = Integer.parseInt(nums[3]);
			boolean legal1 = (x == i) || (y == j);
			boolean legal2 = (i < board.length) && (j < board[0].length);
			boolean legal3 = (legal1 && legal2);
			boolean legal4 = (x < board.length) && (y < board[0].length);

			if(!legal3 || !legal4 || !board[x][y].isOccupied()) {
				System.exit(6);
			}
			blocks.get(board[x][y]).move(board[i][j], true);
		}
		if(!win) {
			System.exit(1);
		}
	}

	public void print(){
		System.out.println("blocks size  = " + blocks.size());
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
					System.out.print(board[i][j].getName() +  "  ");
			}
			System.out.println();
		}
	}

	
// ###################### MAIN ################################################################
	public static void main(String[] args){	
		if(args.length != 2) {

			System.exit(2);
		}
		Checker thai = new Checker(args[0],"",args[1]); 
		Scanner in = new Scanner(System.in);
		thai.play();

	}

// ###################### MAIN ################################################################


//--------------------Block------------------begin----------------------------------------
	public class Block {
		private int name;
		private Coordinate top_left;
		private Coordinate bottom_right;


		public Block(){}

		public Block(Coordinate top_left, Coordinate bottom_right) {
			this.top_left = top_left;
			this.bottom_right = bottom_right;
			this.name = 0;
		}

		public Block(Coordinate top_left, Coordinate bottom_right, int name) {
			this.top_left = top_left;
			this.bottom_right = bottom_right;
			this.name = name;
		}
		public Coordinate getTop() {
			return top_left;
		}
		public Coordinate getBottom() {
			return bottom_right;
		}

		public boolean canMoveUp(Coordinate toMoveTo) {
			for(int i = this.top_left.x - 1; i >= toMoveTo.x && i >= 0; i--) {
					for(int j = this.top_left.y; j <= this.bottom_right.y && j < board[0].length; j++) {
						if(board[i][j].isOccupied()) {
							return false;
						}
					}
				}
				return true;
		}

		public boolean canMoveDown(Coordinate toMoveTo, int xDistance) {
			for(int i = this.bottom_right.x + 1; i <= toMoveTo.x + xDistance && i < board.length; i++) {
					for(int j = this.top_left.y; j <= this.bottom_right.y; j++) {
						if(board[i][j].isOccupied()) {
							return false;
						}
					}
				}

				return true;
		}

		public boolean canMoveLeft(Coordinate toMoveTo) {
			for(int i = this.top_left.y - 1; i >= toMoveTo.y ; i--) {
					for(int j = this.top_left.x; j <= this.bottom_right.x; j++) {
						if(board[j][i].isOccupied()) {
							return false;
						}
					}
				}
				return true;
		}

		public boolean canMoveRight(Coordinate toMoveTo, int yDistance, int k) {
			for(int i = this.bottom_right.y + 1; i <= toMoveTo.y + yDistance + k  && i < board[0].length ; i++) {
					for(int j = this.bottom_right.x; j >= this.top_left.x; j--) {
						if(board[j][i].isOccupied()) {
							return false;
						}
					}
				}
				return true;
		}


		public int canMakeMove(Coordinate toMoveTo)  { 
			int k = 1;
			int x = toMoveTo.x;
			int y = toMoveTo.y;
			int x_length = Math.abs(this.top_left.x - this.bottom_right.x) ; 
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) ;
			if(x_length == 1|| y_length == 1) {
				k = 0;
			}
			int yDistance = Math.abs(this.top_left.y - toMoveTo.y);
			int xDistance = Math.abs(this.top_left.x - this.bottom_right.x);
			if(board[x][y].isOccupied() && toMoveTo.getName() != board[x][y].getName()) {
				return 0;
			}
			if(this.top_left.getX() == x && this.top_left.getY() == y) {
				return 0;
			}

			if(x < this.top_left.x  && canMoveUp(toMoveTo)) { //up


				return 1;
			}
			else if(x > this.top_left.x && (x + x_length) <  board.length &&canMoveDown(toMoveTo, xDistance)) { //down

				return 2;
			}
			else if(y < this.top_left.y && canMoveLeft(toMoveTo)) { //left
				return 3;
			}
			else if(y > this.top_left.y && (y + y_length) < board[0].length && canMoveRight(toMoveTo, yDistance, k)) { //right
				return 4;
			}	
			return 0;
		}
		public void move(Coordinate toMoveTo , boolean check){
			int i = canMakeMove(toMoveTo);

			if(i == 1) {
				moving(toMoveTo);

			}
			else if(i == 2) {
				moving(toMoveTo);

			}
			else if(i == 3) {
				moving(toMoveTo);
			}
			else if(i == 4) {
				moving(toMoveTo);
			}
			else if(i == 0 && check){
				System.exit(6);
			}
			if(check) {
				checkGoal();
			}
		}


		public void moving(Coordinate toMoveTo) {
			int x = this.top_left.getX();
			int y = this.top_left.getY();
			int xi = this.bottom_right.getX();
			int yi = this.bottom_right.getY();

			int xlength = Math.abs(x - xi);
			int ylength = Math.abs(y - yi);

			int k = 1;
			if(xlength == 1) {
				k = 0;
			}
			int z = 0;
			for(int i = x; i <= xi ; i++) {
				for(int j = y; j <= yi; j++) {
					z++;
					blocks.remove(board[i][j]);
					board[i][j] = new Coordinate(i,j,0);
				}
			}
			x = toMoveTo.getX();
			y = toMoveTo.getY();
			xi = x + xlength ;
			yi = y + ylength ;
			z = 0;
			for(int i = x; i <= xi ; i++) {
				for(int j = y; j <= yi && j < board[0].length + k; j++) {
					board[i][j] = new Coordinate(i,j,this.name);
					board[i][j].setOccupied(true);
					blocks.put(board[i][j],this);
					z++;
				}
			}

			int x_length = Math.abs(this.top_left.x - this.bottom_right.x); 
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) ;
			this.top_left = toMoveTo;
			this.bottom_right = new Coordinate(toMoveTo.x+ x_length, toMoveTo.y + y_length, this.name);

			
		}


		public boolean impossibleMove(Coordinate toMoveTo) {
			
			int x = toMoveTo.getX();
			int y = toMoveTo.getY();
			int xi = this.top_left.x;
			int yi = this.top_left.y;
			boolean legal1 = (x == xi) || (y == yi);
			boolean legal2 = (x < board.length) && (y < board[0].length);
			return !(legal1 && legal2);

		}

		public void checkGoal() {
			int x = 0;
			int i = 0;
			for(String a: goals) {
				String[] nums = a.split("\\s+");
				int x_top = Integer.parseInt(nums[0]);
				int y_top = Integer.parseInt(nums[1]);
				int xi = Integer.parseInt(nums[2]);
				int yi = Integer.parseInt(nums[3]);

				if(blocks.containsKey(board[x_top][y_top])) {
					int tempX = blocks.get(board[x_top][y_top]).getBottom().getX();
					int tempY = blocks.get(board[x_top][y_top]).getBottom().getY();
					if(xi == tempX && yi == tempY) {
						x++;
					}
				}
			}
			if(x == goals.size()) {
				win = true;
				System.exit(0);
			}
		}

		public boolean outOfBoundsCheck(int x, int y) {
			int length = board.length;
			int width = board[0].length;
			return !((x >=0 && x < width) && (y >= 0 && y < length));  
		}

		/** 
		* @return returns the block's top left coordinate
		*/
		public Coordinate getTopLeft() {
			return this.top_left;
		}

		/**
		* @return returns the block's bottom right coordinate
		*/
		public Coordinate getBottomRight(){
			return this.bottom_right;
		}

		/**
		* @return returns the name of the block
		*/
		public int getName(){
			return name;
		}



		public String toString() {
			return " " + top_left.toString() + " -- " + bottom_right.toString();
		}
	}
//--------------------Block------------------end----------------------------------------


//--------------------Coordinate------------------begin----------------------------------------
	// public Coordinate

	public class Coordinate {
		private int x;
		private int y;
		private boolean occupied;
		private int name;
		private Block goal = null;


		private Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Coordinate(Coordinate other) {
			this.x = other.x;
			this.y = other.y;
			this.name = other.name;
			this.occupied = other.occupied;
		}

		private Coordinate(int x, int y, int name) {
			this.x = x;
			this.y = y;
			this.occupied = false;
			this.name = name;
			
			if(name != 0) {
				this.occupied = true;
			}
		}
		public Coordinate copyCoordinate() {
			return new Coordinate(this.x, this.y, this.name);

		}

		public void setGoal(int n1, int n2 , int n3, int n4) {
			Coordinate top = new Coordinate(n1, n2);
			Coordinate bottom = new Coordinate(n3, n4);
			this.goal = new Block(top, bottom);
		}

		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}

		public Block getGoal(){
			return this.goal;
		}


		/**
		* @return returns a string that we call hashCode() on
		*/
		public String toString() {
			return this.name + ": " + x + " " + y;
		}

		/**
		* @param takes in an object that we compare this to
		* @return returns true if the two objects have the same coordinates
		*/
		
		/**
		* @param takes a boolean that sets the coordinate's occupation truth value
		* @result sets the coordinate's occupation truth value to the passed in boolean
		*/
		public void setOccupied(boolean occupied){
			this.occupied = occupied;
		}

		public boolean isOccupied(){
			return occupied;
		}

		public int getName(){
			return this.name;
		}

		private void setName(int name) {
			this.name = name;
			
		}

	}

	public class Tray {

		/**
		* @param takes a string that denotes the initial configuration of the tray
		* @result populates the 2D array that represents the board with the blocks
		* passed in by the initial configuration
		*/

		public Tray(int[] numSet) {
				
				int i = 0;
				Coordinate top_left;
				Coordinate bottom_right;
				Block temp = new Block();
				allBlocks = new HashSet<Block>();
				blocks  = new HashMap<Coordinate, Block>();
				int x,y,k,z;

				board = new Coordinate[numSet[0]] [numSet[1]];
				populateBoard();
				
				for(int j = 2; j < numSet.length - 3; j += 4) {
					x = numSet[j];
					y = numSet[j + 1];
					k = numSet[j + 2];
					z = numSet[j + 3];
					int name;
					String s = Integer.toString(k-x) + " "  + Integer.toString(z-y);
					
					if(!dim.containsKey(s)) {
						i++;
						dim.put(s,i);
						name = i;
					}
					else {
						name = dim.get(s);
					}
					top_left = new Coordinate(x,y,name);
					bottom_right = new Coordinate(k,z,name);

					temp = new Block(top_left, bottom_right, name);
					allBlocks.add(temp);
					updateBoard(temp);
				}					
		}

		public void updateBoard(Block myBlock) {
			int width = Math.abs(myBlock.top_left.x - myBlock.bottom_right.x);
			int length = Math.abs(myBlock.top_left.y - myBlock.bottom_right.y);
			if(width == 0 && length == 0) {
				board[myBlock.top_left.x][myBlock.bottom_right.y].setOccupied(true);
				board[myBlock.top_left.x][myBlock.bottom_right.y].setName(myBlock.getName());
				blocks.put(board[myBlock.top_left.x][myBlock.bottom_right.y], myBlock);
			}
			else if(width != 0 && length != 0) {
				for(int i = myBlock.top_left.x; i <= myBlock.bottom_right.x && i < board.length; i++) {
					for(int j = myBlock.top_left.y; j <= myBlock.bottom_right.y && j < board[0].length; j++) {
						board[i][j].setOccupied(true);
						board[i][j].setName(myBlock.getName());
						blocks.put(board[i][j], myBlock);
					}
				}

			}
			else if (width != 0 && length == 0) {
				int i = 0;
				while(i <= width) {
					board[myBlock.top_left.x + i][myBlock.top_left.y].setOccupied(true);
					board[myBlock.top_left.x + i][myBlock.top_left.y].setName(myBlock.getName());
					blocks.put(board[myBlock.top_left.x + i][myBlock.top_left.y], myBlock);
					i++;
				}
			}
			else if (width == 0 && length != 0) {
				int j = 0;
				while(j <= length) {
					
					board[myBlock.top_left.x][myBlock.top_left.y + j].setOccupied(true);
					board[myBlock.top_left.x][myBlock.top_left.y + j].setName(myBlock.getName());
					blocks.put(board[myBlock.top_left.x][myBlock.top_left.y + j], myBlock);
					j++;
				}
			}
		}
		public void populateBoard() {
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					board[i][j] =  new Coordinate(i,j,0);
				}
			}
		}
	}
}