import java.util.*;
import java.io.*;


public class Solver {

	public Node goal;
	private Comparator<Node> compare = new NodeComparator();
	public PriorityQueue<Node> lowestCost; // unquie Nodes with lowest cost;
	public HashSet<Integer> seenInt;
	public HashSet<Checker.Block> goalBlocks;
	public Stack<Node> fringe;


	// private HashMap<Integer, Integer> prime;
	private ArrayList<Integer> prime;
	public void prime(int x,int y) {
		// prime = new HashMap<Integer, Integer>();
		prime = new ArrayList<Integer>();
		// prime.put(0,2);
		prime.add(2);
		int k = 1;
		int status = 1;
		int num = 3;
		for (int count = 2 ; k <= x*y  ; count++) {
			for ( int j = 2 ; j <= Math.sqrt(num) ; j++ ) {
				if (num%j == 0 ) {
					status = 0;
					break;
				}
			}
			if (status != 0 ) {
				// prime.put(k,num);
				prime.add(num);
				count++;
				k++;
			}
			status = 1;
			num++;
		}    
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

	public Solver(String init, String goal) {
		lowestCost = new PriorityQueue<Node>(100000, compare) ;
		fringe = new Stack<Node>();
		goalBlocks = new HashSet<Checker.Block>();

		Node initial = new Node(init,false,0,0);
		seenInt = new HashSet<Integer>();
		seenInt.add(initial.state);

		this.goal = new Node(goal, true, initial.board.length, initial.board[0].length);
		goalBlocks = this.goal.allBlocks;

		fringe.push(initial);

		// lowestCost.add(initial);
		// System.out.println("init state = "+ initial.state);
		// System.out.println("goal state = "+ this.goal.state);
		// System.exit(1);
		// System.out.println("goal's size = " + this.goal.allBlocks.size());
	}
/*
	public boolean solve(){
		int i = 0;
		// while(!fringe.isEmpty()) {
		Node current = null;
		boolean flag = true;
		while(!fringe.isEmpty() || !lowestCost.isEmpty()) {

			
			
			if(!flag) {
				current = fringe.pop();

			}
			else {
				current = lowestCost.poll();
			}

			// current.thai.print();
			i++;

			
			if(current.isGoal()) {
				return true;
			}

			current.generateMoves(flag);

			if(flag) {
				// lowestCost.add(fringe.pop());
				fringe.push(lowestCost.poll());
				flag = false;
			}
			else {
				// fringe.push(lowestCost.poll());
				lowestCost.add(fringe.pop());
				flag = true;
			}

			// while(!lowestCost.isEmpty()) {
			// 	current = lowestCost.poll();
			// 	current.thai.print();
			// 	if(current.isGoal()) {
			// 		return true;
			// 	}
			// 	current.generateMoves(false);
			// 	lowestCost.add(fringe.pop());
			// }


			System.out.println("stack size = "+ fringe.size());

		}
		System.out.println("Herere");
		return false;
	}
 */
	public void solve(){
		int i = 0;
		while(!fringe.isEmpty()) {

			Node low = fringe.pop();
			// low.thai.print();
			i++;

			// System.out.println("i = " + i );

			// System.out.println("low state = "+ low.state);
			// System.out.println("goal state = "+ goal.state);
		
			// if(low.state == goal.state) {
			if(low.isGoal()) {
				for(String a: low.moves) {
					System.out.println(a);
				}
				System.out.println("SOLVE");
				return;
			}

			low.generateMoves();
			// System.out.println("stack size = "+ fringe.size());

		}
		// System.out.println("Here2222222222222222re");
		return ;
	}




	public static void main(String[] args) {
		// System.out.println("hello world");
		Solver thai = new Solver(args[0], args[1]);
		// System.out.println(thai.solve());
		thai.solve();


	}

	public class Node {
		private Checker.Coordinate[][] board;
		private HashMap<Checker.Coordinate, Checker.Block> blocks;
		private HashSet<Checker.Block> allBlocks;
		public Checker thai;
		public int cost;
		private ArrayList<String> moves;
		public int numMoves;
		public int name;
		public int state;

		public Node(String init, boolean isGoal, int x, int y) {
			if(!isGoal){
				thai = new Checker(init, false, 0,0);
				board = thai.getBoard();
				prime(board.length,board[0].length);
			}
			else {
				thai = new Checker(init, true, x,y);
				board = thai.getBoard();
			}
			// thai = new Checker(init);

			this.cost = 0;
			// board = thai.getBoard();
			blocks = new HashMap<Checker.Coordinate, Checker.Block>();
			blocks = thai.getBlocks();
			allBlocks = new HashSet<Checker.Block>();
			allBlocks = thai.getAllBlocks();
			moves = new ArrayList<String>();

			this.state = state();

		}

		public Node(int x_top, int y_top,int x_to, int y_to, Node otherNode) {
			this.thai = new Checker(otherNode.board.length,otherNode.board[0].length);
			blocks = new HashMap<Checker.Coordinate, Checker.Block>();
			allBlocks = new HashSet<Checker.Block>();
			this.thai = this.thai.copy(otherNode.thai, x_top, y_top, x_to,y_to);
			blocks = this.thai.getBlocks();
			this.board = this.thai.getBoard();
			this.allBlocks = this.thai.getAllBlocks();
			// blocks = this.thai.getBlocks();
			this.moves = new ArrayList<String>(otherNode.moves);
			String move = new String(x_top + " " + y_top + " " + x_to + " " + y_to); 
			
			this.moves.add(move);
			this.state = state();
			this.cost = computeCost();
	

		}

		public void generateMoves() {
			ArrayList<Node> result = new ArrayList<Node>();
			int i = 0;
			for(Checker.Block block: allBlocks) {
			
					result.addAll(up(block));
					result.addAll(down(block));
					result.addAll(left(block));
					result.addAll(right(block));				
			}

			for(Node k: result) {
				if(!seenInt.contains(k.state)) {
					seenInt.add(k.state);
					// if(!flag) {
					// 	fringe.push(k);
					// }
					// else {
					// 	lowestCost.add(k);
					// }
					// k.print();
					fringe.push(k);
				}
			}
		}


		public ArrayList<Node> up(Checker.Block block) {
			// System.out.println("UPUPUP");
			// System.out.println("block" + block.getTop());
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(y_top - y_bottom) + 1;

			if(this.blocks.get(this.board[x_top][y_top]) == null) {
				System.out.println("NULl UP");
				System.out.println(block);
				System.out.println("x_top = " + x_top);
				System.out.println("y_top = " + y_top);
				System.out.println(blocks);
				System.exit(1);
			}

			for(int i = x_top - 1; i >= 0; i--) {
				for(int j = y_top; j <= y_bottom; j++) {  // change from j < length
					if(board[i][j].isOccupied()) {
						i = -1;
						break;
					}

				}
				if(i != -1 && this.board[x_top][y_top].getName() != 0 && this.blocks.get(this.board[x_top][y_top]) != null) {				
					if(!hasSeen(block,i, y_top)) {
						Node temp = new Node(x_top, y_top, i, y_top,this);
						result.add(temp);
					}
				}

			}
			return result;
		}

		public ArrayList<Node> down(Checker.Block block) {
			// System.out.println("DOWN DOWN");
			// System.out.println("block" + block.getTop());
			// System.exit(1);
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(x_top - x_bottom);
			if(this.blocks.get(this.board[x_top][y_top]) == null) {
				System.out.println("NULl Down");
				System.out.println(block);
				System.out.println("x_top = " + x_top);
				System.out.println("y_top = " + y_top);
				System.out.println(blocks);
				System.exit(1);
			}

			for(int i = x_top + 1; i + length < this.board.length ; i++) {
				// System.out.println("outer loop");
				for(int j = y_top; j <= y_bottom; j++) {
					if(board[i][j].isOccupied()) { 
						int name = blocks.get(board[i][j]).getName();
						if(name != block.getName()) {
							i = this.board.length;
							break;
						}

					}

				}

				if(i != this.board.length && this.board[x_top][y_top].getName() != 0 && this.blocks.get(this.board[x_top][y_top]) != null) {
					if(!hasSeen(block,i, y_top)) {
						Node temp = new Node(x_top, y_top, i, y_top,this);
						result.add(temp);
					}	
				}


			}
			// System.exit(1);
			return result;
		}
		public ArrayList<Node> left(Checker.Block block) {
			// System.out.println("LEFT LEFT");
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			if(this.blocks.get(this.board[x_top][y_top]) == null) {
				System.out.println("NULl left");
				System.out.println(block);
				System.out.println("x_top = " + x_top);
				System.out.println("y_top = " + y_top);
				System.out.println(blocks);
				System.exit(1);
			}
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			// System.out.println("Block = " + block);

			for(int i = y_top - 1; i >= 0; i--) {
				for(int j = x_top; j <= x_bottom; j++) {
					// System.exit(1);
					if(board[j][i].isOccupied()) {
						i = -1;
						break;
					}
				}
				// System.out.println("i = " + i);
				if(i != -1 && this.board[x_top][y_top].getName() != 0 && this.blocks.get(this.board[x_top][y_top]) != null) {
					// System.out.println("Here LEFTTTTTT");
					// System.exit(1);
					if(!hasSeen(block,x_top, i)) {
						Node temp = new Node(x_top, y_top, x_top, i,this);
						result.add(temp);
						// hasSeen.add(temp.state);
					}	
				}
			}
			return result;
		}
/*
		public ArrayList<Node> right(Checker.Block block) {
			// System.out.println("RIGHT RIGHT");
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(y_top - y_bottom);
			// System.out.println("Block = " + block);

			if(this.blocks.get(this.board[x_top][y_top]) == null) {
				System.out.println("NULl right");
				System.out.println(block);
				System.out.println("x_top = " + x_top);
				System.out.println("y_top = " + y_top);
				System.out.println(blocks);
				System.exit(1);
			}

			for(int i = y_top + 1; i + length < this.board[0].length; i++) {
				for(int j = x_top; j <= x_bottom; j++) {
					// System.exit(1);
					if(blocks.get(board[j][i]) != null) {
						name = blocks.get(board[j][i]).getName();
					}
					if(board[j][i].isOccupied() && name != block.getName()) {
						i = this.board[0].length;
						break;
					}
				}
				if(i < this.board[0].length && this.board[x_top][y_top].getName() != 0 && this.blocks.get(this.board[x_top][y_top]) != null) {
					if(!hasSeen(block,x_top, i)) {
						Node temp = new Node(x_top, y_top, x_top, i,this);
						result.add(temp);
					}	
				}
			}
			return result;



		}
		*/



		public ArrayList<Node> right(Checker.Block block) {
			// System.out.println("RIGHT RIGHT");
			ArrayList<Node> result = new ArrayList<Node>();
			int x_top = block.getTop().getX();
			int y_top = block.getTop().getY();
			int x_bottom = block.getBottom().getX();
			int y_bottom = block.getBottom().getY();
			int length = Math.abs(x_top - x_bottom);

			if(this.blocks.get(this.board[x_top][y_top]) == null) {
				System.out.println("NULl right");
				System.exit(1);
			}
			int k = 0;
			for(int x = y_bottom + 1; x < this.board[0].length; x++) {
				k++;
				for(int y = x_top; y <= x_bottom; y++) {

					if(this.board[y][x].isOccupied()) {
						x = this.board[0].length;
						break;
					}

				}

				if(x < this.board[0].length && this.board[x_top][y_top + k].getName() != 0) {
					if(!hasSeen(block,x_top, y_top + k)) {
						Node temp = new Node(x_top, y_top, x_top, y_top + k,this);
						result.add(temp);
						// hasSeen.add(temp.state);
					}	
				}


			}
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
				
						distance += (num*board[i][j].getName());
					}
					k++;
				}
			}


