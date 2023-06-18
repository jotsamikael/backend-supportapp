package com.example.backendsupportapp.Service;

import com.example.backendsupportapp.domain.User;
import com.example.backendsupportapp.exception.domain.EmailExistException;
import com.example.backendsupportapp.exception.domain.UserNameExistException;
import com.example.backendsupportapp.exception.domain.UserNotFoundException;

import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String userName, String email) throws UserNotFoundException, UserNameExistException, EmailExistException;
    List<User> getUsers();



    User findUserByUserName(String userName);

    User findUserByEmail(String email);
}
