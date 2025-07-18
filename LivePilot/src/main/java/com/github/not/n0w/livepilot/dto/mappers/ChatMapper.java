package com.github.not.n0w.livepilot.dto.mappers;


import com.github.not.n0w.livepilot.dto.ChatCompletionRequest;
import com.github.not.n0w.livepilot.dto.Message;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.SavedMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target = "messages", source = "messages")
    @Mapping(target = "model", constant = "gpt-4")
    ChatCompletionRequest toDto(Chat chat);

    @Mapping(target = "content", source = "message")
    @Mapping(target = "role", source = "role")
    Message toDto(SavedMessage message);

    List<Message> toDto(List<SavedMessage> messages);
}
