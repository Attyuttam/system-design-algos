package com.systemdesign.algo.gossipprotocol.application;

import java.util.List;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.stereotype.Component;

import com.systemdesign.algo.gossipprotocol.domain.ServerNodeState;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GossipHandler {

    @Job(name="Job for initiating Gossip", retries=2)
    @Recurring(id = "gossip-initiation-recurring-job", cron = "*/2 * * * *")
    public void initiateGossip(){
        log.info("Initiating Gossip Protocol");
        List<ServerNodeState> availableServers = ServerHandler.getAvailableServers();
        int randomIndex = getRandomNumber(0,availableServers.size()-1);

        //server with index randomIndex is initiated for gossip with another random server node data
        availableServers.get(randomIndex).getServerNode().initiateGossip(getSeedNode(availableServers, randomIndex));
    }

    private ServerNodeState getSeedNode(List<ServerNodeState> availableServers, int callingServerIndex) {
        int seedNodeIndex = -1;
        do{
            seedNodeIndex = getRandomNumber(0, availableServers.size()-1);
        }while(seedNodeIndex == callingServerIndex);
        return availableServers.get(seedNodeIndex);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
