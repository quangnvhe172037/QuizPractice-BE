package com.example.onlinequiz.Controller.PublicController;

import com.example.onlinequiz.Event.RegistrationCompleteEvent;
import com.example.onlinequiz.Exception.UserAlreadyExistException;
import com.example.onlinequiz.Model.Users;
import com.example.onlinequiz.Payload.Request.RegistrationRequest;
import com.example.onlinequiz.Repo.VerificationTokenRepository;
import com.example.onlinequiz.Services.Impl.UserServiceImpl;
import com.example.onlinequiz.Token.VerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin("*") // Cho phép CORS từ mọi nguồn
@RestController // Đánh dấu đây là một Controller
@RequestMapping("/register") // Định nghĩa đường dẫn cơ sở cho Controller
@RequiredArgsConstructor // Tự động tạo constructor với tham số cho các trường được đánh dấu là final
public class RegistrationController {

    // Tiêm UserServiceImpl vào Controller
    @Autowired
    private final UserServiceImpl userService;

    // Tiêm ApplicationEventPublisher vào Controller để gửi sự kiện
    @Autowired
    private final ApplicationEventPublisher publisher;

    // Tiêm VerificationTokenRepository vào Controller
    @Autowired
    private final VerificationTokenRepository tokenRepository;

    // Xử lý các yêu cầu POST
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request, final HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Gọi UserService để đăng ký người dùng
            Users user = userService.registerUser(request);

            // Gửi sự kiện đăng ký hoàn thành
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(httpServletRequest)));


            response.put("success", true);
            response.put("message", "Register success! Please check your email for registration");
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserAlreadyExistException(e.getMessage()));
        }
    }

    // Xử lý các yêu cầu GET đến /register/verifyEmail
    @GetMapping("/verifyEmail/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            return ResponseEntity.ok("This account has already been verified, please login");
        }
        String verificationResult = userService.validateaToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This account has been verified, please login");
        } else if(verificationResult.equalsIgnoreCase("Token already expired")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has been expired, register again.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token");
    }

    // Phương thức để xây dựng URL ứng dụng dựa trên yêu cầu HTTP
    private String applicationUrl(HttpServletRequest httpServletRequest) {

        return "http://" + httpServletRequest.getServerName() + ":8081" + httpServletRequest.getContextPath();

    }
}
