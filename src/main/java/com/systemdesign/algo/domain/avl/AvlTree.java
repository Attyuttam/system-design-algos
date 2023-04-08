package com.systemdesign.algo.domain.avl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.systemdesign.algo.domain.tree.Tree;
import com.systemdesign.algo.domain.tree.TreeNode;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Component
@Slf4j
public class AvlTree<T extends Comparable<T>> implements Tree<T> {
    private TreeNode<T> root;
    private int numberOfElements = 0;

    @Override
    public Tree<T> insert(T data) {
        this.root = insert(data, this.root);
        return this;
    }

    private TreeNode<T> insert(T data, TreeNode<T> treeNode) {
        
        if (treeNode == null){
            this.numberOfElements++;
            log.info("The number of elements in the mem table: "+this.numberOfElements);
            return new TreeNode<T>(data);
        }

        if (data.compareTo(treeNode.getData()) < 0) {
            treeNode.setLeftTreeNode(insert(data, treeNode.getLeftTreeNode()));
        } else if (data.compareTo(treeNode.getData()) > 0) {
            treeNode.setRightTreeNode(insert(data, treeNode.getRightTreeNode()));
        } else {
            treeNode.setData(data);
            return treeNode;
        }
        updateHeight(treeNode);
        return applyRotation(treeNode);
    }

    private TreeNode<T> applyRotation(TreeNode<T> treeNode) {
        int balance = balance(treeNode);
        if (balance > 1) {
            if (balance(treeNode.getLeftTreeNode()) < 0) {
                treeNode.setLeftTreeNode(rotateLeft(treeNode.getLeftTreeNode()));
            }
            return rotateRight(treeNode);
        }
        if (balance < -1) {
            if (balance(treeNode.getRightTreeNode()) > 0) {
                treeNode.setRightTreeNode(rotateRight(treeNode.getRightTreeNode()));
            }
            return rotateLeft(treeNode);
        }
        return treeNode;
    }

    private TreeNode<T> rotateRight(TreeNode<T> treeNode) {
        TreeNode<T> leftTreeNode = treeNode.getLeftTreeNode();
        TreeNode<T> centerTreeNode = treeNode.getRightTreeNode();
        leftTreeNode.setRightTreeNode(treeNode);
        treeNode.setLeftTreeNode(centerTreeNode);
        updateHeight(treeNode);
        updateHeight(leftTreeNode);
        return leftTreeNode;
    }

    private TreeNode<T> rotateLeft(TreeNode<T> treeNode) {
        TreeNode<T> rightTreeNode = treeNode.getRightTreeNode();
        TreeNode<T> centerTreeNode = treeNode.getLeftTreeNode();
        rightTreeNode.setLeftTreeNode(treeNode);
        treeNode.setRightTreeNode(centerTreeNode);
        updateHeight(treeNode);
        updateHeight(rightTreeNode);
        return rightTreeNode;
    }

    private int balance(TreeNode<T> treeNode) {
        return ((treeNode.getLeftTreeNode() == null) ? 0 : treeNode.getLeftTreeNode().getHeight())
                - ((treeNode.getRightTreeNode() == null) ? 0 : treeNode.getRightTreeNode().getHeight());
    }

    private void updateHeight(TreeNode<T> treeNode) {
        int maxHeight = Math.max(height(treeNode.getLeftTreeNode()), height(treeNode.getRightTreeNode()));
        treeNode.setHeight(maxHeight + 1);
    }

    private int height(TreeNode<T> treeNode) {
        return treeNode != null ? treeNode.getHeight() : 0;
    }

    @Override
    public boolean delete(T data) {
        List<Boolean> deleted = new ArrayList<>();
        deleted.add(false);
        this.root = delete(data, this.root,deleted);
        if(deleted.get(0).booleanValue())this.numberOfElements--;
        return deleted.get(0).booleanValue();
    }

    private TreeNode<T> delete(T data, TreeNode<T> treeNode, List<Boolean> deleted) {
        if (treeNode == null)
            return null;
        if (data.compareTo(treeNode.getData()) < 0) {
            treeNode.setLeftTreeNode(delete(data, treeNode.getLeftTreeNode(),deleted));
        } else if (data.compareTo(treeNode.getData()) > 0) {
            treeNode.setRightTreeNode(delete(data, treeNode.getRightTreeNode(),deleted));
        } else {
            deleted.set(0,true);
            // one child or leaf node(no children)
            if (treeNode.getLeftTreeNode() == null) {
                return treeNode.getRightTreeNode();
            } else if (treeNode.getRightTreeNode() == null) {
                return treeNode.getLeftTreeNode();
            }

            // two children
            treeNode.setData(getMax(treeNode.getLeftTreeNode()).getData());
            treeNode.setLeftTreeNode(delete(treeNode.getData(), treeNode.getLeftTreeNode(), deleted));
        }
        updateHeight(treeNode);
        return applyRotation(treeNode);
    }

    @Override
    public List<T> traverse() {
        List<T> traverseList = new ArrayList<>();
        traverse(this.root, traverseList);
        return traverseList;
    }

    private void traverse(TreeNode<T> treeNode, List<T> traverseList) {
        if (treeNode != null) {
            traverse(treeNode.getLeftTreeNode(),traverseList);
            traverseList.add(treeNode.getData());
            traverse(treeNode.getRightTreeNode(),traverseList);
        }
    }

    @Override
    public TreeNode<T> getMax(TreeNode<T> treeNode) {
        if(treeNode == null)return treeNode;

        TreeNode<T> currTreeNode = treeNode;

        while (currTreeNode.getRightTreeNode() != null) {
            currTreeNode = currTreeNode.getRightTreeNode();
        }

        return currTreeNode;
    }

    @Override
    public TreeNode<T> getMin(TreeNode<T> treeNode) {
        if(treeNode == null)return treeNode;

        TreeNode<T> currTreeNode = treeNode;

        while (currTreeNode.getLeftTreeNode() != null) {
            currTreeNode = currTreeNode.getLeftTreeNode();
        }

        return currTreeNode;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public TreeNode<T> search(T data) {
        return search(data, this.root);
    }

    private TreeNode<T> search(T data, TreeNode<T> treeNode) {
        if(treeNode == null)return null;
        if(treeNode.getData().compareTo(data) == 0)return treeNode;

        if(treeNode.getData().compareTo(data) < 0){
            return search(data, treeNode.getRightTreeNode());
        }

        return search(data, treeNode.getLeftTreeNode());
    }

    @Override
    public TreeNode<T> getRoot() {
        return this.root;
    }

    @Override
    public int size() {
        return this.numberOfElements;
    }

    @Override
    public void clear() {
        this.root = null;
        this.numberOfElements = 0;
    }

    @Override
    public Tree<T> performDeepCopy() {
        TreeNode<T> root = performDeepCopy(this.root);
        AvlTree<T> newTree = new AvlTree<>();
        newTree.root = root;
        newTree.numberOfElements = this.numberOfElements;
        return newTree;
    }

    private TreeNode<T> performDeepCopy(TreeNode<T> treeNode) {
        if(treeNode == null)return treeNode;
        TreeNode<T> newRoot = new TreeNode<>();
        newRoot.setData(treeNode.getData());
        newRoot.setHeight(treeNode.getHeight());
        newRoot.setLeftTreeNode(performDeepCopy(treeNode.getLeftTreeNode()));
        newRoot.setRightTreeNode(performDeepCopy(treeNode.getRightTreeNode()));
        return newRoot;
    }

}
