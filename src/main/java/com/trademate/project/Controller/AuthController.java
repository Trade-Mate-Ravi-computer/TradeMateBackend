package com.trademate.project.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.trademate.project.Model.*;
import com.trademate.project.Repository.*;
import com.trademate.project.Security.JwtHelper;
import com.trademate.project.Service.EmailService;
import com.trademate.project.Service.UserService;
import jakarta.transaction.Transactional;
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

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import java.util.logging.Logger;

@RestController
@CrossOrigin(value = {"http://localhost:3000","http://localhost:8081","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
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


    private static final int OTP_LENGTH = 6;
    private final Map<String, String> otpStore = new HashMap<>();
    private final Map<String, Long> otpExpiry = new HashMap<>();
    private static final long OTP_EXPIRATION_MS = TimeUnit.MINUTES.toMillis(5); // OTP expires in 5 minutes
    private final SecureRandom secureRandom = new SecureRandom();
 private Logger logger = LoggerFactory.getLogger(AuthController.class);

 @PostMapping("/generate-otp")
 public String generateOtpForLogin(@RequestBody JwtRequest jwtRequest){
   doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
  UserModel userModel=userRepository.findByEmail(jwtRequest.getEmail());
  if(userModel.isVerified()){
      generateOtp(jwtRequest.getEmail());
      return "Otp Sent";
  }else{
      return "Your Email is not verified please verify then login";
  }

 }

    public void generateOtp(String email) {
        // Generate a random 6-digit OTP
        String otp = String.format("%06d", secureRandom.nextInt(999999));

        otpStore.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + OTP_EXPIRATION_MS);
        String message ="Your log in Otp is "+otp+ " don not share this otp any unauthorized person";
        emailService.sendEmail(email,"Your loginOtp  ....",message);
        UserModel userModel=userRepository.findByEmail(email);
        userModel.setOtp(otp);
        userModel.setOtpGenerationTime(LocalTime.now());
        userRepository.save(userModel);
    }

 @PostMapping("/login")
 public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest){
     String validation =validateOtp(jwtRequest);
     if(validation=="Otp Validation Successful"){
         UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
         String token =this.jwtHelper.generateToken(userDetails);
         JwtResponse response = JwtResponse.builder().jwtToken(token).userNAme(userDetails.getUsername()).build();
         UserModel user =userRepository.findByEmail(jwtRequest.getEmail());
         response.setName(user.getName());
         response.setUserId(user.getId());
         return new ResponseEntity<>(response, HttpStatus.OK);
     }
     else {
         return ResponseEntity.ok(validation);
     }
 }

    public String validateOtp(@RequestBody JwtRequest jwtRequest) {
     doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
     UserModel userModel=userRepository.findByEmail(jwtRequest.getEmail());
     if(Integer.parseInt(userModel.getOtp())==jwtRequest.getOtp()){
         if(isOtpExpired(userModel)){
             return "Otp Expired";
         }else{
             return "Otp Validation Successful";
         }
     }else{
         return "Otp is Incorrect";
     }
    }

    public boolean isOtpExpired(UserModel userModel) {
        LocalTime otpGenerationTime = userModel.getOtpGenerationTime();
        LocalTime expirationTime = otpGenerationTime.plus(5, ChronoUnit.MINUTES);
        return LocalTime.now().isAfter(expirationTime);
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
          throw new BadCredentialsException("Invalid User name of Password");
      }
  }
  @Autowired
  private EmailService emailService;


    @PostMapping("/otp/{email}")
    public String generateOtpForForgotPassword(@PathVariable String email){
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

  @Transactional
  @PostMapping("/create_order")
  public  String createOrder(@RequestBody Map<String,Object> order) throws RazorpayException {
      int amt = Integer.parseInt(order.get("amount").toString());
      var client = new RazorpayClient("rzp_test_BK3KSXeGUlGCaD", "fP4JC3Ohnw4aHn5AMZSgTEs8");
      JSONObject ob = new JSONObject();
      ob.put("amount", amt * 100);
      ob.put("currency", "INR");
      ob.put("receipt", "txn_235425");
      //creating order
      Order orders = client.orders.create(ob);
      System.out.println(orders.toString());
      OrdersModel ordersModel = new OrdersModel();
      ordersModel.setUser(userRepository.findByEmail(order.get("email").toString()));
      ordersModel.setOrderId(orders.get("id"));
      ordersModel.setStatus("created");
      ordersModel.setAmount((orders.get("amount")).toString());
      ordersModel.setDurationInMonths(Integer.parseInt(order.get("durationInMonths").toString()));
      ordersModel.setReceipt(orders.get("receipt"));
      ordersRepository.save(ordersModel);
      return orders.toString();
  }
  @PostMapping("/updateOrder")
    public void updateOrder(@RequestBody OrdersModel orderModel){
    OrdersModel order = ordersRepository.findByOrderId(orderModel.getOrderId());
    ordersRepository.save(order);
    UserModel user = userRepository.findByEmail(orderModel.getOrderEmail());
    order.setOrderEmail(orderModel.getOrderEmail());
    order.setCreateDate(orderModel.getCreateDate());
    order.setCurrency(orderModel.getCurrency());
    ordersRepository.save(order);
    userRepository.save(user);
    }

    @PostMapping("/failedPayment")
    public void updateFailedPayemnt(@RequestBody OrdersModel ordersModel){
        OrdersModel order = ordersRepository.findByOrderId(ordersModel.getOrderId());
        order.setStatus(order.getStatus());
        order.setNumberOfAttempt(order.getNumberOfAttempt()+1);
        ordersRepository.save(order);
    }

    @PostMapping("/successOrder")
    public void updateSuccessPayments(@RequestBody OrdersModel ordersModel){
    OrdersModel ordersModel1 =ordersRepository.findByOrderId(ordersModel.getOrderId());
    ordersModel1.setRazorpay_payment_id(ordersModel.getRazorpay_payment_id());
    ordersModel1.setNumberOfAttempt(ordersModel1.getNumberOfAttempt()+1);
    ordersModel1.setRazorpay_signature(ordersModel.getRazorpay_signature());
    ordersModel1.setStatus("Paid");
    ordersRepository.save(ordersModel1);
    UserModel userModel=userRepository.findByEmail(ordersModel1.getOrderEmail());
    userModel.setExpDate(userModel.getExpDate().plusDays(ordersModel1.getDurationInMonths()* 30L));
    userModel.setRemainingDays(userModel.getRemainingDays()>0?userModel.getRemainingDays()+ordersModel1.getDurationInMonths()*30:ordersModel1.getDurationInMonths()*30);
   userModel.setSubDate(LocalDate.now());
    userRepository.save(userModel);
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
