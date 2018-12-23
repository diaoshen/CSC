package red_black_binary_search_tree;
/*
 * Author : DIAO
 * Time created : 12/18/2018
 */



/*
 * RED-BLACK BINARY SEARCH TREE V1
 * 
 * This version of RED-BLACK BST has a 1-1 mapping to 2-3 Tree
 * 
 */

/*
 * Current Supported Operations :
 * 
 * public 	int 		size()						//Returns total number of TreeNodes 
 * private	boolean 	isRed(TreeNode x)			//Returns true if a TreeNode is red , Default = BLACK
 * private	TreeNode	rotateLeft(TreeNode h)		//Switch color between h and h.right and return h.right
 * private 	TreeNode	rotateRight(TreeNode h)		//Switch color between h and h.left and return h.left
 * private 	void		flipColors(TreeNode h)		//Change h to red  and childs of h to black
 * public 	void		add(Key key , Value val)	//Add a key/value pair to BST
 */


//Template class RedBlackBST uses Generic type "Key" and "Value"
//Key extends Comparable so compareTo can be used.
public class RedBlackBST <Key extends Comparable<Key> , Value> {

	
	/*
	 * Private members
	 */
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private TreeNode root;
	
	private class TreeNode{
		Key key;						//key
		Value val;						//associated value
		TreeNode left,right;			//Left subtree , right subtree
		int n;							// # of treenodes in this subtree
		boolean color;					//Color of node
		
		/*
		 * Constructor to create a TreeNode
		 */
		TreeNode(Key key, Value val, int N, boolean color){
			this.key = key;
			this.val = val;
			this.n = N;
			this.color = color;
		}
	}//END TREENODE CLASS
	
	
	
	/*
	 * Operation Definitions : 
	 */
	
	
	
	/*
	 * Returns total number of nodes
	 */
	public int size() {
		return size(root);
	}
	private int size(TreeNode x) {
		return x == null ? 0 : x.n;
	}
	
	/*
	 * is a node red?  Default color of a null tree is BLACK=FALSE, Any newly created TreeNode is RED
	 */
	private boolean isRed(TreeNode x) {
		if(x == null) return BLACK; 
		return x.color = RED;
	}
	
	/*
	 * is a node black?
	 */
	private boolean isBlack(TreeNode x) {
		return isRed(x) ? false : true;
	}
	
	/*
	 * Left Rotation
	 */
	private TreeNode rotateLeft(TreeNode h) {
		/*
		 * Input h is BLACK. h.right is RED , To switch them perform:
		 * Steps : 
		 * 1. Create a TreeNode and point to h.right(The RED Node)
		 * 2. Connect H's right link to X's left link
		 * 3. Connect X's left  link to H
		 * 4. X is the new root so update colors by setting X's color to H's color
		 * 5. H is now RED 
		 * 6. update N Field
		 */
		TreeNode x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		x.n = h.n;
		h.n = size(h.left) + size(h.right) + 1;
		return x;
	}
	
	/*
	 * Right rotation
	 */
	private TreeNode rotateRight(TreeNode h) {
		/*
		 *Similar Steps to rotateLeft
		 */
		TreeNode x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		x.n = h.n;
		h.n = size(h.left) + size(h.right) + 1;
		return x;
	}
	
	/*
	 * Color Flip
	 */
	private void flipColors(TreeNode h) {
		h.color = RED;
		h.left.color = BLACK;
		h.right.color = BLACK;
	}
	
	/*
	 *  add key/value pair 
	 */
	public void add(Key key , Value val) {
		root = add(root, key , val);
		root.color = BLACK;
	}
	private TreeNode add(TreeNode currNode , Key key , Value val) {
		
		/*
		 * Add  operation for the most part is same as regular BST
		 * Difference is : all new TreeNode is RED and must check for balance after inserting.
		 * 
		 * 1. If no TreeNode exists, or Reached to Bottom(this new node should be a leaf)
		 * 2. Else If , this node belongs to left branch
		 * 3. Else if , this node belongs to right branch
		 * 4. Else , Duplicate (Update val)
		 * 
		 */
		
		/*
		 * Case 1 When going left or right result to end of tree , then create node and return this.
		 * OR
		 * Case 2 This is a empty BST , then create node and return this.
		 */
		if( currNode == null) { 
			return new TreeNode(key , val , 1 , RED);
		}		
		/*
		 * Key is a generic type , could be Int , String , Double
		 * Because Key extends comparable , compareTo() knows what's the real data type of Key. 
		 */
		int cmp = key.compareTo(currNode.key);
		if(cmp < 0) { // key < current node's key , go left
			currNode.left = add(currNode.left , key , val); //calling add will eventually reach to x==null and turns a new node to be attach to x.left
		}else if(cmp > 0) { // key > current node's key , go right
			currNode.right = add(currNode.right , key , val);
		}else {	// key == current node's key , key trying to add already exist , update val
			currNode.val = val;
		}
		
		//When done inserting.. Tree might be un-balanced, so perform below steps to balance tree
		
		//BLACK LEFT  , RED RIGHT    then should rotate LEFT
		if(isRed(currNode.right) && !isRed(currNode.left)) {
			currNode = rotateLeft(currNode);
		}
		//2 Left RED link in a row    then should rotate RIGHT
		if(isRed(currNode.left) && isRed(currNode.left.left)) {
			currNode = rotateRight(currNode);
		}
		//Color Flip
		if(isRed(currNode.left) && isRed(currNode.right)) {
			flipColors(currNode);
		}
		
		currNode.n = size(currNode.left) + size(currNode.right) + 1;
		return currNode; // This returns to possibly case 2,3,4 thus will perform balance check at each level		
	}
	
