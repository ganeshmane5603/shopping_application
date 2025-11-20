package com.example.shopping_application.controller;

import com.example.shopping_application.dto.UserPatchRequest;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user=userService.getOneUser(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable Long id){
        User updatedUser=userService.updateUserById(id,user);
        return ResponseEntity.ok().body(updatedUser);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUserSpecificField(@RequestBody User user,@PathVariable Long id){
        User updatedUser=userService.updateUserSpecificField(user,id);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<User> patchUser(@RequestBody UserPatchRequest userPatchRequest,@PathVariable Long id){
       User user= userService.patchUser(id,userPatchRequest);
       return ResponseEntity.ok().body(user);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<User>> sortedUser(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "5") int pageSize,
                                                 @RequestParam(defaultValue = "name") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String sortOrder){

        List<User> users=userService.sortedUser(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok().body(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
