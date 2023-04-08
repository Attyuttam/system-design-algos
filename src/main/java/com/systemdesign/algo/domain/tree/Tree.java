package com.systemdesign.algo.domain.tree;

import java.util.List;

public interface Tree<T extends Comparable<T>> {
    Tree<T> insert(T data);
    boolean delete(T data);
    List<T> traverse();
    TreeNode<T> getMax(TreeNode<T> treeNode);
    TreeNode<T> getMin(TreeNode<T> treeNode);
    boolean isEmpty();
    TreeNode<T> search(T data);
    TreeNode<T> getRoot();
    int size();
    void clear();
    Tree<T> performDeepCopy();
}
