package com.systemdesign.algo.gossipprotocol.domain;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Getter
public class ServerNodeState {
    public String status;
    public Timestamp lastTimestamp;
    public ServerNode serverNode;

    public ServerNodeState(String status, Timestamp timestamp, ServerNode serverNode){
        this.status = status;
        this.lastTimestamp = timestamp;
        this.serverNode = serverNode;
    }

    @Override
    public String toString(){
        return "Status: "+this.status+" Last Timestamp: "+this.lastTimestamp+" Server Node: "+this.serverNode;
    }
}