	/*
	 * Return value of given key
	 */
	public Value get(Key key) {
		TreeNode x = root;
		while(x != null) {
			int cmp = key.compareTo(x.key);
			if(cmp == 0) return x.val;
			else if(cmp <0) x = x.left;
			else x = x.right;
		}
		return null;
	}
	
	/*
	 * fix un-balance (Perform series of local transformation to restore balance)
	 */
	private TreeNode fix(TreeNode h) {
		if(isRed(h.right))
			h = rotateLeft(h);
		if(isRed(h.left) && isRed(h.left.left)) 
			h = rotateRight(h);
		if(isRed(h.left) && isRed(h.right)) 
			flipColors(h);
		return h;
	}
	
	/*
	 * Helper function for deleteMax()
	 * Purpose : turn h into a 3-node or 4-node depending on h.left.left
	 */
	private TreeNode makeRedRight(TreeNode h) {
		flipColors(h); 
		//Now h is a 4-node after flipColors(h)
		if(isRed(h.left.left)) { // If there is 2 red in a row  h is a 5-node instead of 4-node
			//Break 5-node 
			h = rotateRight(h);
			flipColors(h);
			//Now h is a 3-node
		}
		return h; //In the end, h.right must be in either a 3-node or 4-node
	}
	
	/*
	 * Helper function for deleteMin()
	 * Purpose : to turn h into a 3-node or 4-node depending on h.right.left
	 */
	private TreeNode makeRedLeft(TreeNode h) {
		flipColors(h);
		//Now h is a 4-node
		if(isRed(h.right.left)) { //Why check h.right.left and not h.right.right? because is not possible to have a red link on the right after adding.
			//Break 5-node
			h.right = rotateRight(h.right); //After this will create 2 red link in a row on right
			h = rotateLeft(h); // move the 2 red link to left
			flipColors(h); // this will fix 2 red link in a row problem.
		}
		return h;
	}
	
	/*
	 * Delete minimum key
	 */
	public void deleteMin() {
		root = deleteMin(root);
		root.color = BLACK;
	}
	private TreeNode deleteMin(TreeNode h) {
		if(h.left == null) // remove node on bottom level (h must be RED by invariant)
			return null;
		
		if(isBlack(h.left) && isBlack(h.left.left)) //Push red link down if necessary
			h = makeRedLeft(h);
		
		h.left = deleteMin(h.left); // Move down one level
		
		return fix(h);
	}
	
	/*
	 * Delete maximum key
	 * 
	 * General strategy is carry a red-link down right subtree , if target key is found. It is within a 3-node or 4-node
	 * that way deleting a 3/4 node will not destroy balance.
	 */
	public void deleteMax() {
		root = deleteMax(root);
		root.color = BLACK;
	}
	private TreeNode deleteMax(TreeNode h) {
		if(isRed(h.left))
			h = rotateRight(h); //lean 3-nodes to right , prep for deletion
		if(h.right == null) //remove node on bottom level (h must be RED by invariant)
			return null;
		//Carry red-link down if there is not a red link at next level
		/*
		 * Also.. if h.right is the target max. then h.right must be in 3-node. That can only happen if h.right OR h.right.left is RED
		 * if none is RED , makeRedRight must be called so that h.right will be either a 3-node or 4-node
		 */
		if(isBlack(h.right) && isBlack(h.right.left)) //h.right is not RED
			h = makeRedRight(h); //Make it RED
		h.right = deleteMax(h.right); // Move down one level
		
		return fix(h); //fix right red link , double red link , break 4-node on the way up.
	}	
	
	
	/*
	 * Return minimum key
	 */
	public Key min() {
		TreeNode x = root;
		while(x.left != null) {
			x = x.left;
		}
		return x == null ? null : x.key;
	}
	
	/*
	 * Return maximum key
	 */
	public Key max() {
		TreeNode x = root;
		while(x.right != null) {
			x = x.right;
		}
		return x == null ? null : x.key;
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
