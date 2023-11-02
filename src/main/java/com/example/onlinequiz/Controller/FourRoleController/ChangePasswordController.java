package com.example.onlinequiz.Controller.FourRoleController;

import com.example.onlinequiz.Payload.Request.PasswordChangeRequest;
import com.example.onlinequiz.Security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin( allowedHeaders = "*", origins = "https://quangnv1911-fe.onrender.com")
@RestController
@RequestMapping("/change-password")
public class ChangePasswordController {
    @Autowired
    UserDetailsServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest){
        // Lấy thông tin từ yêu cầu đổi mật khẩu
        String email = passwordChangeRequest.getEmail();
        String oldPassword = passwordChangeRequest.getOldPassword();
        String newPassword = passwordChangeRequest.getNewPassword();

        // Lấy thông tin người dùng dựa trên email
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Kiểm tra xem người dùng tồn tại và mật khẩu cũ đúng
        if (userDetails != null && passwordEncoder.matches(oldPassword, userDetails.getPassword())){
            // Mã hóa mật khẩu mới và cập nhật mật khẩu trong cơ sở dữ liệu
            String encodedPassword = passwordEncoder.encode(newPassword);
            userService.updatePassword(email, encodedPassword);
            return ResponseEntity.ok("Password change successfully!"); // Trả về 200 OK nếu thành công
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Something wrong in email or password");
            // Trả về lỗi 401 UNAUTHORIZED nếu có lỗi
        }
    }
}
