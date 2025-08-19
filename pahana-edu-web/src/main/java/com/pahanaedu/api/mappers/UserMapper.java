package com.pahanaedu.api.mappers;

import com.pahanaedu.api.dto.UserDTO;
import com.pahanaedu.models.User;

public final class UserMapper {
    private UserMapper() {}

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}