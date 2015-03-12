import java.util.*;
import java.io.*;


public class Solver {

	public Node goal;
	private Comparator<Node> compare = new NodeComparator();
	public PriorityQueue<Node> lowestCost; // unquie Nodes with lowest cost
	// public HashSet<Checker> seen;
	public HashSet<Integer> seenInt;
	public HashSet<Checker.Block> goalBlocks;
	public int goalDistance ;

	private ArrayList<Integer> prime;
	public void prime() {
		prime = new ArrayList<Integer>();
		prime.add(2);
		int status = 1;
		int num = 3;
		for (int count = 2 ; count <=1000 ;  ) {
			for ( int j = 2 ; j <= Math.sqrt(num) ; j++ ) {
				if (num%j == 0 ) {
					status = 0;
					break;
				}
			}
			if (status != 0 ) {
				prime.add(num);
				count++;
			}
			status = 1;
			num++;
		}    
	}

	public Solver(String init, String goal) {
		prime();
		goalBlocks = new HashSet<Checker.Block>();
		// seen = new HashSet<Checker>();
		this.goal = new Node(goal, "goal");
		goalBlocks = this.goal.allBlocks;
		goalDistance = this.goal.state;
		System.out.println("Goal distance = "+ goalDistance);
		System.out.println("Goal Blocks = "+ goalBlocks.size());
		// System.exit(1);
		Node initial = new Node(init,"");
		seenInt = new HashSet<Integer>();
		seenInt.add(initial.state);
		System.out.println("state = "+ initial.state);
		// System.exit(1);
		// seenInt.add(initial.thai);
		// System.exit(1);
		lowestCost = new PriorityQueue<Node>(1000,compare);
		lowestCost.add(initial);
		System.out.println("goal's size = " + this.goal.allBlocks.size());
		if(goalBlocks.size() > initial.allBlocks.size()) {
			System.exit(6);
		}
		// solve();
	}

	public boolean solve(){
		int i = 0;
		while(!lowestCost.isEmpty()){
			Node low = lowestCost.poll();
			
			low.thai.print();
			// System.out.println("IIII = " + i);
			// System.out.println("low state = " + low.state);
			// System.out.println(low.allBlocks);
			i++;
			// if(low.state == goalDistance){
			if(low.isGoal()) {
				// System.out.println("low state = " + low.state);
				// System.out.println("goalDistance = " + goalDistance);
				// System.out.println("solution bitch");
				return true;
				// break;
			}
			low.generateMoves();
			// System.out.println("lowestCost size = " + lowestCost.size());
		}
		// System.out.println("size = " + lowestCost.size());
		// System.out.println("IIII final = " + i);
		return false;
	}

	public class NodeComparator implements Comparator<Node>
    {
        @Override
        public int compare(Node node1, Node node2)
        {
            if (node1.cost < node2.cost)
            {
                return -1;
            }
            else if (node1.cost > node2.cost)
            {
                return 1;
            }
            return 0;
        }
    }



	public static void main(String[] args) {
		System.out.println("hello world");
		Solver thai = new Solver(args[0], args[1]);
		System.out.println(thai.solve());

	}

	public class Node {
		private Checker.Coordinate[][] board;
		private HashMap<Checker.Coordinate, Checker.Block> blocks;
		private HashSet<Checker.Block> allBlocks;
		public Checker thai;
		public int cost;
		private LinkedList<String> moves;
		public int numMoves;
		public int name;
		public int state;

		public Node(String init, String isGoal) {
			thai = new Checker(init);

			cost = 0;
			board = new Checker.Coordinate[10][10];
			board = thai.getBoard();
			blocks = new HashMap<Checker.Coordinate, Checker.Block>();
			blocks = thai.getBlocks();
			allBlocks = new HashSet<Checker.Block>();
			allBlocks = thai.getAllBlocks();
			moves = new LinkedList<String>();
			System.out.println("Size Block = " + allBlocks.size());
			// if(!isGoal.equals("goal")) {
				this.state = state();
	
		}

		public Node(int x_top, int y_top,int x_to, int y_to, Node otherNode) {
			this.thai = new Checker();
			board = new Checker.Coordinate[5][4];
			blocks = new HashMap<Checker.Coordinate, Checker.Block>();
			allBlocks = new HashSet<Checker.Block>();
			moves = new LinkedList<String>();
			// System.out.println("other = sssssss= =" + otherNode);
			this.thai = this.thai.copy(otherNode.thai, x_top, y_top, x_to,y_to);
			
			this.board = this.thai.getBoard();
			this.allBlocks = this.thai.getAllBlocks();
			blocks = this.thai.getBlocks();
			this.moves = otherNode.moves;
			String move = new String(x_top + " " + y_top + " " + x_to + " " + y_to); 
			this.cost = computeCost();
			this.moves.add(move);
			this.state = state();
	

		}

