package net.corilus.chatservice.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import net.corilus.chatservice.constants.Constants;
import net.corilus.chatservice.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component

public class SocketModule {

    private final SocketIOServer server;
    private static final Logger log = LoggerFactory.getLogger(SocketModule.class);

    // Store connected users: sessionId -> username
    private final Map<String, String> connectedUsers = new ConcurrentHashMap<>();
    // Store user sessions: username -> sessionId
    private final Map<String, String> userSessions = new ConcurrentHashMap<>();
    // Store clients by username
    private final Map<String, SocketIOClient> clientsByUsername = new ConcurrentHashMap<>();

    public SocketModule(SocketIOServer server) {
        this.server = server;

        ServiceLocator.setSocketModule(this);
        server.addConnectListener(onConnected());
       server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, message, ackSender) -> {
            log.info("Message received from {}: {}", senderClient, message);
            ServiceLocator.getSocketService().saveMessage(senderClient, message);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            System.out.println("test the method in socketModule onConnected into");

            var params = client.getHandshakeData().getUrlParams();

            // Check if the "username" key exists and the value is not null
            if (params != null && params.get("username") != null) {
                // Extract the username from the parameters
                String username = params.get("username").stream().collect(Collectors.joining());
                log.info("Socket ID[{}] - username [{}] Connected to chat module", username);

                userSessions.put(username, client.getSessionId().toString());
                connectedUsers.put(client.getSessionId().toString(), username);
                clientsByUsername.put(username, client);

                // Save a welcome message using the socket service
                ServiceLocator.getSocketService().saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), username);
                log.info("Socket ID[{}] - username [{}] Connected to chat module", client.getSessionId(), username);

                // Emit the list of connected users
                emitConnectedUsers();
            } else {
                // Handle case where "username" is missing or null
                log.error("Handshake data does not contain 'username'. Socket ID: {}", client.getSessionId());
            }
        };
    }


    private DisconnectListener onDisconnected() {
        return client -> {
            String username = connectedUsers.remove(client.getSessionId().toString());
            if (username != null) {
                userSessions.remove(username);
                clientsByUsername.remove(username);
                ServiceLocator.getSocketService().saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), username);
                log.info("Socket ID[{}] - username [{}] Disconnected from chat module", client.getSessionId(), username);

                emitConnectedUsers();
            }
        };
    }

    private void emitConnectedUsers() {
        List<String> connectedUsernames = getConnectedUsernames();
        server.getBroadcastOperations().sendEvent("update_connected_users", connectedUsernames);
        System.out.println("testing the username");
        connectedUsernames.forEach(System.out::println);
    }

    public List<String> getConnectedUsernames() {
        return connectedUsers.values().stream().collect(Collectors.toList());
    }

    public SocketIOClient getClientByUsername(String username) {
        return clientsByUsername.get(username);
    }
}
