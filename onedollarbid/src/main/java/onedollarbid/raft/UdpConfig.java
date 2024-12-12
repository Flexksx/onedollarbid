package onedollarbid.raft;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Configuration
public class UdpConfig {

    @Value("${udp.multicast.address}")
    private String MULTICAST_ADDRESS;

    @Value("${udp.multicast.port}")
    private int MULTICAST_PORT;

    @Value("${node.id}")
    private int nodeId;
}