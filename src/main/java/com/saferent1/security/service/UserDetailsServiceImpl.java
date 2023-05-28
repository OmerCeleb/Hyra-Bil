package com.saferent1.security.service;

import com.saferent1.domain.User;
import com.saferent1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
// Eftersom affärslogiken finns i tjänsteskiktet kommer vi först att navigera till tjänsten, därifrån går den till repo, men även om vi skriver repo så fungerar koden
// vi injicerar den i userService eftersom den är bästa praxis

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user =  userService.getUserByEmail(email);
       return UserDetailsImpl.build(user);  // Jag skriver detta för att användaren inte ska återvända

    }
}