			// System.out.println("has seen distance = " + distance);
			return seenInt.contains(distance);
		}
		

		public boolean isGoal() {

			// int distance = 0;
			// System.out.println("size  state= " + this.allBlocks.size());
			for(Checker.Block b: goalBlocks) {
			
				int x_top = b.getTop().getX();
				int y_top = b.getTop().getY();
				int x_bottom = b.getBottom().getX();
				int y_bottom = b.getBottom().getY();
				if(this.board[x_top][y_top].getName() != 0) {
					Checker.Block tempB = blocks.get(this.board[x_top][y_top]);
					int x = tempB.getTop().getX();
					int y = tempB.getTop().getY();
					int xi = tempB.getBottom().getX();
					int yi = tempB.getBottom().getY();
					boolean legal1 = (x == x_top) && (y == y_top);
					boolean legal2 = (xi == x_bottom) && (yi == y_bottom);
					if( !(legal1 && legal2) ) {
						return false;
					}
				}
				else {
					return false;
				}

			}
			return true;
	
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
			return distance ;
		}
		public int state() {
			int distance = 0;
			int k = 0;
			for(int i = 0; i < this.board.length; i++) {
				for(int j = 0; j < this.board[0].length; j++) {
					if(this.board[i][j].isOccupied()) {
						// int k = i*this.board[0].length + j;
						// System.out.println("k = " + k);
						int num = prime.get(k);
						// System.out.println("herere");
						distance += (num*board[i][j].getName());// (Integer.parseInt(block.getName())*(num));
					}
					k++;
				}
			}
			return distance;
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