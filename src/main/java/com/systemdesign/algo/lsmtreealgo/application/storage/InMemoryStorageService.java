package com.systemdesign.algo.lsmtreealgo.application.storage;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.lsmtreealgo.application.storage.exception.DataInsertIntoStorageException;
import com.systemdesign.algo.lsmtreealgo.application.storage.exception.DataTransferException;
import com.systemdesign.algo.lsmtreealgo.domain.lsm.memtable.MemTable;
import com.systemdesign.algo.lsmtreealgo.domain.lsm.memtable.MemTableData;
import com.systemdesign.algo.lsmtreealgo.domain.lsm.sstable.SortedStringTable;
import com.systemdesign.algo.lsmtreealgo.domain.tree.Tree;
import com.systemdesign.algo.lsmtreealgo.domain.tree.TreeNode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class InMemoryStorageService {
    private final MemTable memTable;
    private final DiskStorageService diskStorageService;
    

    @Value("${memory.storage.limit}")
    private int memoryStorageLimit;

    public Tree<MemTableData> insertDataIntoMemory(MemTableData memTableData) throws DataInsertIntoStorageException{
        Tree<MemTableData> result = null;
        try{
            result = this.memTable.insert(memTableData);
        }catch(Exception ex){
            log.error("",ex);
            throw new DataInsertIntoStorageException("Insertion into in-memory store failed", ex);
        }
        return result;
    }

    public void deleteDataByKey(String key) {
        boolean deleted = this.memTable.delete(new MemTableData(key, null));

        if(deleted){
            return;
        }

        diskStorageService.delete(key);
    }

    public void transferMemTableToDisk() throws DataTransferException{
        if(this.memTable.size() >= memoryStorageLimit){
            try{
                MemTable toCopyMemTable = this.memTable.performDeepCopy();
                SortedStringTable sst = new SortedStringTable(toCopyMemTable);
                diskStorageService.addToSSTableList(sst);
                this.memTable.clear();
            }catch(Exception ex){
                log.info("Data transfer from in-memory to disk failed");
                throw new DataTransferException("Data transfer from in-memory to disk failed", ex);
            }
        }
    }

    public List<SortedStringTable> viewDiskStorage(){
        return diskStorageService.viewDiskContents();
    }

    public List<MemTableData> viewInMemoryStorage(){
        return this.memTable.traverse();
    }

    public MemTableData findDataByKey(String key) {
        TreeNode<MemTableData> foundData = this.memTable.search(new MemTableData(key, null));

        if(foundData != null){
            return foundData.getData();
        }

        return diskStorageService.findDataByKey(key);
    }
}
