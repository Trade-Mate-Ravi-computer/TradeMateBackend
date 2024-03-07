package com.trademate.project.Controller;

import com.trademate.project.Model.JwtRequest;
import com.trademate.project.Model.JwtResponse;
import com.trademate.project.Model.UpdatePasswordRequest;
import com.trademate.project.Model.UserModel;
import com.trademate.project.Repository.UserRepository;
import com.trademate.project.Security.JwtHelper;
import com.trademate.project.Service.EmailService;
import com.trademate.project.Service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
 @Autowired
 private UserRepository userRepository;
@Autowired
private PasswordEncoder passwordEncoder;
 private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
 public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
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
public String setVerification(@PathVariable String email){
       return userService.setVerification(email);
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
  @Autowired
  private EmailService emailService;
  @GetMapping("/sendemail")
  public String sendMail(){
        emailService.sendEmail("anamikamaurya460@gmail.com","Regaurding your Email Verification","Hi Anamika ji You are very cute and beautifull");

        return "Email Sent";
  }

    @PostMapping("/otp/{email}")
    public String generateOtp(@PathVariable String email){
        UserModel user =userService.getByEmail(email);
        String otp= RandomStringUtils.randomNumeric(6);
        if(user!=null){
                  user.setOtp(otp);
                  userRepository.save(user);
                  String massage="Use this Otp to regenerate your password :-\n OTP:- "+otp+"\n this otp will expire after one hour"+"\n TradeMate";
                  emailService.sendEmail(user.getEmail(),"OTP for new password",massage);
                  return "Otp Has been Sent";
        }
        else{
            return "Your Email is not registered Please Signup";
        }
    }
    @PostMapping("/updatepassword")
    public String updatePassword(@RequestBody UpdatePasswordRequest password) {
      if (password.getConfirmPassword().equals(password.getNewPassword())) {
            // Retrieve user by email
            UserModel user = userService.getByEmail(password.getEmail());

            // Verify OTP
            if (user.getOtp().equals(password.getOtp())) {
                System.out.println(password.getNewPassword());
                System.out.println(passwordEncoder.encode(password.getNewPassword()));
                user.setPassword(passwordEncoder.encode(password.getNewPassword()));
                user.setOtp("");
                userRepository.save(user); // Save updated user

                return "Password Changed";
            } else {
                return "Please enter correct OTP";
            }
        } else {
            return "New password and confirm password should be the same";
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
      return "Credential Invalid";
  }

}
