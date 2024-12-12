package onedollarbid.raft;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UdpConfig {

    @Value("${udp.multicast.address}")
    private String MULTICAST_ADDRESS;

    @Value("${udp.multicast.port}")
    private int MULTICAST_PORT;

    @Value("${node.id}")
    private int nodeId;

    public String getMULTICAST_ADDRESS() {
        return MULTICAST_ADDRESS;
    }

    public void setMULTICAST_ADDRESS(String mULTICAST_ADDRESS) {
        MULTICAST_ADDRESS = mULTICAST_ADDRESS;
    }

    public int getMULTICAST_PORT() {
        return MULTICAST_PORT;
    }

    public void setMULTICAST_PORT(int mULTICAST_PORT) {
        MULTICAST_PORT = mULTICAST_PORT;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}