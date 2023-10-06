public class AVLTree<E extends Comparable<E>> extends BST<E> {
    //create an empty AVL tree
    public AVLTree() {
    }

    //create an AVL tree from an array of objects
    public AVLTree(E[] objects) {
        super(objects);
    }

    //override createNewNOde to create an AVLTreeNode
    @Override
    protected AVLTreeNode<E> createNewNode(E e) {
        return new AVLTreeNode<E>(e);
    }

    //Insert an element and re-balance if necessary
    @Override
    public boolean insert(E e) {
        boolean successful = super.insert(e);
        if (!successful)
            return false; //e is already in the tree
        else {
            balancePath(e); //Balance from e to the root if necessary
        }

        return true; //e is inserted
    }

    //update the height of a specified node
    private void updateHeight(AVLTreeNode<E> node) {
        if (node.left == null && node.right == null) //node is a leaf
            node.height = 0;
        else if (node.left == null) //node has no left subtree
            node.height = 1 + ((AVLTreeNode<E>) (node.right)).height;
        else if (node.right == null) //node has no right subtree
            node.height = 1 + ((AVLTreeNode<E>) (node.left)).height;
        else
            node.height = 1 + Math.max(((AVLTreeNode<E>) (node.left)).height, ((AVLTreeNode<E>) (node.left)).height);

    }

    //Balance the nodes in the path from the specified node to the root if necessary
    private void balancePath(E e){
        java.util.ArrayList<TreeNode<E>> path = path(e);
        for(int i = path.size() - 1; i >= 0; i--){
            AVLTreeNode<E> A = (AVLTreeNode<E>)(path.get(i));
            updateHeight(A);
            AVLTreeNode<E> parentOfA = (A == root) ? null:
                    (AVLTreeNode<E>)(path.get(i - 1));

            switch (balanceFactor(A)){
                case -2:
                    if(balanceFactor((AVLTreeNode<E>) A.left) <= 0){
                        balanceLL(A, parentOfA); //perform LL rotation
                    }
                    else{
                        balanceLR(A, parentOfA); //perform LR rotation
                    }
                    break;
                case +2:
                    if(balanceFactor((AVLTreeNode<E>)A.right) >= 0){
                        balanceRR(A, parentOfA); //perform RR rotation
                    }
                    else{
                        balanceRL(A, parentOfA); //perform RL rotation
                    }
                    break;
            }
        }
    }

    //return the balance factor of the node
    private int balanceFactor(AVLTreeNode<E> node){
        if(node.right == null) //node has no right subtree
            return -node.height;
        else if (node.left == null) //node has no left subtree
            return +node.height;
        else
            return((AVLTreeNode<E>)node.right).height - ((AVLTreeNode<E>)node.left).height;
    }

    //balance LL
    private void balanceLL(TreeNode<E> A,TreeNode<E> parentOfA){
        TreeNode<E> B = A.left; // A is left-heavy and B is left-heavy

        if (A == root) {
            root = B;
        }
        else{
            if(parentOfA.left == A){
                parentOfA.left = B;
            }
            else{
                parentOfA.right = B;
            }
        }

        A.left = B.right; //Make T2 the left subtree of A
        B.right = A; //Make A the left child of B;
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
    }

    //Balance LR
    private void balanceLR(TreeNode<E> A, TreeNode<E> parentOfA){
        TreeNode<E> B = A.left; //A is left-heavy
        TreeNode<E> C = B.right; //B is right-heavy

        if(A == root){
            root = C;
        }
        else{
            if(parentOfA.left == A){
                parentOfA.left = C;
            }
            else{
                parentOfA.right = C;
            }
        }

        A.left = C.right; //make T3 the left subtree of A
        B.right = C.left; //make T2 the right subtree of B
        C.left = B;
        C.right = A;

        //Adjust heights
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
        updateHeight((AVLTreeNode<E>) C);
    }

    //Balance RR
    private void balanceRR(TreeNode<E> A, TreeNode<E> parentOfA){
        TreeNode<E> B = A.right; //A is right-heavy and B is right-heavy

        if(A == root){
            root = B;
        }
        else{
            if(parentOfA.left == A){
                parentOfA.left = B;
            }
            else{
                parentOfA.right = B;
            }
        }

        A.right = B.left; //make T2 the right subtree of A
        B.left = A;
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
    }

    //Balance RL
    private void balanceRL(TreeNode<E> A, TreeNode<E> parentOfA){
        TreeNode<E> B = A.right; //A is right-heavy
        TreeNode<E> C = B.left; //B is left-heavy

        if(A == root){
            root = C;
        }
        else{
            if(parentOfA.left == A){
                parentOfA.left = C;
            }
            else{
                parentOfA.right = C;
            }
        }

        A.right = C.left; // make T2 the right subtree of A
        B.left = C.right; //make T3 the left subtree of B
        C.left = A;
        C.right = B;

        //adjust heights
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
        updateHeight((AVLTreeNode<E>) C);
    }

    //delete an element from the AVL Tree
    //return true if the element is deleted successfully
    //return false if the element is not in the tree
    @Override
    public boolean delete(E element){
        if(root == null)
            return false; //element is not in the tree

        //locate the node to be deleted and also locate its parent node
        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while(current != null){
            if(element.compareTo(current.element) < 0){
                parent = current;
                current = current.left;
            }
            else if(element.compareTo(current.element) > 0){
                parent = current;
                current = current.right;
            }
            else
                break; //element is in the tree pointed by current
        }
        if(current == null)
            return false; //element is not in the tree

        //Case1: current has no left children
        if(current.left == null){
            //connect the parent with the right child of the current node
            if(parent == null){
                root = current.right;
            }
            else{
                if(element.compareTo(parent.element) < 0)
                    parent.left = current.right;
                else
                    parent.right = current.right;

                //Balance tree if necessary
                balancePath(parent.element);
            }
        }
        else{
            //Case 2: the current node has a left child
            //locate the rightmost node in the left subtree of the current node and also its parent
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while(rightMost.right != null){
                parentOfRightMost = rightMost;
                rightMost = rightMost.right; //keep going to the right
            }

            //replace the element in current by the element in rightMost
            current.element = rightMost.element;

            //Eliminate rightmost node
            if(parentOfRightMost.right == rightMost)
                parentOfRightMost.right = rightMost.left;
            else
                //special case: parentOfRightMost is current
                parentOfRightMost.left = rightMost.left;

            //balance the tree if necessary
            balancePath(parentOfRightMost.element);
        }
        size--;
        return true; //element deleted
    }

    //AVLTreeNode is TreeNode plus height
    protected static class AVLTreeNode<E extends Comparable<E>> extends BST.TreeNode<E>{
        protected  int  height = 0; //new data field

        public AVLTreeNode(E e){
            super(e);
        }
    }

    public TreeNode<E> getRoot(){
        return root;
    }

    public boolean isBST(TreeNode<E> root){
        //an empty tree is a binary search tree
        if(root == null)
            return true;

        //if the root's left element is greater than the root's element, then the tree is not a BST
        if(root.left != null && root.left.element.compareTo(root.element) > 0){
            return false;
        }

        //if the root's right element is less than the root's element, then the tree is not a BST
        if(root.right != null && root.right.element.compareTo(root.element) < 0){
            return false;
        }

        boolean left = isBST(root.left);
        boolean right = isBST(root.right);

        return left && right;
    }

    public boolean isBalanced(TreeNode<E> root){
        int leftHeight; //height of left subtree
        int rightHeight; //height of right subtree

        //empty trees are balanced
        if(root == null)
            return true;

        leftHeight = height(root.left);
        rightHeight = height(root.right);

        return Math.abs(leftHeight - rightHeight) <= 1 && isBalanced(root.left) && isBalanced(root.right);
    }

    //calculate height of a node
    protected int height(TreeNode<E> node){
        //base case
        if(node == null)
            return 0;

        return 1 + Math.max(height(node.left), height(node.right));
    }
}