		public int computeCost(){
			//iterate through all blocks and sum up their distances from where they are in the goal configuration
			//numMoves (distance we've traveled thus far) + distance to solution ^described above
			int distance = 0;
			HashSet<Checker.Block> temp = new HashSet<Checker.Block>(allBlocks);
			int min = Integer.MAX_VALUE;
			for(Checker.Block b: goal.allBlocks) {
				Checker.Block z = null;
				for(Checker.Block t: temp) {
					int x_top = b.getTop().getX();
					int y_top = b.getTop().getY();
					int x_bottom = b.getBottom().getX();
					int y_bottom = b.getBottom().getY();

					int xi_top = t.getTop().getX();
					int yi_top = t.getTop().getY();
					int xi_bottom = t.getBottom().getX();
					int yi_bottom = t.getBottom().getY();

					int top_length = Math.abs(x_top - x_bottom);
					int bottom_length = Math.abs(y_top - y_bottom);
					int itop_length = Math.abs(xi_top - xi_bottom);
					int ibottom_length = Math.abs(yi_top - yi_bottom);
					if(top_length == itop_length && bottom_length == ibottom_length) {
						int diff = Math.abs(x_top - xi_top + y_top - yi_top);
						if(min > diff) {
							min = diff;
							z = t;
						} 
					}
				}
				distance += min;
				if(z != null) {
					temp.remove(z);
				}
			}
			return distance + moves.size();
		}
		public void generateMoves() {
			ArrayList<Node> result = new ArrayList<Node>();
			for(Checker.Block block: allBlocks) {
					result.addAll(up(block));
					result.addAll(down(block));
					result.addAll(left(block));
					result.addAll(right(block));
			}
			
			for(Node k: result) {

				if(!seenInt.contains(k.state)) {
					seenInt.add(k.state);
					// System.out.println("state = " + k.state);
					// print();
					lowestCost.add(k);
				}
			}
			// System.exit(1);
		}


		public ArrayList<Node> up(Checker.Block block) {
			System.out.println("UPUPUP");
			System.out.println("block" + block.getTop());
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(y_top - y_bottom) + 1;

			for(int i = x_top - 1; i >= 0; i--) {
				for(int j = y_top; j <= y_bottom; j++) {  // change from j < length
					if(board[i][j].isOccupied()) {
						i = -1;
						break;
					}

				}
				if(i != -1) {				
					if(!hasSeen(block,i, y_top)) {
						Node temp = new Node(x_top, y_top, i, y_top,this);
						// temp.print();
						result.add(temp);
						// System.out.println("temp distance = " + temp.thai.state());
					}
				}

			}
			return result;
		}

		public ArrayList<Node> down(Checker.Block block) {
			System.out.println("DOWN DOWN");
			// System.out.println("block" + block.getTop());
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(x_top - x_bottom);
			System.out.println("out side");
			for(int i = x_top + 1; i + length < this.board.length ; i++) {
				System.out.println("outer loop");
				for(int j = y_top; j <= y_bottom; j++) {

					// String name = "0";
					// if(blocks.get(board[j][j]) != null) {
					// 	name = blocks.get(board[i][j]).getName();
					// }
					System.out.println("x = " + i + ", y = "+ j);
					if(board[i][j].isOccupied()) { 
						int name = blocks.get(board[i][j]).getName();
						if(name != block.getName()) {
							// System.out.println("name = " + name + ", block.getName() = "+ block.getName());
							// System.out.println("y = " + i + ", y = "+ j);
							// System.exit(1);
							i = this.board.length;
							break;
						}
						// System.out.println("name = " + name + ", block.getName() = "+ block.getName());
							// System.out.println("y = " + i + ", y = "+ j);
						// System.exit(1);
					}

				}

				if(i != this.board.length && this.board[x_top][y_top].getName() != 0) {
					if(!hasSeen(block,i, y_top)) {
						// System.exit(1);
						Node temp = new Node(x_top, y_top, i, y_top,this);
						// System.exit(1);
						// temp.print();
						result.add(temp);
						// System.out.println("temp distance = " + temp.thai.state());

					}	
				}


			}
			// System.exit(1);
			return result;
		}
		public ArrayList<Node> left(Checker.Block block) {
			System.out.println("LEFT LEFT");
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			System.out.println("Block = " + block);

			for(int i = y_top - 1; i >= 0; i--) {
				for(int j = x_top; j <= x_bottom; j++) {
					// System.out.println("x = " + j + ", y = "+ i);
					if(board[j][i].isOccupied()) {
						i = -1;
						break;
					}
				}
				System.out.println("i = " + i);
				// System.exit(1);
				if(i != -1) {
					if(!hasSeen(block,x_top, i)) {
						// System.out.println("EHRERE");
						// System.out.println("x_top = "+ x_top+ ", y_top " + y_top  + " to " + x_top + " "+  i);
						// System.exit(1);
						Node temp = new Node(x_top, y_top, x_top, i,this);
						// temp.print();
						result.add(temp);
						// System.out.println("temp distance = " + temp.thai.state());


					}	
				}
				// System.exit(1);
			}
			// System.exit(1);
			return result;
		}

