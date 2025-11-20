package com.example.shopping_application.service;

import com.example.shopping_application.dto.UserPatchRequest;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.SocketHandler;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOneUser(Long id){
        if (!userRepository.existsById(id)){
            throw new NoSuchElementException("User not found");
        }
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }

    public List<User> getAllUsers(){
        if (userRepository.findAll().isEmpty()){
            throw new NoSuchElementException("User not found");
        }
        return userRepository.findAll();
    }

    public User updateUserById(Long id, User user){
        User existingUser=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        existingUser.setName(user.getName());
        existingUser.setMobile(user.getMobile());
        existingUser.setAddress(user.getAddress());
        existingUser.setGender(user.getGender());

        return userRepository.save(existingUser);
    }

    public User updateUserSpecificField(User user,Long id){
        User updatedUser=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));

        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setMobile(user.getMobile());
        updatedUser.setAddress(user.getAddress());
        updatedUser.setGender(user.getGender());

        return userRepository.save(updatedUser);
    }
    public User patchUser(Long id, UserPatchRequest user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getName() != null) existing.setName(user.getName());
        if (user.getEmail() != null) existing.setEmail(user.getEmail());
        if (user.getMobile() != null) existing.setMobile(user.getMobile());
        if (user.getAddress() != null) existing.setAddress(user.getAddress());
        if (user.getGender() != null) existing.setGender(user.getGender());

        return userRepository.save(existing);
    }


    public void deleteUser(Long id){
        if (!userRepository.existsById(id)){
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> sortedUser(int pageNumber,int pageSize,String sortBy,String sortOrder){

        Sort sort=sortOrder.equalsIgnoreCase("desc")
                ?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<User> userPage=userRepository.findAll(pageable);
        return userPage.getContent();
    }
}
