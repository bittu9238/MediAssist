package MediAssist.service;

import MediAssist.model.ChatMessage;
import MediAssist.model.User;
import MediAssist.model.ChatSession;
import MediAssist.repository.ChatMessageRepository;
import MediAssist.repository.ChatSessionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {

    private final ChatSessionRepository sessionRepository;
    private final ChatMessageRepository messageRepository;

    public ChatHistoryService(
            ChatSessionRepository sessionRepository,
            ChatMessageRepository messageRepository) {

        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
    }

    public ChatSession createSession(
        User user,
        String mode) {

    String title =
            getModeTitle(mode);

    ChatSession session =
            new ChatSession(
                    user,
                    mode,
                    title
            );

    return sessionRepository.save(session);
}

    public List<ChatSession> getSessions(
        Long userId,
        String mode) {

    return sessionRepository
            .findByUserIdAndModeOrderByUpdatedAtDesc(
                    userId,
                    mode
            );
    }

    public List<ChatMessage> getMessages(
        Long sessionId,
        Long userId) {

    ChatSession session =
            sessionRepository
                    .findByIdAndUserId(
                            sessionId,
                            userId
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Chat session not found"
                            )
                    );

    return messageRepository
            .findBySessionIdOrderByCreatedAtAsc(
                    session.getId()
            );
}


    public ChatMessage saveMessage(
        Long sessionId,
        String sender,
        String message) {

    ChatSession session =
            sessionRepository
                    .findById(sessionId)
                    .orElseThrow();

    // Use the user's first message as the chat title
    if (sender.equals("user")
            && session.getTitle().contains("Medical Chat")) {

        String title = message.trim();

        if (title.length() > 35) {
            title = title.substring(0, 35) + "...";
        }

        session.setTitle(title);
    }

    ChatMessage chatMessage =
            new ChatMessage(
                    session,
                    sender,
                    message
            );

    session.setUpdatedAt(
            java.time.LocalDateTime.now()
    );

    sessionRepository.save(session);

    return messageRepository.save(chatMessage);
    }



    private String getModeTitle(String mode) {

        return switch (mode) {

            case "symptoms" ->
                    "🩺 Symptom Checker";

            case "medicine" ->
                    "💊 Medicine Information";

            case "appointment" ->
                    "📅 Appointment Assistance";

            case "health" ->
                    "❤️ Health Information";

            default ->
                    "🏥 Medical Chat";
        };
    }

    public ChatSession getSessionForUser(
        Long sessionId,
        Long userId) {

    return sessionRepository
            .findByIdAndUserId(
                    sessionId,
                    userId
            )
            .orElseThrow(() ->
                    new RuntimeException(
                            "Chat session not found"
                    )
            );
}
}
