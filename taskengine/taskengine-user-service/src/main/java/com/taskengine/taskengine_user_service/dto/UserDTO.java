package com.taskengine.taskengine_user_service.dto;

import com.taskengine.taskengine_user_service.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

import java.time.LocalDateTime;

public record UserDTO(

        Long id,
        String userName,
        String email,
        String mobileNumber,
        String alternativeNumber,
        Role role,
        Boolean isActive
) {}
