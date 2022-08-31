package Staj;

import java.net.InetAddress;

public class ServerClient {
    public InetAddress clientAddress;
    public int port;
    public String username;
    public ServerClient(InetAddress clientAddress, int port, String username) {
        this.clientAddress = clientAddress;
        this.port = port;
        this.username = username;
    }
}
