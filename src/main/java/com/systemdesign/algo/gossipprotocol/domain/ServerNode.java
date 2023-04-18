package com.systemdesign.algo.gossipprotocol.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Component
@NoArgsConstructor
@Slf4j
public class ServerNode {
    private String serverIp;
    private int serverSize;
    private String serverName;

    private Map<String, ServerNodeState> serverNodesForGossip;

    public ServerNode(String serverIp, int serverSize, String serverName) {
        this.serverIp = serverIp;
        this.serverSize = serverSize;
        this.serverName = serverName;
        this.serverNodesForGossip = new HashMap<>();
    }
    public void setServerNodesForGossip(Map<String, ServerNodeState> serverNodesForGossip){
        this.serverNodesForGossip = serverNodesForGossip;
    }
    public Map<String, ServerNodeState> initiateGossip(ServerNodeState serverNodeState) {
        log.info("Gossip initiated by : " + this.serverIp + "(" + this.serverName + ")");

        Timestamp lastTimestamp;

        //Get a random IP from the available list of servers and if that server's data has not been updated 
        //in the local table of this node, then initiate gossip with this node
        ServerNodeState serverNodeStateToStartGossipWith;
        String randomServerNodeIp = "";
        int count = 0;
        do{
            randomServerNodeIp = getRandomServerIp();
            serverNodeStateToStartGossipWith = this.serverNodesForGossip.get(randomServerNodeIp);
            lastTimestamp = serverNodeStateToStartGossipWith.getLastTimestamp();
            count++;
            log.info("Time diff : "+Math.abs(lastTimestamp.getTime() - System.currentTimeMillis()));
        }while(!randomServerNodeIp.equals(this.serverIp) && Math.abs(lastTimestamp.getTime() - System.currentTimeMillis()) < 500 && count<this.serverNodesForGossip.size());

        //now we have found a node with which we have not initiated any transaction
        if(count<this.serverNodesForGossip.size()){
            log.info("Initiating Gossip by "+this.serverName+"("+this.serverIp+") with "+serverNodeStateToStartGossipWith.getServerNode().getServerName()+"("+serverNodeStateToStartGossipWith.getServerNode().getServerIp()+")");
            Map<String, ServerNodeState> recievedUpdatedGossipNodeState = serverNodeStateToStartGossipWith.getServerNode().initiateGossip(new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), this));
            updateServerNodesForGossip(recievedUpdatedGossipNodeState);

        }
        log.info("Updated server node list");
        print(this.serverNodesForGossip);
        return this.serverNodesForGossip;
    }

    private String getRandomServerIp() {
        List<ServerNodeState> valuesList = new ArrayList<>(this.serverNodesForGossip.values());
        int randomIndex = new Random().nextInt(valuesList.size());
        return valuesList.get(randomIndex).getServerNode().getServerIp();
    }

    private void print(Map<String, ServerNodeState> serverNodesForGossip) {
        for(Map.Entry<String,ServerNodeState> entry: serverNodesForGossip.entrySet()){
            log.info("Key: "+entry.getKey()+" Value: "+entry.getValue());
        }
    }

    private void updateServerNodesForGossip(Map<String, ServerNodeState> recievedUpdatedGossipNodeState) {
        for(Map.Entry<String, ServerNodeState> entry : recievedUpdatedGossipNodeState.entrySet()){
            ServerNodeState currentServerNodeState = this.serverNodesForGossip.getOrDefault(entry.getKey(),null);
            if(currentServerNodeState != null){
                if(currentServerNodeState.getLastTimestamp().compareTo(entry.getValue().getLastTimestamp())<0){
                    this.serverNodesForGossip.put(currentServerNodeState.getServerNode().getServerIp(),entry.getValue());
                }
            }
        }
    }

    // public ServerNodeState getServerNodeState() {
    //     return ServerNodeState.builder()
    //             .status("UP")
    //             .lastTimestamp(new Timestamp(System.currentTimeMillis()))
    //             .build();
    // }

    @Override
    public String toString(){
        return "Ip: "+this.serverIp+" Size: "+this.serverSize+" Name: "+this.serverName;
    }
}
