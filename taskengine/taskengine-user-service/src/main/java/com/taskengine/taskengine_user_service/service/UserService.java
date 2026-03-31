package com.taskengine.taskengine_user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskengine.taskengine_user_service.dto.*;
import com.taskengine.taskengine_user_service.exception.UserAlreadyExist;
import com.taskengine.taskengine_user_service.exception.UserNotFoundException;
import com.taskengine.taskengine_user_service.model.User;
import com.taskengine.taskengine_user_service.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    public ResponseEntity<ResponseDTO<Page<UserDTO>>> getUsers(int size, int page) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("userName").ascending());
        Page<User> userPage = userRepo.findAll(pageable);
        Page<UserDTO> dtoPage = userPage.map(user ->
                new UserDTO(
                        user.getId(),
                        user.getUserName(),
                        user.getEmail(),
                        user.getMobileNumber(),
                        user.getAlternativeNumber(),
                        user.getRole(),
                        user.getActive()
                ));

        if (dtoPage.isEmpty()) {
            throw new UserNotFoundException("user not found with this id:::" + dtoPage.get().map(UserDTO::id));
        }
        ResponseDTO<Page<UserDTO>> responseDTO = new ResponseDTO<>(HttpStatus.OK, dtoPage);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<ResponseDTO<User>> addUser(User user) {

        if (userRepo.findByUserName(user.getUserName()).isPresent()) {
            throw new UserAlreadyExist("user Already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ResponseDTO<User> responseDTO = new ResponseDTO<>(
                HttpStatus.OK,
                userRepo.save(user) // User
        );
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<ResponseDTO<String>> addBulkUser(List<User> userList) {

      Set<String> distinctEmail=new HashSet<>();
      //Remove Duplicate Entries
      List<User> uniqueUser =userList.stream()
              .filter(user -> distinctEmail.add(user.getEmail()))
              .toList();
      // gather emails from unique user
      List<String> emails=uniqueUser.stream()
              .map(User::getEmail)
              .toList();
      // gather existing email
      Set<String> existingEmails=userRepo.findByEmailIn(emails).stream().
              map(User::getEmail)
              .collect(Collectors.toSet());
      //Remove existing emails
      List<User> userToSave=uniqueUser.stream()
              .filter(user -> !existingEmails.contains(user.getEmail()))
              .peek(user -> {
                  if(user.getPassword()!=null){
                      user.setPassword(passwordEncoder.encode(user.getPassword()));}
              })
              .toList();

      if(userToSave.isEmpty()){
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO<>("Existing Emails: "+existingEmails));
      }else {
          userRepo.saveAll(userToSave);
          return  ResponseEntity.ok(new ResponseDTO<>("Successfully email inserted..." +"Skipped UserEmails:"+ existingEmails));
      }
    }

    public ResponseEntity<ResponseDTO<UserDTO>> updateAllUserdetails(Long id, UserDTO userDTO) {

        User userDetail=userRepo.findById(id).orElseThrow(() ->new UserNotFoundException("User not found in this id:::"+id));

        if(!userDetail.getActive()){
            throw new RuntimeException("User is not active"+userDetail.getUserName());
        }
        userDetail.setUserName(userDTO.userName());
        userDetail.setEmail(userDTO.email());
        userDetail.setMobileNumber(userDTO.mobileNumber());
        userDetail.setAlternativeNumber(userDTO.alternativeNumber());
        userDetail.setActive(userDTO.isActive());
        userRepo.save(userDetail);

        ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>(HttpStatus.OK,userDTO);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<ResponseDTO<Map<String,Object>>> updatePartialUpdate(Long id, Map<String,Object> fields)  {
        User existingUser=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User not found with this id"+id));
        if(!existingUser.getActive()){
            throw new RuntimeException("User is inactive"+existingUser.getUserName());
        }

        fields.forEach((key,value)->{
            Field field= ReflectionUtils.findField(User.class,key);
            if(field!=null){
                field.setAccessible(true);
                    if(field.getType().isEnum()){
                        @SuppressWarnings("unchecked")
                        Class<? extends Enum> enumClass=(Class<? extends Enum>)field.getType();
                        try{
                            Object enumValue=Enum.valueOf(enumClass,value.toString().toUpperCase());
                            ReflectionUtils.setField(field,existingUser,enumValue);
                        }catch (IllegalArgumentException e){
                                throw new IllegalArgumentException("Invalid value:"+value+"for enum field"+field.getType());
                        }
                    }
                    else{ ReflectionUtils.setField(field,existingUser,value);}
            }
        });
//        User updatedUser=objectMapper.updateValue(existingUser,fields);
              userRepo.save(existingUser);
        ResponseDTO<Map<String,Object>> responseDTO=new ResponseDTO<>(HttpStatus.OK,fields);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseDTO<UserDTO> getUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("user id not found:::"+id));

        UserDTO responseUserDto=new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getAlternativeNumber(),
                user.getRole(),
                user.getActive()
        );

        return  new ResponseDTO<>(HttpStatus.OK,responseUserDto);
       // return  ResponseEntity.ok(new ResponseDTO<>(user));
    }

    public ResponseEntity<ResponseDTO<String>> deleteUser(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("user id not found:::" +id));
        userRepo.deleteById(id);
        return ResponseEntity.ok(new ResponseDTO<>("user has been deleted successfully Email: "+user.getEmail()));
    }


    public ResponseEntity<ResponseDTO<User>> getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user Email is not Found :"+email));
        return ResponseEntity.ok(new ResponseDTO<>(user));
    }



    @Transactional
    public ResponseEntity<ResponseDTO<String>> setActivation(Long id, ActivationDTO dto) {
        User user =userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User ID not found User_id: "+id));
        user.setActive(dto.active()); // ✅ explicit update
        String message =Boolean.TRUE.equals(dto.active()) ? "User has been Activated" : "User has been Deactivated";
        return ResponseEntity.ok(new ResponseDTO<>(message));
    }

    @Transactional
    public ResponseEntity<ResponseDTO<String>> changePassword(Long id, changePasswordDTO PasswordSetDTO) {
        User user =userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("User ID not found User_id: "+id));

        String existingEncodedPassword=user.getPassword();

        if(!passwordEncoder.matches(PasswordSetDTO.oldPassword(),existingEncodedPassword)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>("PassWord is not matching, Kindly enter correct password"));
        } else if (passwordEncoder.matches(PasswordSetDTO.newPassword(),existingEncodedPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>("Existing and New Password should not be same kindly use different password"));
        } else if (user.getUpdatedAt().isAfter(LocalDateTime.now().minusDays(1))) {
            long elapsedHours = Duration.between(user.getUpdatedAt(),LocalDateTime.now()).toHours();
            long remainingHours=Math.max(0,24-elapsedHours);
            return ResponseEntity.badRequest().body(new ResponseDTO<>("Password was changed within the last 24 hours. Please try again after "+ remainingHours+" hour(s)."));
        }

        user.setPassword(passwordEncoder.encode(PasswordSetDTO.newPassword()));
            return ResponseEntity.ok(new ResponseDTO<>("Password has been updated successfully"));
    }

    public ResponseDTO<UserDTO> getUserByEmailDto(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("user Email is not Found :"+email));
        UserDTO responseUserDto=new UserDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getAlternativeNumber(),
                user.getRole(),
                user.getActive()
        );

        return new ResponseDTO<>(HttpStatus.OK,responseUserDto);
    }



    public ResponseDTO<List<IsValidUser>> validateUser(List<Long> ids) {

        List<User> users = userRepo.findAllById(ids);
        Map<Long,User> userMap =users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity(),
                        (a,b)->a
        ));
        List<Long> uniqueIds=ids.stream().distinct().toList();

        List<IsValidUser> response=uniqueIds.stream().map(
                id->{
                    User user=userMap.get(id);
                   if (user!=null && Boolean.TRUE.equals(user.getActive())){
                       return new IsValidUser(
                               id,
                               user.getEmail(),
                               user.getActive(),
                               true);
                   }else if (user!=null && Boolean.FALSE.equals(user.getActive())){
                       return new IsValidUser(
                               id,
                               user.getEmail(),
                               user.getActive(),
                               false);
                   }else
                       return new IsValidUser(id,null,null,false);

                }
        ).toList();

        return new ResponseDTO<>(HttpStatus.OK,response);
    }


}
