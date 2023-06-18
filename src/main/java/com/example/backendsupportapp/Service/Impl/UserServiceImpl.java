package com.example.backendsupportapp.Service.Impl;


import com.example.backendsupportapp.Service.UserService;
import com.example.backendsupportapp.domain.User;
import com.example.backendsupportapp.domain.UserPrincipal;
import com.example.backendsupportapp.exception.domain.EmailExistException;
import com.example.backendsupportapp.exception.domain.UserNameExistException;
import com.example.backendsupportapp.exception.domain.UserNotFoundException;
import com.example.backendsupportapp.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backendsupportapp.enumeration.Role;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static com.example.backendsupportapp.constant.FileConstant.*;
import static com.example.backendsupportapp.constant.UserImplConstant.*;
import static com.example.backendsupportapp.enumeration.Role.ROLE_USER;


@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
     private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

     User user = userRepository.findUserByUserName(username);





     if(user == null){
         LOGGER.error("User not found by username: "+username);

         throw new UsernameNotFoundException("User not found by username: "+username);
     } else{
         user.setLastLoginDateDisplay(user.getLastLoginDate());
         user.setLastLoginDate(new Date());
         userRepository.save(user);
         UserPrincipal userPrincipal = new UserPrincipal(user);
         LOGGER.info("Returning found user by username: "+username);

         return userPrincipal;

     }
    }


    @Override
    public User register(String firstName, String lastName, String userName, String email) throws UserNotFoundException, UserNameExistException, EmailExistException {

     validateNewUserNameAndEmail(StringUtils.EMPTY, userName, email);
     User user = new User();

        System.out.println("touched");
        user.setUserId(generateUserId());

        String password = generatePassword();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        user.setEmail(email);
        user.setJoinDate(new Date());
        System.out.println("touched2");
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());

        System.out.println("touched3");
        //user.setProfileImageUrl(getTemporaryProfileImageUrl(userName));
        user.setProfileImageUrl("http://www.google.com/dog");
        userRepository.save(user);
        LOGGER.info("New user password: " + password);

        return user;
    }

    private String getTemporaryProfileImageUrl(String userName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH + userName).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);

    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    private User validateNewUserNameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UserNameExistException, EmailExistException {
        User userByNewUserName = findUserByUserName(newUsername);

        User userByNewEmail = findUserByEmail(newEmail);

        if(StringUtils.isNotBlank(currentUsername)) {

            User currentUser = findUserByUserName(currentUsername);

            if(currentUser == null) {

                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }

            if(userByNewUserName != null && !currentUser.getIdUser().equals(userByNewUserName.getIdUser())) {

                throw new UserNameExistException(USERNAME_ALREADY_EXISTS);

            }
            if(userByNewEmail != null && !currentUser.getIdUser().equals(userByNewEmail.getIdUser())) {

                throw new EmailExistException(EMAIL_ALREADY_EXISTS);

            }
            return currentUser;
        } else {
            if(userByNewUserName != null) {
                throw new UserNameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

}
