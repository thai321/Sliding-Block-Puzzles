import java.util.*;
import java.io.*;
public class Checker {
	private String goal;
	private HashMap<Coordinate, Block> blocks;
	private HashSet<Coordinate> goals;
	private Coordinate[][] board;
	private HashSet<Block> allBlocks;
	public boolean win;

	public Checker(int x, int y){
		// initialToGoal = new HashMap<Block, Block>();
		allBlocks = new HashSet<Block>();
		board = new Coordinate[x][y];
		blocks = new HashMap<Coordinate,Block>();
		goals = new HashSet<Coordinate>();

	}
	public Checker(String init, boolean isGoal, int x , int y) {
		if(!isGoal) {
			int [] numsSet;
			numsSet = exitFive(init);
			Tray thai = new Tray(numsSet);
			win = false;
		}
		else {
			int [] numsSet;
			numsSet = exitGoal(init,x,y);
			Tray thai = new Tray(numsSet);
		}
	}
	public Checker copy(Checker other, int x_top, int y_top, int x_to, int y_to) {
		this.board = new Coordinate[other.getBoard().length][other.getBoard()[0].length];
		this.blocks = new HashMap<Coordinate, Block>();
		this.allBlocks = new HashSet<Block>(other.getAllBlocks());
		
		for(int i = 0; i < other.getBoard().length; i++) {
			for(int j = 0; j < other.getBoard()[0].length; j++) {
				this.board[i][j] = new Coordinate(i,j,other.getBoard()[i][j].getName());
				if(this.board[i][j].isOccupied()) {
					Block bl = other.getBlocks().get(other.getBoard()[i][j]);
					
					Block bl2 = new Block(bl.getTop(), bl.getBottom(), bl.getName());

					this.blocks.put(this.board[i][j], bl2);
					// if(i == 5 && j == 59) {
					// 	if(this.blocks.get(this.board[i][j]).getName() == 2) {
					// 		System.out.println("HERERERERE");
					// 		System.exit(1);
					// 	}
					// }
				}
			}
		}

		// System.out.println(this.board[x_to][y_to].getName());
		// System.out.println(this.board[x_top][y_top].getName());
		// if(this.blocks.get(this.board[x_top][y_top]) == null) {
		// 	System.out.println("NULL");
		// }
		// if(x_to == 5 && y_to	== 99) {
		// 	System.out.println("!!!!");
		// 	System.out.println(blocks);
		// 	this.blocks.get(this.board[x_top][y_top]).move(this.board[x_to][y_to], false);
		// 	System.out.println(blocks);
		// 	System.exit(1);
		// }
		System.out.println("x_top = "+ x_top + ", y_top = " + y_top);
		System.out.println(blocks);
		// System.exit(1);
		this.blocks.get(this.board[x_top][y_top]).move(this.board[x_to][y_to], false);
		System.out.println("x_to = "+ x_to + ", y_to = " + y_to);
		this.allBlocks = new HashSet<Block>(this.getAllBlocks());
		// if()
		if(this.board[5][99].getName() == 2) {
			System.out.println("???");
			
			System.exit(1);
			// if(this.blocks.get(this.board[5][99]).getTop().getX() == 5 && this.blocks.get(this.board[5][99]).getTop().getY() == 53 ) {
			// 	if(x_to == 5 && x_to == 99 ) {
			// 		System.out.println("5 99");
			// 		System.exit(1);
			// 	}
			// }
		}
		// System.out.println(blocks);
		// System.exit(1);
			
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
		Tray thai = new Tray(numsSet);
		numsSet = exitGoal(goal,numsSet[0],numsSet[1]);

		readGoal(numsSet);
		// System.out.println("goal size = "+ goals.size());

	}
	public int[] exitFive(String file) {
		int[] numSet = new int[1];
		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String current = "";
			boolean flag = false;
			while((current = in.readLine()) != null) { 

				String[] nums = current.split("\\s+");
				if( i == 0) {
					int n1 = Integer.parseInt(nums[0]);
					int n2 = Integer.parseInt(nums[1]);
					if(nums.length != 2 && (n2 < 256 || n1 < 256) ) {
						System.out.println("board dim Must be 2 integer");
						System.out.println("exit(5)");
						System.exit(5);
					}
					numSet = new int[n1*n2*4 + 2];
					numSet[i++] = n1;
					numSet[i++] = n2;
					flag = false;
					// System.out.println("heelo");

				}

				if(i != 2 && nums.length != 4) {
					System.out.println("Tray, Must be 4 integer");
					System.out.println("exit(5)");
					System.exit(5);
				}
				if(nums.length != 2) {
					for(int j = 0; j < nums.length ; j++) {
						try {
							int n = Integer.parseInt(nums[j]);
							
						 	if(n < 0 || n >= 256) {
								System.out.println("Tray, number must be postive");
								System.out.println("exit(5)");
								System.exit(5);
							}
							
						} catch (NumberFormatException e) {
							System.out.println("Tray, not a number");
							System.out.println("exit(5)");
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
			System.err.println("Can't read the file");
			System.out.println("exit(3)");
			System.exit(3);
		}
		int[] temp = new int[i + 2];
		System.arraycopy(numSet, 0, temp, 0, temp.length);
		// System.out.println("i = " + i);
		return temp;
	}

	public int[] exitGoal(String file, int x, int y) {
		int[] numSet = new int[x * y *4];
		int i = 0;
		numSet[i++] = x;
		numSet[i++] = y;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String current = "";
			// boolean flag = false;
			while((current = in.readLine()) != null) { 

				String[] nums = current.split("\\s+");

				if(nums.length != 4) {
					System.out.println("Tray, Must be 4 integer");
					System.out.println("exit(5)");
					System.exit(5);
				}
				for(int j = 0; j < nums.length ; j++) {
					try {
						int n = Integer.parseInt(nums[j]);
						if(n < 0 || n >=256) {
							System.out.println("Tray, number must be postive");
							System.out.println("exit(5)");
							System.exit(5);
						}
					} catch (NumberFormatException e) {
						System.out.println("Tray, not a number");
						System.out.println("exit(5)");
						System.exit(5);
					}
				}
			
					numSet[i++] = Integer.parseInt(nums[0]);
					numSet[i++] = Integer.parseInt(nums[1]);
					numSet[i++] = Integer.parseInt(nums[2]);
					numSet[i++] = Integer.parseInt(nums[3]);
				

			}
			
		}catch( IOException e) {
			System.err.println("Can't read the file");
			System.out.println("exit(3)");
			System.exit(3);
		}
		int[] temp = new int[i];
		System.arraycopy(numSet, 0, temp, 0, temp.length);
		// System.out.println("i = " + i);
		return temp;
	}


	public void readGoal(int[] numSet) {
		int z = 0;
		goals = new HashSet<Coordinate>();
		for(int i = 2; i < numSet.length - 3; i +=4) {	
			int x = numSet[i];
			int y = numSet[i + 1];
			int k = numSet[i + 2];
			int v = numSet[i + 3];
			board[x][y].setGoal(x, y, k, v);
			goals.add(board[x][y]);
			if(board[x][y].isOccupied()) {
				if(blocks.get(board[x][y]).isGoal()) {
					z++;
				}
			}
		}
		System.out.println("goal size  = " + goals.size());
		if(goals.size() == z) {
			System.out.println("WIN MAN");
			System.out.println("exit(0)");
			System.exit(0);
		}
	}


	public void play() {
		print();
		Scanner in = new Scanner(System.in);
		while(in.hasNextLine()) { // check if is legal move later
			String temp = in.nextLine();
			String[] nums = temp.split("\\s+");
			if(nums.length != 4) {
				System.out.println("Play, Must be 4 integer");
				System.out.println("exit(4)");
				System.exit(4);
			}
			for(int i = 0; i < 4 ; i++) {
				try {
					int n = Integer.parseInt(nums[i]);
					if(n < 0) {
						System.out.println("Play, number must be postive");
						System.out.println("exit(4)");
						System.exit(4);
					}
				} catch (NumberFormatException e) {
					System.out.println("Play, not a number");
					System.out.println("exit(4)");
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

			if(!legal3 || !board[x][y].isOccupied()) {
				System.out.println("Contains an impossible move");
				System.out.println("exit(6)");
				System.exit(6);
			}

			blocks.get(board[x][y]).move(board[i][j], true);
			print();
		}
		if(!win) {
			System.out.println("the input moves do not solve the puzzle");
			System.out.println("exit(1)");
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
			System.out.println("Must receive exactly 2 command lind inputs");
			System.out.println("exit(2)");
			System.exit(2);
		}
		Checker thai = new Checker(args[0],"",args[1]); 
		Scanner in = new Scanner(System.in);
		thai.play();

	}

// ###################### MAIN ################################################################


//--------------------Block------------------begin----------------------------------------
	public class Block {
		private int name; // for testing
		private Coordinate top_left;
		private Coordinate bottom_right;
		// private Block goal;

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
			for(int i = this.top_left.x - 1; i >= toMoveTo.x; i--) {
				for(int j = this.top_left.y; j <= this.bottom_right.y; j++) {
					// System.out.println(board[i][j]);
					if(board[i][j].isOccupied()) {
						return false;
					}
				}
			}
			return true;
		}

		public boolean canMoveDown(Coordinate toMoveTo, int xDistance) {
			System.out.println("Down");
			for(int i = this.bottom_right.x + 1; i <= toMoveTo.x + xDistance && i < board.length; i++) {
				for(int j = this.top_left.y; j <= this.bottom_right.y; j++) {
					// System.out.println(board[i][j]);
					if(board[i][j].isOccupied()) {
						return false;
					}
				}
			}
			return true;
		}
		public boolean canMoveLeft(Coordinate toMoveTo) {
			System.out.println("Left");
			for(int i = this.top_left.y - 1; i >= toMoveTo.y ; i--) {
				for(int j = this.top_left.x; j <= this.bottom_right.x; j++) {
					// System.out.println(board[j][i]);
					if(board[j][i].isOccupied()) {
						return false;
					}
				}
			}
			return true;
		}
		public boolean canMoveRight(Coordinate toMoveTo, int yDistance, int k) {
			System.out.println("Right");
			for(int i = this.bottom_right.y + 1; i < toMoveTo.y + yDistance + k && i < board[0].length ; i++) {
				for(int j = this.bottom_right.x; j >= this.top_left.x; j--) {
					// System.out.println(board[j][i]);
					if(board[j][i].isOccupied()) {
						return false;
					}
				}
			}
			return true;
		}

		/** 
		* @param takes a Coordinate that specifies where we want to try to move by trying to
		* move the block's top_left to toMoveTo
		* @return returns a boolean that says if we can make the move
		*/
		public int canMakeMove(Coordinate toMoveTo)  {  // 
			int k = 1;
			int x = toMoveTo.x;
			int y = toMoveTo.y;
			int x_length = Math.abs(this.top_left.x - this.bottom_right.x) + 1; 
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) + 1;
			int yDistance = Math.abs(this.top_left.y - toMoveTo.y);
			int xDistance = Math.abs(this.top_left.x - this.bottom_right.x);

			if(x_length == 1|| y_length == 1) {
				k = 0;
			}

			if(x < this.top_left.x && canMoveUp(toMoveTo)) { //up

				return 1;
			}
			else if(x > this.top_left.x && canMoveDown(toMoveTo,xDistance)) { //down

				return 2;
			}
			else if(y < this.top_left.y && canMoveLeft(toMoveTo)) { //left

				return 3;
			}
			else if(y > this.top_left.y && canMoveRight(toMoveTo, yDistance, k)) { //right
				return 4;
			}
			return 0;
		}

		public void top(Coordinate toMoveTo) {
			System.out.println("up update");
			int x_length = Math.abs(this.top_left.x - this.bottom_right.x) + 1; 
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) + 1;
			for(int i = this.top_left.x - 1; i >= toMoveTo.x; i--) {
				for(int j = this.top_left.y; j <= this.bottom_right.y; j++) {
					// System.out.println(board[i][j]);
					if(goals.contains(board[i][j])) {
						System.out.println("contains!!");
						System.exit(1);
						board[i][j].setOccupied(true);
						board[i][j].setName(this.name);
					}
					else {
						board[i][j] = new Coordinate(i,j,this.name);
					}
					blocks.put(board[i][j],this);
					blocks.remove(board[i + x_length][j]);
					if(goals.contains(board[i + x_length][j])) {
						System.out.println("contains!!");
						System.exit(1);
						board[i + x_length][j].setOccupied(false);
						board[i + x_length][j].setName(0);
					}
					else {
						board[i + x_length][j] = new Coordinate(i + x_length, j, 0);
					}
				}
			}
		}

		public void bottom(Coordinate toMoveTo) {
			int k = 1;
			System.out.println("Bottom update");
			int xDistance = Math.abs(this.top_left.x - this.bottom_right.x);
			int x_length = Math.abs(this.top_left.x - this.bottom_right.x) + 1; 
			if(x_length == 1) {
				k = 0;
			}
			for(int i = this.bottom_right.x + 1; i <= toMoveTo.x + xDistance   && i < board.length; i++) {
				for(int j = this.top_left.y; j <= this.bottom_right.y; j++) {
					// System.out.println(board[i][j]);
					if(goals.contains(board[i][j])) {
						System.out.println("contains!!");
						System.exit(1);
						board[i][j].setOccupied(true);
						board[i][j].setName(this.name);
					}
					else {
						board[i][j] = new Coordinate(i,j,this.name);
					}

					blocks.put(board[i][j],this);
					blocks.remove(board[i - x_length][j]);
					if(goals.contains(board[i - x_length][j])) {

						System.out.println("contains!!");
						System.exit(1);
						board[i - x_length][j].setOccupied(false);
						board[i - x_length][j].setName(0);
					}
					else {
						board[i - x_length][j] = new Coordinate(i-x_length, j, 0);
					}
				}
			}
		}

		public void left(Coordinate toMoveTo) {
			System.out.println("left update");
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) + 1;
			for(int i = this.top_left.y - 1; i >= toMoveTo.y ; i--) {
				for(int j = this.top_left.x; j <= this.bottom_right.x; j++) {
					// System.out.println(board[j][i]);
					if(goals.contains(board[j][i])) {
						System.out.println("contains!!");
						System.exit(1);
						board[j][i].setOccupied(true);
						board[j][i].setName(this.name);
					}
					else {
						board[j][i] = new Coordinate(j,i,this.name);
					}
					blocks.put(board[j][i], this);
					blocks.remove(board[j][i + y_length]);
					if(goals.contains(board[j][i + y_length])) {
						System.out.println("contains!!");
						System.exit(1);
						board[j][i + y_length].setOccupied(false);
						board[j][i + y_length].setName(0);
					}
					else {
						board[j][i + y_length] = new Coordinate(j, i + y_length, 0);
					}
				}
			}
		}

		public void right(Coordinate toMoveTo) {
			int k = 1;
			System.out.println("right update");
			int yDistance = Math.abs(this.top_left.y - toMoveTo.y);
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) + 1;
			if(y_length == 1) {
				k = 0;
			}
			for(int i = this.bottom_right.y + 1; i < toMoveTo.y + yDistance  + k && i < board[0].length ; i++) {
				for(int j = this.bottom_right.x; j >= this.top_left.x; j--) {
					// System.out.println(board[j][i]);
					if(goals.contains(board[j][i])) {
						System.out.println("contains!!");
						System.exit(1);
						board[j][i].setOccupied(true);
						board[j][i].setName(this.name);
					}
					else {
						board[j][i] = new Coordinate(j,i,this.name);
					}
					blocks.put(board[j][i], this);
					blocks.remove(board[j][i - y_length]);
					if(goals.contains(board[j][i - y_length])) {
						System.out.println("contains!!");
						System.exit(1);
						board[j][i - y_length].setOccupied(false);
						board[j][i - y_length].setName(0);
					}
					else {
						board[j][i - y_length] = new Coordinate(j, i - y_length, 0);
					}
				}
			}
		}
		public void printBlocks() {
			Iterator <Map.Entry<Coordinate, Block>> iterator = blocks.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Coordinate, Block> entry = iterator.next();
				Coordinate key = entry.getKey();
				Block val = entry.getValue();	
			  	// System.out.println("key: " + key + " value: " + val);
			}
		}
		/**
		* @param takes 2 coordinates: the original top left coordinate of the block, and the
		* new top left coordinate we wish to move the block to
		* @result only call and make move when canMakeTrue(...) returns true
		*/

		public void move(Coordinate toMoveTo , boolean check){
			int i = canMakeMove(toMoveTo);

			if(i == 1) {
				top(toMoveTo);
			}
			else if(i == 2) {
				bottom(toMoveTo);
			}
			else if(i == 3) {
				left(toMoveTo);
			}
			else if(i == 4) {
				right(toMoveTo);
			}
			else if(i == 0 && check){
				System.out.println("Contains an impossible move");
				System.out.println("exit(6)");
				System.exit(6);
			}
			updateMove(toMoveTo);
			if(check) {
				checkGoal();
			}
		}

		public void updateMove(Coordinate toMoveTo) {
			int x_length = Math.abs(this.top_left.x - this.bottom_right.x) + 1; 
			int y_length = Math.abs(this.top_left.y - this.bottom_right.y) + 1;
			toMoveTo.setName(this.name);
			toMoveTo.setOccupied(true);
			this.top_left = toMoveTo;

			if(goals.contains(board[toMoveTo.x + x_length -1][toMoveTo.y + y_length - 1])) {
				board[toMoveTo.x + x_length -1][toMoveTo.y + y_length - 1].setOccupied(true);
				board[toMoveTo.x + x_length -1][toMoveTo.y + y_length - 1].setName(this.name);
				this.bottom_right = board[toMoveTo.x + x_length -1][toMoveTo.y + y_length - 1];
			}
			else {
				this.bottom_right = new Coordinate(toMoveTo.x + x_length -1, toMoveTo.y + y_length - 1, this.name);
			}
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
			for(Coordinate a: goals) {
				if(blocks.containsKey(a) && blocks.get(a).isGoal()) {
					x++;
				}
			}
			if(x == goals.size()) {
				System.out.println("REACH ALL YOUR GOALS, DONE");
				System.out.println("exit(0)");
				win = true;
				System.exit(0);
			}
		}

		public boolean isGoal() {
			Coordinate temp = board[this.top_left.x][this.top_left.y];
			if(temp.getGoal() != null) {
				Block bottom = temp.getGoal();
				return bottom.bottom_right.x == this.bottom_right.x && bottom.bottom_right.y == this.bottom_right.y;
			}
			return false;
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

		public int hashCode() {
			return this.name;
		}

		public String toString() {
			return top_left.toString() + ", " + bottom_right.toString();
		}

		public boolean equals(Object o) {
			if (o instanceof Block) {
				Block other = (Block) o;
				boolean legal1 = (this.top_left.x == other.top_left.x && this.top_left.y == other.top_left.y);
				// boolean legal2 = (this.bottom_right.x == other.bottom_right.x && this.bottom_right.y == other.bottom_right.y);
				return legal1;
			}
			return false;
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

		private Coordinate(int x, int y, int name) {
			this.x = x;
			this.y = y;
			this.occupied = false;
			this.name = name;
			// if(!name.substring(0,1).equals("0")) {
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
		public boolean equals(Object o) {
			Coordinate cast = (Coordinate) o;
			return (this.x == cast.x && this.y == cast.y);
		}

		// public int hashCode(){
		// 	return this.toString().hashCode();
		// }

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
				HashMap<String,Integer> dim = new HashMap<String,Integer>();
					
				board = new Coordinate[numSet[0]] [numSet[1]];
				populateBoard();
				// System.out.println("length = " + numSet.length);
				for(int j = 2; j < numSet.length - 3; j += 4) {
					x = numSet[j];
					y = numSet[j + 1];
					k = numSet[j + 2];
					z = numSet[j + 3];
					int name;
					String s = Integer.toString(k-x) + " "  + Integer.toString(z-y);
					// System.out.println("j = " + j);
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
					// System.out.println("myBlock.top_left.x  + " +myBlock.top_left.x);
					// System.out.println("myBlock.top_left.y + j " +(myBlock.top_left.y + j));
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
//--------------------Coordinate------------------begin----------------------------------------


}