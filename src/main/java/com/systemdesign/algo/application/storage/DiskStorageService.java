package com.systemdesign.algo.application.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.domain.lsm.memtable.MemTableData;
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
        if(sortedStringTableList.size() >= sortedStringTableListSizeLimit){
            //TODO: Write code to merge the SS tables
            log.info("Merging SS Tables");
        }
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
