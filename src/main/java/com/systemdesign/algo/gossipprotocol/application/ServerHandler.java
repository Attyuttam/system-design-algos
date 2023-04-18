package com.systemdesign.algo.gossipprotocol.application;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.systemdesign.algo.gossipprotocol.domain.ServerNode;
import com.systemdesign.algo.gossipprotocol.domain.ServerNodeState;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ServerHandler {

    public static List<ServerNodeState> getAvailableServers(){
        List<ServerNodeState> serverNodeStates = Arrays.asList(
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.200", 2000, "Server_A")),
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.300", 3000, "Server_B")),
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.400", 2000, "Server_C")),
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.500", 5000, "Server_D")),
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.600", 2000, "Server_E")),
            new ServerNodeState("UP", new Timestamp(System.currentTimeMillis()), new ServerNode("192.90.100.700", 7000, "Server_F"))
        );
        Map<String, ServerNodeState> serverIpToStateMap = new HashMap<>();
        serverIpToStateMap.put("192.90.100.200", serverNodeStates.get(0));
        serverIpToStateMap.put("192.90.100.300", serverNodeStates.get(1));
        serverIpToStateMap.put("192.90.100.400", serverNodeStates.get(2));
        serverIpToStateMap.put("192.90.100.500", serverNodeStates.get(3));
        serverIpToStateMap.put("192.90.100.600", serverNodeStates.get(4));
        serverIpToStateMap.put("192.90.100.700", serverNodeStates.get(5));

        serverNodeStates.forEach(serverNodeState -> {
            serverNodeState.getServerNode().setServerNodesForGossip(serverIpToStateMap);
        });
        
        return serverNodeStates;
    }
}
