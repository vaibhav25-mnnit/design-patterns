package Implementation;

public enum EnumSingleton {
    INSTANCE("localhost",4000);

    private String host;
    private int port;


    EnumSingleton(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // Public method
    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    /*

        ->Don't use this setter methods as below they are not thread safe
     */
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
