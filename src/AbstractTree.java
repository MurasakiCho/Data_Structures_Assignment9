public abstract class AbstractTree<E> implements Tree<E> {
    //Inorder traversal from the root
    @Override
    public void inorder() {}

    //Postorder traversal from the root
    @Override
    public void postorder() {}

    //Preorder traversal from the root
    @Override
    public void preorder() {}

    //Return true if the tree is empty
    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }
}
