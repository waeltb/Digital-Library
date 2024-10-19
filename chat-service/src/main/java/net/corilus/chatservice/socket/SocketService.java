package net.corilus.chatservice.socket;

import com.corundumstudio.socketio.SocketIOClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.corilus.chatservice.model.Message;
import net.corilus.chatservice.model.MessageType;
import net.corilus.chatservice.service.MessageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final MessageService messageService;

    @PostConstruct
    public void init() {
        // Register this service with the service locator
        ServiceLocator.setSocketService(this);
    }

    // Send a message to a specific recipient if they are connected
    public void sendSocketMessage(SocketIOClient senderClient, Message message) {
        System.out.println("display message into the socketService "+message);
        String recipientUsername = message.getRecipientUsername();
        System.out.println("testing into the socketService "+recipientUsername);
        SocketIOClient recipientClient = ServiceLocator.getSocketModule().getClientByUsername(recipientUsername);
        //if (recipientClient != null && !recipientClient.getSessionId().equals(senderClient.getSessionId())) {
            recipientClient.sendEvent("receive_message", message);
        //}
    }

    // Save the client message and send it to the intended recipient
    public void saveMessage(SocketIOClient senderClient, Message message) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.CLIENT)
                .content(message.getContent())
                .senderUsername(message.getSenderUsername())
                .recipientUsername(message.getRecipientUsername())
                .build());
        sendSocketMessage(senderClient, storedMessage);
    }

    // Save an informational server message and send it to the client
    public void saveInfoMessage(SocketIOClient senderClient, String message, String recipientUsername) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.SERVER)
                .content(message)
                .senderUsername("Server")
                .recipientUsername(recipientUsername)
                .build());
        sendSocketMessage(senderClient, storedMessage);
    }
}
