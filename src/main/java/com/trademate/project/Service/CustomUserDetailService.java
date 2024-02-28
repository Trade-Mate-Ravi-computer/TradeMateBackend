package com.trademate.project.Service;

import com.trademate.project.Model.UserModel;
import com.trademate.project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
   @Autowired
   private UserRepository userRepository;
   @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//       System.out.println("email"+ email);
       UserModel user =userRepository.findByEmail(email);
        return user;
    }
}
