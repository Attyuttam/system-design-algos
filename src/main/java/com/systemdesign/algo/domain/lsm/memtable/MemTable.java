package com.systemdesign.algo.domain.lsm.memtable;

import java.util.List;

import com.systemdesign.algo.domain.tree.Tree;
import com.systemdesign.algo.domain.tree.TreeNode;

//As we know MemTables are immutable, we do not have delete method
public interface MemTable {
    public Tree<MemTableData> insert(MemTableData data);
    public boolean delete(MemTableData data);
    public TreeNode<MemTableData> search(MemTableData data);
    public TreeNode<MemTableData> getMin();
    public TreeNode<MemTableData> getMax();
    public int size();
    public List<MemTableData> traverse();
    public void clear();
    public MemTable performDeepCopy();
}
