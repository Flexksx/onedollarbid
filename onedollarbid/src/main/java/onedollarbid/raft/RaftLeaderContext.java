package onedollarbid.raft;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RaftLeaderContext {
    private int node;
    private String host;
    private int port;

    RaftLeaderContext(int node, String host, int port) {
        this.node = node;
        this.host = host;
        this.port = port;
    }
}
