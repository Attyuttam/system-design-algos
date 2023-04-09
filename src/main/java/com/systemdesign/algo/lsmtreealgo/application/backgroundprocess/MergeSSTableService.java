package com.systemdesign.algo.lsmtreealgo.application.backgroundprocess;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.lsmtreealgo.application.storage.DiskStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MergeSSTableService {
    private final DiskStorageService diskStorageService;

    @Job(name="Job for merging SS Tables", retries=2)
    @Recurring(id = "merge-ss-tables-recurring-job", cron = "*/10 * * * *")
    public void mergeSSTables(){
        log.info("Merging SS Tables");
        diskStorageService.mergeSSTables();
    }

}
