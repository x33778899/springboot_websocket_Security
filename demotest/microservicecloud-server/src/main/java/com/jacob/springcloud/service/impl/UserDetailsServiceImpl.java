//package com.jacob.springcloud.service.impl;
//
//import com.jacob.springcloud.model.User;
//import com.jacob.springcloud.service.UserService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserService userService;
//
//    public UserDetailsServiceImpl(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userService.findByAccount(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        // Create a UserDetails object with the required user information
//        // Here, we assume that the User entity has properties for username, password, and roles
//        return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getAccount())
//                .password(user.getPassword())
//                .roles(user.getRole()) // Assuming roles is a Collection<String>
//                .build();
//    }
//}
