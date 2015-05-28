package resource;


import java.io.Serializable;

public class ThreadSettings implements Serializable{
    private static int clientSleepTime; // = 25;
    private static int serviceSleepTime; //= 100;

    public ThreadSettings() {
        this.clientSleepTime = 25;
        this.serviceSleepTime = 100;
    }


    public int getClientSleepTime() {
        return clientSleepTime;
    }

    public int getServiceSleepTime() {
        return serviceSleepTime;
    }
}
