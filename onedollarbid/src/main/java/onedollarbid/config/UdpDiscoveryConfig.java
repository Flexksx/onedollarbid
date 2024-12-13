package onedollarbid.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import onedollarbid.raft.RaftLeaderElectionService;

@Configuration
@RequiredArgsConstructor
public class UdpDiscoveryConfig {
    private final RaftLeaderElectionService raftLeaderElectionService;

    @PostConstruct
    public void initUdpDiscovery() {
        raftLeaderElectionService.init();
    }
}