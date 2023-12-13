
/**
 * RBT
 * Red-Black Tree Insert
 * @author djr41 (djr41@kent.ac.uk)
 */
import java.util.*;
public class RBT {
    public Node root; //ROOT NODE

    public static void main(String[] args){
        RBT tree = new RBT();
        tree.insert(0); print(tree.root); System.out.println();
        tree.insert(1); print(tree.root); System.out.println();
        tree.insert(6); print(tree.root); System.out.println();
        tree.insert(3); print(tree.root); System.out.println();
        tree.insert(2); print(tree.root);System.out.println();
        tree.insert(4); print(tree.root);System.out.println();
    }

    private static void print(Node x){
        if (x == null) {
            return;
        }
        print(x.getLeft());
        System.out.print(x.getData() + "\n");
        print(x.getRight());
    }

    public RBT() {}

    public boolean isRed(Node x) {
        if (x == null) return false;
        return x.getColor() == Node.Color.RED;
    }
    
    public boolean isEmpty() {
        return root == null; 
    }

    public boolean contains(int x) {
        return nodeContainsData(root, x);
    }

    private boolean nodeContainsData(Node r, int x) {
        while (r != null) {
            if (r.getData() - x < 0) {
                r = r.getLeft();
            } else if (r.getData() - x > 0) {
                r = r.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Integer> serializeTree() {
        return serializeTree(root);
    }

    private List<Integer> serializeTree(Node r) {
        if (r == null) return new LinkedList<>();
        int data = r.getData();
        List<Integer> left = serializeTree(r.getLeft());
        List<Integer> right = serializeTree(r.getRight());
        left.add(data);
        left.addAll(right);
        return left;
    }

    public int maxHeight() {
        return maxHeight(root);
    }

    private int maxHeight(Node r) {
        if (r==null) return 0;        
        return 1 + Math.max(maxHeight(r.getLeft()), maxHeight(r.getRight()));
    }




    // ************************************************************************
    // * INSERT INTO RED-BLACK TREE
    // ************************************************************************

    public void insert(int x) {
        root = nodeInsertData(root, x);
        root.setColor(Node.Color.BLACK);
    }

    private Node nodeInsertData(Node r, int x) {
            Node newNode=new Node (x,Node.Color.RED); //make the created node a variable for testing purposes as ease of reading (all inserted nodes have to be red)
            

            //case 0: base case where is r is null (we have arrived to the leaves of the tree) create a new Node and insert it into the tree.
            if (r == null){
                return newNode;
            }
            //initialise 2 local variables parentNode and cur (currentNode) to enhance readability of code and to help track parent node and current node without compromising r (also for ease of debuggin purpose)
            Node curNode=r;
            Node parentNode=null;

            //whilst the current node (in this case r) is not equal to null we iteratively go down the tree until we reach the leaves 
            //where we can hen insert x (tried doing this recursively but results in SO errors for larger trees due to memory being filled)
            while(curNode!=null){
                parentNode=curNode;
                if(x<curNode.getData()){
            //case 1: if x is greater than the current node we go down the tree and make the current node the right child
                    curNode=curNode.getLeft();
                }else if(x>curNode.getData()){
            //case 2: if x is greater than the current node we go down the tree and make the current node the right child
                    curNode=curNode.getRight();
                }else{
                    return this.root;
                }
            }//now that we iterated down the tree and found out where x is going to be inserted we find out of we are going to insert it to the left or right of its parent node:
            //insertion step 2: for insertion now as r is at the leaves we take the parentNode and figure out if x is > or < then the
            //parentNode and insert it inside the tree as a child of that node
            if(x<parentNode.getData()){
                parentNode.setLeft(newNode);
            }else{
                parentNode.setRight(newNode);
            }//on spec it says we do not deal with duplicate values so not considered in this code
            //insertion step 3: when something is inserted/deleted from a R/B tree the tree has to be rebalanced, below we now rebalance the tree in reaction to the insertion of "newNode"
            
            
            rebalanceTree(newNode);//helper method below (for testing and abstraction purposes (allowed in spec)
            return this.root;
        }

        public Node rotateRight(Node h) {                              
            // Set variables for the parent and the left child of h.
            Node parent = getParent(h);
            
            Node leftChild = h.getLeft();

            if(!isNodesSafe(leftChild)){
                return h;
            }
    
            // Set the left child of h to the right child of the left child of h.
            h.setLeft(leftChild.getRight());
    
            // Set the right child of the left child of h to h.
            leftChild.setRight(h);

            if(isNodesSafe(parent)){
                if(leftChild.getData() < parent.getData()){
                    parent.setLeft(leftChild);
                }else{
                    parent.setRight(leftChild);
                }

                return leftChild;
            }

            // If the parent of h is null, set the root to the left child of h.
            // If not, set the left or right child of the parent of h to the left child of h.
            this.root = leftChild;
            
            return leftChild;
        }
    
        public Node rotateLeft(Node h) {
    
            // Set variables for the parent and the right child of h.
            Node parent = getParent(h);
            
            Node rightChild = h.getRight();

            if(!isNodesSafe(rightChild)){
                return h;
            }
    
            // Set the right child of h to the left child of the right child of h.
            h.setRight(rightChild.getLeft());
    
            // Set the left child of the right child of h to h.
            rightChild.setLeft(h);

            if(isNodesSafe(parent)){
                if(rightChild.getData() < parent.getData()){
                    parent.setLeft(rightChild);
                }else{
                    parent.setRight(rightChild);
                }

                return rightChild;
            }


            // If the parent of h is null, set the root to the right child of h.
            // If not, set the left or right child of the parent of h to the right child of h.
            //error fixed was reutrning root instead of this.root so it was returning the old tree instead of the new one (phantom deletion)
            this.root = rightChild;
            
            return this.root;
        }



    // flip the colors of a node and its two children
    private void flipColors(Node h) {

        if(h == null){//base case
            return;
        }

        //here we are setting the colour of node h and its two children to be flipped (by use of flipColour method given to us in NODE.jav class)
        h.setColor(Node.flipColor(h.getColor()));

        Node lChild = h.getLeft();//better for space complexity
        Node rChild = h.getRight();

        if(isNodesSafe(lChild)){//check is leftChild is not null
            lChild.setColor(Node.flipColor(lChild.getColor()));
        }

        if(isNodesSafe(rChild)){//check is rightChild is not null
            rChild.setColor(Node.flipColor(rChild.getColor()));
        }
    }//DONE;make one instance vairable for h.getLeft()/getRIGHT as its slightly quicker (less memory wasteage);good practice

//---------------------------------------------------------------------------
//extra methods I added to help with the balancing function (also to help debugging and abstraction) (all of them are called in the marked methods and are allowed)

//helper method to check if nodes are safe (not null) (did Node... if i want to check multiple nodes at once) used for error handling)
    private boolean isNodesSafe(Node... nodes){
        for(Node node : nodes){
            if(node == null){
                return false;
            }
        }
        return true;
    }

//so the insertNode method isnt as bloaded I abstracted the rebalacing method into its own method and call it from the method when a node is inserted.
    private void rebalanceTree(Node curNode){ //we can do this via recursion as unlike insert, this shouldnt have space complexity problems (found out through testing with big trees insertioon via recursion give stack overflow error)
        
        Node parentNode = getParent(curNode); //we need this to set the if statement to prevent null exception point

        if(curNode.getData() != root.getData() && isRed(parentNode)){//we move leave-root through the tree from the position inserted to root to make sure that whole side of the tree is balanced
            Node grandParentNode = getParent(parentNode);//initialise and obtain parentNode
            Node uncleNode = getUncle(parentNode, grandParentNode);//initialise and obtain uncle

            if(grandParentNode == null){
                return;
            }

            if(isRed(uncleNode)){
                //case 2 for rebalancing: when uncle is red we flip colours of parent,uncle and grandparent node; we do this thanks to flipColors method already implemented and call it recursively
                
                flipColors(grandParentNode);

                this.rebalanceTree(grandParentNode);
                return;
            }

            Node leftUncle = grandParentNode.getLeft();
            Node rightUncle = grandParentNode.getRight();
            
            //case 3 && case 3&4 for the left side of the tree
            if(isNodesSafe(leftUncle) && leftUncle.getData() == parentNode.getData()){
                leftSideRebalancing(curNode,parentNode,grandParentNode);
                return;
            }
            
            //case 3 && case 3&4 for the left side of the tree
            if(isNodesSafe(rightUncle) && rightUncle.getData() == parentNode.getData()){
                rightSideRebalancing(curNode,parentNode,grandParentNode);
                return;
            }
        }

        //case 1 for reabalancing: make the root black
        this.root.setColor(Node.Color.BLACK);
    }   

//helper method to getNodesUncle 
    private Node getUncle(Node parentNode, Node grandparentNode){
    //set instance variable for uncle and set to null (well use this later)
        Node uncle = null;
    //check if grandparent is null as we need a grandparent for an uncle to exist
        if(grandparentNode!=null){
            //we can exhaust all possibilities in one if statement, if grandparents left node is the parent (same data) we know the right is the uncle and if it isnt but not null its also the uncle
            if(grandparentNode.getLeft()!=null && grandparentNode.getLeft().getData()==parentNode.getData()){
                uncle=grandparentNode.getRight();
            }else{
                uncle=grandparentNode.getLeft();
            }
        }
    //return uncle as we now found uncle
        return uncle;
    }
//helper method to find parent (and by extension grandparent (by calling getparent twice as it returns the parent of the parent))
//we need this to recursively go through larger trees, to make sure its completly balanced
    private Node getParent(Node curNode){
        //this method is very similar to the insert method where we iteratively go down the R/B tree until cur is found
        //whilst this happens we track the parentNode so when cur is found, in consequence parent is already found
        //initialise method parameters:
        Node traversalNode = root; //here were setting "traversalNode" as the root as we are starting at the top of the RB tree and traversing down
       
        while(traversalNode!=null){
            Node leftChild = traversalNode.getLeft();
            Node rightChild = traversalNode.getRight();

            if(isNodesSafe(leftChild) && leftChild.getData()==curNode.getData()){
                return traversalNode;
            }

            if(isNodesSafe(rightChild) && rightChild.getData()==curNode.getData()){
                return traversalNode;
            }

            if(curNode.getData() < traversalNode.getData()){
                traversalNode = traversalNode.getLeft();
            }else{
                traversalNode = traversalNode.getRight();
            }
        }

        //error since we couldnt find "curNode" in the tree
        return null; 
    }

    //helper method for case 3 && case 4 (left side of tree)
    private void leftSideRebalancing(Node curNode,Node parentNode,Node grandParentNode){
        if(isNodesSafe(parentNode.getRight()) && parentNode.getRight().getData() == curNode.getData()){
            //case 3 (triangle) "X.uncle == black" ; what we do is we rotate left through the parent node as curNode is is the right child of the parentNode
            rotateLeft(parentNode);
            //update the colors of curNode and grandParentNode
            curNode.setColor(Node.Color.BLACK);//FIXED:ADD CONDITIONAL STATEMENT TO BOTH OF THESE INCASE THEIR NOT RED/BLACK RESPECTIVELY
            grandParentNode.setColor(Node.Color.RED);
            //follow with case 4 (line) as case 4 always follows 3
            rotateRight(grandParentNode);
        } else{
            //case 4 "line"
            rotateRight(grandParentNode);             
            parentNode.setColor(Node.Color.BLACK);
            grandParentNode.setColor(Node.Color.RED);
        }
    }

    //helper method for case 3 && case 4 (right side of tree)
    private void rightSideRebalancing(Node curNode, Node parentNode, Node grandParentNode){
        if(isNodesSafe(parentNode.getLeft()) && parentNode.getLeft().getData() == curNode.getData()){
            //case 3 (triangle) "X.uncle == black" ; what we do is we rotate right through the parent node as curNode is is the left child of the parentNode
            rotateRight(parentNode);
            //update the colors of curNode and grandParentNode
            curNode.setColor(Node.Color.BLACK);//FIXED:ADD CONDITIONAL STATEMENT TO BOTH OF THESE INCASE THEIR NOT RED/BLACK RESPECTIVELY
            grandParentNode.setColor(Node.Color.RED);
            //follow with case 4 (line) as case 4 always follows 3
            rotateLeft(grandParentNode);
        } else{
            //case 4 "line"
            rotateLeft(grandParentNode);             
            parentNode.setColor(Node.Color.BLACK);
            grandParentNode.setColor(Node.Color.RED);
        }
    }
}