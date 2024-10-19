package net.corilus.chatservice.socket;

import org.springframework.stereotype.Component;

@Component
public class ServiceLocator {

    private static SocketService socketService;
    private static SocketModule socketModule;

    public static void setSocketService(SocketService socketService) {
        ServiceLocator.socketService = socketService;
    }

    public static void setSocketModule(SocketModule socketModule) {
        ServiceLocator.socketModule = socketModule;
    }

    public static SocketService getSocketService() {
        return socketService;
    }

    public static SocketModule getSocketModule() {
        return socketModule;
    }
}
