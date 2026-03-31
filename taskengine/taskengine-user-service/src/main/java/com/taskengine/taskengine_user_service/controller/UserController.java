package com.taskengine.taskengine_user_service.controller;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.taskengine.taskengine_user_service.dto.*;
import com.taskengine.taskengine_user_service.model.Role;
import com.taskengine.taskengine_user_service.model.User;
import com.taskengine.taskengine_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/api/v1")
public class UserController {

    @Autowired
   private UserService userService;

    @GetMapping("/user/all/get")
    public ResponseEntity<ResponseDTO<Page<UserDTO>>> getUsers(@RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "0") int page){
        return userService.getUsers(size,page);
    }

    @GetMapping("/get/{email}")
    public ResponseDTO<UserDTO> getUserByEmailDtoResponse(@PathVariable("email") String email) {
        return userService.getUserByEmailDto(email);
    }

    @GetMapping("/user/get/id/{id}")
    public ResponseDTO<UserDTO> getUserById(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PostMapping("/user/register")
    public ResponseEntity<ResponseDTO<User>> addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/user/bulk/add")
    public  ResponseEntity<ResponseDTO<String>> addBulkUser(@RequestBody List<User> userList){
        return userService.addBulkUser(userList);
    }

    @PutMapping("/user/update/all/id/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> updateAllUserDetails(@PathVariable Long id,@RequestBody UserDTO userDTO){
        return userService.updateAllUserdetails(id,userDTO);
    }

    @PatchMapping("/user/update/id/{id}")
    public  ResponseEntity<ResponseDTO<Map<String,Object>>> updatePartialDetails(@PathVariable Long id,@RequestBody Map<String ,Object> fileds) throws JsonMappingException {
        return userService.updatePartialUpdate(id,fileds);
    }


    @DeleteMapping("/user/delete/id/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteUserById(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/user/get/email/{email}")
    public ResponseEntity<ResponseDTO<User>> getUserByEmail(@PathVariable String email){
        return  userService.getUserByEmail(email);
    };

    @PatchMapping("/user/{id}/activation")
    public  ResponseEntity<ResponseDTO<String>> setActivationUser(@PathVariable Long id,@RequestBody ActivationDTO active){
        return userService.setActivation(id,active);
    }

    @PatchMapping("/user/id/{id}/change")
    public  ResponseEntity<ResponseDTO<String>> changePassword(@PathVariable Long  id,@RequestBody changePasswordDTO newPasswordDTO){
        return userService.changePassword(id,newPasswordDTO);
    }

    @PostMapping("/validate-user")
    public ResponseDTO<List<IsValidUser>> validateUserById(@RequestBody List<Long> ids){
        return userService.validateUser(ids);
    }


}
