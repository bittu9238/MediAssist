package MediAssist.repository;

import MediAssist.model.ChatSession;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository
        extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findByUserIdAndModeOrderByUpdatedAtDesc(
        Long userId,
        String mode
);

Optional<ChatSession> findByIdAndUserId(
        Long id,
        Long userId
);

}