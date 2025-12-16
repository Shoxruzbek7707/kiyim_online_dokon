package uz.pdp.kiyim_online_dokon.service.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.entity.VerificationCode;
import uz.pdp.kiyim_online_dokon.repository.VerificationCodeRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    public String sendVerificationCode(String email) {
        // 6 raqamli tasodifiy kod yaratish
        String code = String.format("%06d", new Random().nextInt(999999));

        // Kodni bazaga saqlash (10 daqiqa amal qiladi)
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(verificationCode);

        // Email yuborish
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Verification Code");
        message.setText("Sizning parolni tiklash kodingiz: " + code +
                "\nBu kod 10 daqiqa amal qiladi.");

        mailSender.send(message);

        return "Verification code sent to " + email;
    }

    public boolean verifyCode(String email, String code) {
        VerificationCode verificationCode = verificationCodeRepository
                .findByEmailAndCodeAndExpiryTimeAfter(email, code, LocalDateTime.now())
                .orElse(null);

        if (verificationCode != null) {
            // Kod ishlatilgan, o'chirish
            verificationCodeRepository.delete(verificationCode);
            return true;
        }
        return false;
    }
}