		public ArrayList<Node> right(Checker.Block block) {
			System.out.println("RIGHT RIGHT");
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(y_top - y_bottom);
			System.out.println("Block = " + block);

			for(int i = y_top + 1; i + length < this.board[0].length; i++) {
				for(int j = x_top; j <= x_bottom; j++) {
					// System.out.println("x = " + j + ", y = "+ i);
					// System.out.println("hash name = " + blocks.get(board[j][j]).getName());
					// System.out.println("block name = " + block.getName());
					// System.exit(1);
					// String name = "man";
					if(blocks.get(board[j][i]) != null) {
						name = blocks.get(board[j][i]).getName();
					}
					if(board[j][i].isOccupied() && name != block.getName()) {
						i = this.board[0].length;
						break;
					}
				}
				// System.out.println("i = " + i);
				// System.exit(1);
				// System.out.println("x_top = "+ x_top+ ", y_top " + y_top  + " to " + x_top + " "+  i);
				// System.exit(1);
				if(i < this.board[0].length) {
					if(!hasSeen(block,x_top, i)) {
						// System.out.println("EHRERE");
						// System.out.println("x_top = "+ x_top+ ", y_top " + y_top  + " to " + x_top + " "+  i);
						// System.exit(1);
						Node temp = new Node(x_top, y_top, x_top, i,this);
						// temp.print();
						result.add(temp);
						// System.out.println("temp distance = " + temp.thai.state());
					}	
				}
				// System.exit(1);
				// System.exit(1);
			}
			// System.exit(1);
			return result;



		}
			
		public boolean hasSeen(Checker.Block block, int x_to, int y_to) {
			int distance = 0;
			Checker.Coordinate[][] temp = new Checker.Coordinate[this.board.length][this.board[0].length];
			for(int i = 0; i < temp.length; i++) {
				for(int j = 0; j < temp[0].length; j++) {
					temp[i][j] = board[i][j].copyCoordinate();// = new Checker.Coordinate(i,j,Integer.parseInt(board[i][j].getName().substring(0,1)));
				}
			}
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int y_length = Math.abs(y_top - y_bottom);
			int x_length = Math.abs(x_top - x_bottom);
			for(int i = x_top; i <= x_bottom; i++) {
				for(int j = y_top; j <= y_bottom; j++) {
					temp[i][j].setOccupied(false);
				}
			}
			// System.out.println("block = " + block + ",x_to " + x_to + ", y_to  = " + y_to );
			x_top = x_to;
			y_top = y_to;
			x_bottom = x_to + x_length;
			y_bottom = y_to + y_length;

			for(int i = x_to; i <= x_bottom; i++) {
				for(int j = y_to; j <= y_bottom; j++) {
					// System.out.println("herer???" + " x = " + i  + ", y = " + j);
					temp[i][j].setOccupied(true);
				}
			}

			int k = 0;
			for(int i = 0; i < temp.length; i++) {
				for(int j = 0; j < temp[0].length; j++) {
					if(temp[i][j].isOccupied()) {
						int num = prime.get(k);
						// System.out.println("prime solve = "+ num);
						// System.out.println("i = " + i + ", j = " + j + " ");
						if(i == 0 && j == 0) {
							distance += num;// Integer.parseInt(block.getName());
						}
						else {
							distance += num;// (Integer.parseInt(block.getName())*(num));
						}
						// distance += (Integer.parseInt(block.getName())*(i*temp[0].length + i + j));
					}
					k++;
				}
			}


			System.out.println("has seen distance = " + distance);
			// System.exit(1);
			return seenInt.contains(distance);
		}

		public boolean isGoal() {

			// int distance = 0;
			System.out.println("size  state= " + this.allBlocks.size());
			// System.exit(1);
			for(Checker.Block b: goalBlocks) {
			
				int x_top = b.getTop().getX();
				int y_top = b.getTop().getY();
				int x_bottom = b.getBottom().getX();
				int y_bottom = b.getBottom().getY();
				System.out.println("block = " + b + "\nx_top " + x_top + ", y_top  = " + y_top );
				System.out.println("x_bottom  = " + x_bottom + ", y_bottom = " + y_bottom);
				int num = 0;
				int length = x_bottom - x_top;
				int width = y_bottom - y_top;
				// System.exit(1);
				Checker.Block temp;
				if(this.board[x_top][y_top].isOccupied()) {
					temp = blocks.get(this.board[x_top][y_top]);
					int x = temp.getTop().getX();
					int y = temp.getTop().getY();
					int xi = temp.getBottom().getX();
					int yi = temp.getBottom().getY();

					if(!((x == x_top) && (y == y_top) && (xi == x_bottom) && (yi == y_bottom))) {
						return false;
					}
				}
				else {
					return false;
				}
			}
			return true;
				// print();
			// System.out.println("distance2 = " + distance);
			// System.exit(1);
			// return distance;
		}

		public int state() {
			int distance = 0;
			int k = 0;
			for(int i = 0; i < this.board.length; i++) {
				for(int j = 0; j < this.board[0].length; j++) {
					if(this.board[i][j].isOccupied()) {
						int num = prime.get(k);
						distance += num;// (Integer.parseInt(block.getName())*(num));
					}
					k++;
				}
			}
			return distance;
		}


		public int getCost(){
			return this.cost;
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

	}

}