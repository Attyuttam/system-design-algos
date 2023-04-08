package com.systemdesign.algo.infra.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systemdesign.algo.application.storage.InMemoryStorageService;
import com.systemdesign.algo.application.storage.exception.DataDeleteFromStorageException;
import com.systemdesign.algo.application.storage.exception.DataInsertIntoStorageException;
import com.systemdesign.algo.domain.lsm.memtable.MemTableData;
import com.systemdesign.algo.domain.lsm.sstable.SortedStringTable;
import com.systemdesign.algo.domain.tree.Tree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/storage")
public class StorageController {
    private final InMemoryStorageService inMemoryStorageService;

    @PostMapping(path = "/add-data", consumes = "application/json")
    public void addData(@RequestBody MemTableData memTableData) throws DataInsertIntoStorageException{
        log.info("MemTableData: ",memTableData);
        Tree<MemTableData> result = inMemoryStorageService.insertDataIntoMemory(memTableData);
        result.traverse();
    }

    @PostMapping(path = "/add-multiple-data", consumes = "application/json")
    public void addMultipleData(@RequestBody List<MemTableData> memTableData){
        log.info("MemTableData: ",memTableData);
        memTableData.forEach(mmt -> {
            Tree<MemTableData> result;
            try {
                result = inMemoryStorageService.insertDataIntoMemory(mmt);
                result.traverse();
            } catch (DataInsertIntoStorageException e) {
                log.info("Data Storage Exception");
                e.printStackTrace();
            }
            
        });
    }

    @GetMapping("/delete-data-by-key/{key}")
    public void deleteData(@PathVariable String key) throws DataDeleteFromStorageException{
        inMemoryStorageService.deleteDataByKey(key);
    }

    @GetMapping("/view-all-in-memory-storage")
    public List<MemTableData> viewMemTable(){
        return inMemoryStorageService.viewInMemoryStorage();
    }

    @GetMapping("/view-all-disk-storage")
    public List<SortedStringTable> viewDiskStorage(){
        return inMemoryStorageService.viewDiskStorage();
    }

    @GetMapping("/find-data-by-key/{key}")
    public MemTableData findDataByKey(@PathVariable String key){
        return inMemoryStorageService.findDataByKey(key);
    }
}
