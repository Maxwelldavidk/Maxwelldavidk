public class AVLTree  <K extends Comparable<K>, V> extends BinarySearchTree<K,V> {


    public AVLTree() {
        super();
    }


    public V insert(K key, V value) {
        V oldValue = super.insert(key, value);  // Call the insert method
        root = insert(key, value, root);  // Insert the key-value pair and balance the tree
        return oldValue;  // Return the old value associated with the key
    }

    // Helper method to insert a key-value pair and balance the tree
    private TreeNode<K,V> insert(K key, V value, TreeNode<K,V> node) {
        if (node == null) {
            size++;  // Increase the size of the tree
            return new TreeNode<>(key, value);  // Create a new node if the current node is null
        }
        int compareResult = key.compareTo(node.key);  // Compare the given key with the current node's key
        if (compareResult < 0) {
            node.left = insert(key, value, node.left);  // Insert in the left subtree
        } else if (compareResult > 0) {
            node.right = insert(key, value, node.right);  // Insert in the right subtree
        } else {
            node.value = value;  // Update the value if the keys are equal
        }
        return balance(node);  // Balance the tree and return the node
    }

    // Balances the AVL tree at the given node
    private TreeNode<K,V> balance(TreeNode<K,V> node) {
        if (node == null) {
            return node;  // Return if the node is null
        }
        // Check if the left subtree is higher than the right subtree by more than 1
        if (height(node.left) - height(node.right) > 1) {
            // Perform a single or double rotation with the left child
            if (height(node.left.left) >= height(node.left.right)) {
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (height(node.right) - height(node.left) > 1) {
            // Perform a single or double rotation with the right child
            if (height(node.right.right) >= height(node.right.left)) {
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }
        node.updateHeight();  // Update the height of the node
        return node;  // Return the balanced node
    }

    // Returns the height of the given node
    private int height(TreeNode<K,V> node) {
        return node == null ? -1 : node.height;  // Return -1 if the node is null otherwise return the node's height
    }

    // Single rotation with left child
    private TreeNode<K,V> rotateWithLeftChild(TreeNode<K,V> node) {
        TreeNode<K,V> leftChild = node.left;  // Get the left child of the node
        node.left = leftChild.right;  // Move the right subtree of the left child to the left of the node
        leftChild.right = node;  // Make the node the right child of the left child
        node.updateHeight();  // Update the height of the node
        leftChild.updateHeight();  // Update the height of the left child
        return leftChild;  // Return the new root of the subtree
    }

    // Single rotation with right child
    private TreeNode<K,V> rotateWithRightChild(TreeNode<K,V> node) {
        TreeNode<K,V> rightChild = node.right;  // Get the right child of the node
        node.right = rightChild.left;  // Move the left subtree of the right child to the right of the node
        rightChild.left = node;  // Make the node the left child of the right child
        node.updateHeight();  // Update the height of the node
        rightChild.updateHeight();  // Update the height of the right child
        return rightChild;  // Return the new root of the subtree
    }

    // Double rotation with left child
    private TreeNode<K,V> doubleWithLeftChild(TreeNode<K,V> node) {
        node.left = rotateWithRightChild(node.left);  // First rotate the left child with its right child
        return rotateWithLeftChild(node);  // Then rotate the node with its left child
    }

    // Double rotation with right child
    private TreeNode<K,V> doubleWithRightChild(TreeNode<K,V> node) {
        node.right = rotateWithLeftChild(node.right);  // First rotate the right child with its left child
        return rotateWithRightChild(node);  // Then rotate the node with its right child
    }
}