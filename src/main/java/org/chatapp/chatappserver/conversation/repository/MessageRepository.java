package org.chatapp.chatappserver.conversation.repository;

import org.chatapp.chatappserver.conversation.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
