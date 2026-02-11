package org.chatapp.chatappserver.conversation.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDmRequest (@NotBlank Long otherUserId) {}
