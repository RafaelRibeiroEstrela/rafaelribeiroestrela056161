package com.example.processoseletivoapi.mappers;

import com.example.processoseletivoapi.models.Role;
import com.example.processoseletivoapi.requests.RoleRequest;
import com.example.processoseletivoapi.responses.RoleResponse;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role requestToModel(RoleRequest request) {
        if (request == null) return null;
        return new Role(request.authority());
    }

    public RoleResponse modelToResponse(Role model) {
        if (model == null) return null;
        return new RoleResponse(model.getId(), model.getAuthority());
    }

}
