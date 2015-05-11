package resource;

import java.io.Serializable;

public class ServerSettings implements Serializable {
    private int port;

    public ServerSettings() {
        this.port = 5000;
    }

    public int getPort() {
        return port;
    }
}
