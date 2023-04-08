package com.systemdesign.algo.application.backgroundprocess;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.application.storage.InMemoryStorageService;
import com.systemdesign.algo.application.storage.exception.DataTransferException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferInMemoryStorageToDiskService {
    private final InMemoryStorageService inMemoryStorageService;

    @Job(name="Job for transferring data from memory to disk", retries=2)
    @Recurring(id = "transfer-memory-data-to-disk-recurring-job", cron = "*/5 * * * *")
    public void transferInMemoryDataToDisk() throws DataTransferException{
        log.info("Transferring data from memory to disk");
        this.inMemoryStorageService.transferMemTableToDisk();
    }
}
