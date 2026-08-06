package org.solen.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.solen.business.usercases.ICreateUserUseCase;
import org.solen.business.usercases.IDeleteUserUseCase;
import org.solen.business.usercases.IGetUserByIdUseCase;
import org.solen.business.usercases.IPromoteToAdminUseCase;
import org.solen.business.usercases.IUpdateUserUseCase;
import org.solen.controller.dto.user.CreateUserRequest;
import org.solen.controller.dto.user.UpdateUserRequest;
import org.solen.controller.dto.user.UserDto;
import org.solen.controller.mappers.UserMapper;
import org.solen.domain.users.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private ICreateUserUseCase createUserUseCase;
    private IDeleteUserUseCase deleteUserUseCase;
    private IGetUserByIdUseCase getUserByIdUseCase;
    private IUpdateUserUseCase updateUserUseCase;
    private IPromoteToAdminUseCase promoteToAdminUseCase;
    private UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = createUserUseCase.createUser(request);
        return ResponseEntity.ok(mapper.convertToDto(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = getUserByIdUseCase.getUserById(id);
        return ResponseEntity.ok(mapper.convertToDto(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#id, authentication.name)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User user = updateUserUseCase.updateUser(request, id);
        return ResponseEntity.ok(mapper.convertToDto(user));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("@categorySecurity.isAdminByEmail(authentication.name)")
    public ResponseEntity<UserDto> promoteToAdmin(@PathVariable Long id) {
        User user = promoteToAdminUseCase.promote(id);
        return ResponseEntity.ok(mapper.convertToDto(user));
    }
}