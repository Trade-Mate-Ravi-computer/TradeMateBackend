package com.trademate.project.Controller;

import com.trademate.project.Model.JwtRequest;
import com.trademate.project.Model.JwtResponse;
import com.trademate.project.Model.UserModel;
import com.trademate.project.Security.JwtHelper;
import com.trademate.project.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

//import java.util.logging.Logger;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/"})
@RequestMapping("/auth")
public class AuthController {
 @Autowired
    private UserDetailsService userDetailsService;
 @Autowired
 private UserService userService;
 @Autowired
    private JwtHelper jwtHelper;
 @Autowired
    private AuthenticationManager authenticationManager;

 private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
 public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
        System.out.println(jwtRequest.getEmail());
        System.out.println(jwtRequest.getPassword());
this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
     UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
     String token =this.jwtHelper.generateToken(userDetails);
     JwtResponse response = JwtResponse.builder().jwtToken(token).userNAme(userDetails.getUsername()).build();
     return new ResponseEntity<>(response, HttpStatus.OK);
 }
@PostMapping("/sign-up")
public UserModel addUser(@RequestBody UserModel user){
        user.setVerified(false);
        return userService.addUser(user);
}
@GetMapping("/setverify/{email}")
public void setVerification(@PathVariable String email){
        userService.setVerification(email);
}
  private void doAuthenticate(String email,String password){
      UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(email,password);
      try{
          authenticationManager.authenticate(authentication);
      }catch(BadCredentialsException be){
//          System.out.println(be);
          throw new BadCredentialsException("Invailid User name of Password");
      }
  }
  @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
      return "Credential Invailid";
  }
}
