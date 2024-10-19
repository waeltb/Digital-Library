package net.corilus.chatservice.repository;

import net.corilus.chatservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    //List<Message> findAllByRoom(String room);
    // Fetch messages between two users
    List<Message> findBySenderUsernameAndRecipientUsernameOrRecipientUsernameAndSenderUsernameOrderByCreatedDateTime(
            String sender1, String receiver1, String receiver2, String sender2);

}
