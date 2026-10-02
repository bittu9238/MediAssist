package MediAssist.controller;

import MediAssist.model.User;
import MediAssist.model.ChatMessage;
import MediAssist.model.ChatSession;

import MediAssist.repository.UserRepository;

import MediAssist.service.ChatHistoryService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    private final UserRepository userRepository;


    public ChatHistoryController(
            ChatHistoryService chatHistoryService,
            UserRepository userRepository) {

        this.chatHistoryService =
                chatHistoryService;

        this.userRepository =
                userRepository;
    }


    @PostMapping("/sessions")
    public ChatSession createSession(

            @RequestParam Long userId,

            @RequestParam(
                    defaultValue = "normal"
            )
            String mode) {

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        return chatHistoryService
                .createSession(
                        user,
                        mode
                );
    }


    @GetMapping("/sessions")
    public List<ChatSession> getSessions(

            @RequestParam Long userId,

            @RequestParam(
                    defaultValue = "normal"
            )
            String mode) {

        return chatHistoryService
                .getSessions(
                        userId,
                        mode
                );
    }


    @GetMapping("/sessions/{id}/messages")
    public List<ChatMessage> getMessages(

            @PathVariable Long id,

            @RequestParam Long userId) {

        return chatHistoryService
                .getMessages(
                        id,
                        userId
                );
    }
}