package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.UserDTO;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.UsersService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateUser(Integer id, UserDTO dto) {
        // User ma'lumotlarini yangilash
    }

    @Override
    public void deleteUser(Integer id) {
        // User o'chirish
    }

    @Override
    public UserDTO getUser(Integer id) {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    /**
     * Parolni email orqali yangilash (forgot password uchun)
     * Bu metod verification code tasdiqlangandan keyin ishlatiladi
     */
    public void updatePassword(String email, String newPassword) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Parolni shifrlash va saqlash
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    /**
     * Tizimga kirgan user uchun parolni yangilash
     * Eski parolni tekshiradi va to'g'ri bo'lsa yangi parol o'rnatadi
     */
    public void updatePasswordWithVerification(String username, String oldPassword, String newPassword) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User topilmadi!"));

        // Eski parolni tekshirish
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Eski parol noto'g'ri!");
        }

        // Yangi parolni shifrlash va saqlash
        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }
}