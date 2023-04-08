package com.systemdesign.algo.domain.lsm.sstable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.systemdesign.algo.domain.lsm.memtable.MemTable;
import com.systemdesign.algo.domain.lsm.memtable.MemTableData;

import lombok.Data;

@Component
@Data
public class SortedStringTable {
    private final MemTable memTable;
    private List<MemTableData> sparseIndex; 

    public SortedStringTable(MemTable memTable){
        this.memTable = memTable;
        this.sparseIndex = new ArrayList<>();
        if(memTable!=null && this.memTable.size()>0)
            populateSparseIndex();
    }

    private void populateSparseIndex() {
        //As of now, I am just saving the min and max as sparse indices
        sparseIndex.add(this.memTable.getMin().getData());
        sparseIndex.add(this.memTable.getMax().getData());
    } 

    public MemTableData getData(MemTableData searchMemTableData){
        //Use sparse index to check if the data is even present in this SSTable

        if(this.sparseIndex.get(0).getKey().compareTo(searchMemTableData.getKey()) <= 0 && this.sparseIndex.get(1).getKey().compareTo(searchMemTableData.getKey()) >= 0){
            return this.memTable.search(searchMemTableData).getData();
        }
        return null;
    }

    public boolean delete(String key) {
        if(this.sparseIndex.get(0).getKey().compareTo(key) <= 0 && this.sparseIndex.get(1).getKey().compareTo(key) >= 0){
            return this.memTable.delete(new MemTableData(key, null));
        }
        return false;
    }
}
