package com.systemdesign.algo.lsmtreealgo.domain.tree;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<T extends Comparable<T>> {
    private T data;
    private int height;
    private TreeNode<T> leftTreeNode;
    private TreeNode<T> rightTreeNode;

    public TreeNode(T data){
        this.data = data;
        this.height = 1;
        this.leftTreeNode = null;
        this.rightTreeNode = null;
    }

}
