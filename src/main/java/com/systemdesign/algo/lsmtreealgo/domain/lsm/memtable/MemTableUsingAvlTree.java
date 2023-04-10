package com.systemdesign.algo.lsmtreealgo.domain.lsm.memtable;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.systemdesign.algo.lsmtreealgo.domain.tree.Tree;
import com.systemdesign.algo.lsmtreealgo.domain.tree.TreeNode;

@Component
public class MemTableUsingAvlTree<T extends Comparable<T>> implements MemTable{
    private Tree<MemTableData> memTableStorageStructure;
    private BloomFilter<CharSequence> bloomFilter;

    public MemTableUsingAvlTree(Tree<MemTableData> memTable) {
        this.memTableStorageStructure = memTable;
        this.bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(Charset.defaultCharset()),
                50,
                0.01);
    }

    @Override
    public Tree<MemTableData> insert(MemTableData data) {
        this.bloomFilter.put(data.getKey());
        this.memTableStorageStructure = memTableStorageStructure.insert(data);
        return this.memTableStorageStructure;
    }

    @Override
    public TreeNode<MemTableData> search(MemTableData data) {
        if(!bloomFilter.mightContain(data.getKey())){
            return null;
        }
        return this.memTableStorageStructure.search(data);
    }

    @Override
    public TreeNode<MemTableData> getMin() {
        return this.memTableStorageStructure.getMin(this.memTableStorageStructure.getRoot());
    }

    @Override
    public TreeNode<MemTableData> getMax() {
        return this.memTableStorageStructure.getMax(this.memTableStorageStructure.getRoot());
    }

    @Override
    public boolean delete(MemTableData data) {
        return this.memTableStorageStructure.delete(data);
    }

    @Override
    public int size() {
        return this.memTableStorageStructure.size();
    }

    @Override
    public List<MemTableData> traverse() {
        return this.memTableStorageStructure.traverse();
    }

    @Override
    public void clear() {
        this.memTableStorageStructure.clear();
    }

    @Override
    public MemTable performDeepCopy() {
        Tree<MemTableData> newMemTable = this.memTableStorageStructure.performDeepCopy();
        MemTableUsingAvlTree<MemTableData> memTableUsingAvlTree = new MemTableUsingAvlTree<>(newMemTable);

        return memTableUsingAvlTree;
    }

}
