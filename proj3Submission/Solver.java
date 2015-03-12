import java.util.*;
import java.io.*;


public class Solver {

	public Node goal;
	public HashSet<Integer> seenInt;
	public HashSet<Checker.Block> goalBlocks;
	public Stack<Node> fringe;

	private ArrayList<Integer> prime;
	public void prime(int x,int y) {
		prime = new ArrayList<Integer>();
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
		fringe = new Stack<Node>();
		goalBlocks = new HashSet<Checker.Block>();

		Node initial = new Node(init,false,0,0);
		seenInt = new HashSet<Integer>();
		seenInt.add(initial.state);

		this.goal = new Node(goal, true, initial.board.length, initial.board[0].length);
		goalBlocks = this.goal.allBlocks;

		fringe.push(initial);

	}

	public void solve(){
		int i = 0;
		Node low = null;
		while(!fringe.isEmpty()) {

			low = fringe.pop();
			i++;

			if(low.isGoal()) {
				for(String a: low.moves) {
					System.out.println(a);
				}
				return;
			}
			low.generateMoves();
		}
		return ;
	}

	public static void main(String[] args) {
		Solver thai = new Solver(args[0], args[1]);
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

			this.cost = 0;
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
			this.moves = new ArrayList<String>(otherNode.moves);
			String move = new String(x_top + " " + y_top + " " + x_to + " " + y_to); 
			
			this.moves.add(move);
			this.state = state();
			this.cost = computeCost();
	

		}

		public void generateMoves() {
			ArrayList<Node> result = new ArrayList<Node>();
			int i = 0;
			for(Checker.Block block: this.allBlocks) {
				result.addAll(move(block));			
			}

			for(Node k: result) {
				if(!seenInt.contains(k.state)) {
					seenInt.add(k.state);
					fringe.push(k);
				}
			}
		}
		public ArrayList<Node> move(Checker.Block bl){
			ArrayList<Node> temp = new ArrayList<Node>();
			int x = bl.getTop().getX();
			int y = bl.getTop().getY();
			Node n = null;

			if(((x-1) >= 0) && bl.canMakeMove(this.board[x - 1][y]) != 0) {
				n = new Node(x, y, x - 1, y, this);
				temp.add(n);
			}
			if((x+1) < board.length  && bl.canMakeMove(this.board[x + 1][y]) != 0) {
				n = new Node(x, y, x + 1, y, this);
				temp.add(n);
			}
			if( ((y-1) >= 0) && bl.canMakeMove(this.board[x][y - 1]) != 0) {
				n = new Node(x, y, x, y - 1, this);
				temp.add(n);
			}
			if((y+1) < board[0].length && bl.canMakeMove(this.board[x][y + 1]) != 0) {
				n = new Node(x, y, x, y + 1, this);
				temp.add(n);
			}
			return temp;
		}

		public boolean hasSeen(Checker.Block block, int x_to, int y_to) {
			int distance = 0;
			Checker.Coordinate[][] temp = new Checker.Coordinate[this.board.length][this.board[0].length];
			for(int i = 0; i < temp.length; i++) {
				for(int j = 0; j < temp[0].length; j++) {
					temp[i][j] = board[i][j].copyCoordinate();
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

			x_top = x_to;
			y_top = y_to;
			x_bottom = x_to + x_length;
			y_bottom = y_to + y_length;

			for(int i = x_to; i <= x_bottom; i++) {
				for(int j = y_to; j <= y_bottom; j++) {
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

			return seenInt.contains(distance);
		}
		

		public boolean isGoal() {

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
						
						int num = prime.get(k);
						
						distance += (num*board[i][j].getName());
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