package com.trademate.project.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.trademate.project.Model.*;
import com.trademate.project.Repository.*;
import com.trademate.project.Security.JwtHelper;
import com.trademate.project.Service.EmailService;
import com.trademate.project.Service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
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
import com.razorpay.RazorpayClient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
@Autowired
private ContactUsRepository contactUsRepository;
@Autowired
private SaleRepository saleRepository;
@Autowired
private ExpenseRepository expenseRepository;
@Autowired
private OrdersRepository ordersRepository;
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
    user.setSubscribed(true);
    user.setRemainingDays(15);
    user.setSubDate(LocalDate.now());
    user.setExpDate(LocalDate.now().plusDays(user.getRemainingDays()));
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
    @PostMapping("/contact")
public String addContact(@RequestBody ContactUsModel contact){
      try{
          if(contact.getEmail()!=null){
              contactUsRepository.save(contact);
              String message = "We are TradeMate,\n Thanks for contacting us. We are glad that you are here. We will connect with you very quickly,\n and we will answer your question as soon as possible.\n Regards,\n TradeMate";
              emailService.sendEmail(contact.getEmail(),"Thanks for contacting Us",message);
              emailService.sendEmail("tradematebusinessapp@gmail.com","new Querry from\n"+contact.getEmail(),contact.getMessage());
              return "Your query has been sent to TradeMate.";
          }else{
              return "Something is wrong please enter a valid email";
          }
      }catch(IllegalArgumentException e){
          return "Please enter valid email";
      }
    }
@Autowired
private FeedbackRepository feedbackRepository;
@GetMapping("/feedback")
public List<FeedbackModel> getAll(){
      return feedbackRepository.findAll();
}
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){
      return "Credential Invalid";
  }

  @PostMapping("/create_order")
    public  String createOrder(@RequestBody Map<String,Object> order) throws RazorpayException {
      int amt = Integer.parseInt(order.get("amount").toString());
      var client = new RazorpayClient("rzp_test_daKBtgff3GpV4I", "Szf9Sb91O1eDFyyMHgV1aDoM");
      JSONObject ob = new JSONObject();
      ob.put("amount", amt * 100);
      ob.put("currency", "INR");
      ob.put("receipt", "txn_235425");
      //creating order
      Order orders = client.orders.create(ob);
      OrdersModel ordersModel = new OrdersModel();
      ordersModel.setUser(userRepository.findByEmail(order.get("email").toString()));
      ordersModel.setOrderId(orders.get("id"));
      ordersModel.setStatus("created");
      ordersModel.setAmount((orders.get("amount")).toString());
      ordersModel.setReceipt(orders.get("receipt"));
      ordersRepository.save(ordersModel);
      return orders.toString();
  }
  @PostMapping("/updateOrder")
    public void updateOrder(@RequestBody Map<String,String> orderStatus){
    OrdersModel order = ordersRepository.findByOrderId(orderStatus.get("order_id"));
    order.setStatus(orderStatus.get("status"));
    ordersRepository.save(order);
    UserModel user = userRepository.findByEmail(orderStatus.get("email"));
    user.setRemainingDays((Math.max(user.getRemainingDays(), 0))+Integer.parseInt(orderStatus.get("days")));
    user.setExpDate(user.getExpDate().plusDays(Integer.parseInt(orderStatus.get("days"))));
    userRepository.save(user);
    }
  @GetMapping("/setSubscription/{email}")
    public String setSubscription(@PathVariable String email){
    UserModel user = userRepository.findByEmail(email);
    user.setSubscribed(false);
    userRepository.save(user);
      long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(),user.getSubDate());
      System.out.println("Remaining Days  "+daysDifference);
    return "Subscription saved";
  }

}
