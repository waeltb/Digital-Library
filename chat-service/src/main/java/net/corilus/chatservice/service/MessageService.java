package net.corilus.chatservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.corilus.chatservice.model.Message;
import net.corilus.chatservice.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


   /* public List<Message> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }*/

    public List<Message> getMessagesBetweenUsers(String user1, String user2) {
        return messageRepository.findBySenderUsernameAndRecipientUsernameOrRecipientUsernameAndSenderUsernameOrderByCreatedDateTime(
                user1, user2, user1, user2);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

}
