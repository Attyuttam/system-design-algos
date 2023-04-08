package com.systemdesign.algo.domain.lsm.memtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemTableData implements Comparable<MemTableData> {
    private String key;
    private Object content;

    public MemTableData(String key, Object content) {
        this.key = key;
        this.content = content;
    }

    @Override
    public int compareTo(MemTableData lsmt) {
        return this.key.compareTo(lsmt.key);
    }
}
