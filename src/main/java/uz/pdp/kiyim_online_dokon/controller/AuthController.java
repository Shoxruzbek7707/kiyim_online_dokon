package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.*;
import uz.pdp.kiyim_online_dokon.security.AuthService;
import uz.pdp.kiyim_online_dokon.service.impl.EmailServiceImpl;
import uz.pdp.kiyim_online_dokon.service.impl.UsersServiceImpl;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailServiceImpl emailService;
    private final UsersServiceImpl usersService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        try {
            authService.register(authRequest);
            return ResponseEntity.ok("Foydalanuvchi muvaffaqiyatli ro'yxatdan o'tdi!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Ro'yxatdan o'tishda xatolik: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.token(loginRequest);
        if (token == null) {
            return ResponseEntity.status(401).body("Username yoki parol noto'g'ri");
        }
        return ResponseEntity.ok(token);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetRequest request) {
        try {
            // Email mavjudligini tekshirish
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.status(400).body("Email kiritilishi shart!");
            }

            // Verification code yaratish va emailga yuborish
            String result = emailService.sendVerificationCode(request.getEmail());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body("Email yuborishda xatolik: " + e.getMessage());
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetConfirm request) {
        try {

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.status(400).body("Email kiritilishi shart!");
            }
            if (request.getVerificationCode() == null || request.getVerificationCode().isEmpty()) {
                return ResponseEntity.status(400).body("Verification code kiritilishi shart!");
            }
            if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
                return ResponseEntity.status(400).body("Yangi parol kiritilishi shart!");
            }


            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.status(400)
                        .body("Parol kamida 6 ta belgidan iborat bo'lishi kerak!");
            }


            boolean isCodeValid = emailService.verifyCode(
                    request.getEmail(),
                    request.getVerificationCode()
            );

            if (!isCodeValid) {
                return ResponseEntity.status(400)
                        .body("Kod noto'g'ri yoki muddati tugagan. Qaytadan kod so'rang!");
            }


            usersService.updatePassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Parol muvaffaqiyatli yangilandi!");

        } catch (RuntimeException e) {

            return ResponseEntity.status(404)
                    .body("Bu email bilan foydalanuvchi topilmadi!");
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body("Parolni yangilashda xatolik: " + e.getMessage());
        }
    }


    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestBody PasswordResetRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity.status(400).body("Email kiritilishi shart!");
            }

            String result = emailService.sendVerificationCode(request.getEmail());
            return ResponseEntity.ok("Yangi " + result);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body("Kodni qayta yuborishda xatolik: " + e.getMessage());
        }
    }


    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        try {
            // Ma'lumotlar to'liqligini tekshirish
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                return ResponseEntity.status(400).body("Username kiritilishi shart!");
            }
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                return ResponseEntity.status(400).body("Eski parol kiritilishi shart!");
            }
            if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
                return ResponseEntity.status(400).body("Yangi parol kiritilishi shart!");
            }

            // Yangi parol uzunligini tekshirish
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.status(400)
                        .body("Yangi parol kamida 6 ta belgidan iborat bo'lishi kerak!");
            }

            // Eski va yangi parol bir xil bo'lmasligini tekshirish
            if (request.getOldPassword().equals(request.getNewPassword())) {
                return ResponseEntity.status(400)
                        .body("Yangi parol eski paroldan farq qilishi kerak!");
            }

            // Parolni yangilash (eski parolni tekshirish bilan)
            usersService.updatePasswordWithVerification(
                    request.getUsername(),
                    request.getOldPassword(),
                    request.getNewPassword()
            );

            return ResponseEntity.ok("Parol muvaffaqiyatli yangilandi!");

        } catch (RuntimeException e) {
            // User topilmasa yoki parol noto'g'ri bo'lsa
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body("Parolni yangilashda xatolik: " + e.getMessage());
        }
    }
}