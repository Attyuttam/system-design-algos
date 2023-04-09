package com.systemdesign.algo.application.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.domain.avl.AvlTree;
import com.systemdesign.algo.domain.lsm.memtable.MemTableData;
import com.systemdesign.algo.domain.lsm.memtable.MemTableUsingAvlTree;
import com.systemdesign.algo.domain.lsm.sstable.SortedStringTable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Setter
@Slf4j
public class DiskStorageService {
    private List<SortedStringTable> sortedStringTableList = new ArrayList<>();

    @Value("${sorted.string.table.size.limit}")
    private Integer sortedStringTableListSizeLimit;

    public void mergeSSTables(){
        log.info("Size of LSM Tree before merging: "+this.sortedStringTableList.size());
        while(sortedStringTableList.size() >= sortedStringTableListSizeLimit){
            log.info("Merging SS Tables");
            Collections.sort(sortedStringTableList, new SSTComparator());
            SortedStringTable mergedSST = mergeSSTables(sortedStringTableList.get(0), sortedStringTableList.get(1));
            sortedStringTableList.remove(0);
            sortedStringTableList.remove(1);
            sortedStringTableList.add(mergedSST);
        }
        log.info("Size of LSM Tree after merging: "+this.sortedStringTableList.size());
    }

    private SortedStringTable mergeSSTables(SortedStringTable sortedStringTable1, SortedStringTable sortedStringTable2) {
        List<MemTableData> sortedStringTable1Contents = sortedStringTable1.getMemTable().performDeepCopy().traverse();
        List<MemTableData> sortedStringTable2Contents = sortedStringTable2.getMemTable().performDeepCopy().traverse();
        
        AvlTree<MemTableData> avlTree = new AvlTree<>();
        sortedStringTable1Contents.forEach(sst -> avlTree.insert(sst));
        sortedStringTable2Contents.forEach(sst -> avlTree.insert(sst));

        MemTableUsingAvlTree<MemTableData> memTableUsingAvlTree = new MemTableUsingAvlTree<>(avlTree);
        SortedStringTable mergedSortedStringTable = new SortedStringTable(memTableUsingAvlTree);
        return mergedSortedStringTable;
    }

    public void addToSSTableList(SortedStringTable sst){
        this.sortedStringTableList.add(sst);
        printSortedStringTableList();
    }
    public List<SortedStringTable> viewDiskContents(){
        return this.sortedStringTableList;
    }
    private void printSortedStringTableList() {
        System.out.println("The Sorted String Table List");
        sortedStringTableList.forEach(System.out::println);
    }

    public MemTableData findDataByKey(String key) {
        for(SortedStringTable sst : sortedStringTableList){
            MemTableData mmt = (MemTableData) sst.getData(new MemTableData(key, null));
            if(mmt == null)continue;

            return MemTableData.builder()
            .content(mmt.getContent())
            .key(mmt.getKey())
            .build();
        }
        
        return null;
    }

    public void delete(String key) {
        for(SortedStringTable sst : sortedStringTableList){
            boolean deleted = sst.delete(key);
            if(deleted)return;
        }
    }
}

class SSTComparator implements Comparator<SortedStringTable>{

    @Override
    public int compare(SortedStringTable sst1, SortedStringTable sst2) {
        if(sst1.getMemTable().size() == sst2.getMemTable().size())return 0;
        if(sst1.getMemTable().size() < sst2.getMemTable().size()) return -1;
        return 1;
    }

}
