package net.corilus.chatservice.controller;


import lombok.RequiredArgsConstructor;
import net.corilus.chatservice.model.Message;
import net.corilus.chatservice.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/message")
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;



    @GetMapping("/chat/messages")
    public List<Message> getMessages(@RequestParam String sender, @RequestParam String receiver) {
        System.out.println(sender+" "+receiver);
        return messageService.getMessagesBetweenUsers(sender, receiver);


    }
}