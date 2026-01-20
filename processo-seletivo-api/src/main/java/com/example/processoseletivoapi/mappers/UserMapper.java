package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.models.User;
import com.example.processoseletivoapi.requests.UserRequest;
import com.example.processoseletivoapi.responses.RoleResponse;
import com.example.processoseletivoapi.responses.UserResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User requestToModel(UserRequest request) {
        if (request == null) return null;
        return new User(request.username(), request.password(), request.rolesId().toArray(new Long[0]));
    }

    public UserResponse modelToResponse(User model) {
        if (model == null) return null;
        return new UserResponse(model.getId(), model.getUsername(), model.getPassword(), null);
    }

    public UserResponse modelToResponse(User model, Set<Role> roles) {
        if (model == null) return null;
        Set<RoleResponse> rolesResponse = roles.stream().map(roleMapper::modelToResponse).collect(Collectors.toSet());
        return new UserResponse(model.getId(), model.getUsername(), model.getPassword(), rolesResponse);
    }
}
