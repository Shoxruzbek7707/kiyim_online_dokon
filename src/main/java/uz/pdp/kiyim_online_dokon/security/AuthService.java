package uz.pdp.kiyim_online_dokon.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.AuthRequest;
import uz.pdp.kiyim_online_dokon.dto.LoginRequest;
import uz.pdp.kiyim_online_dokon.entity.Role;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.jwt.JwtUtils;
import uz.pdp.kiyim_online_dokon.repository.RoleRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;

    public String token(LoginRequest loginRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        if(passwordEncoder.matches(loginRequest.password(),userDetails.getPassword())){
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtUtils.generateToken(userDetails.getUsername());
        }
        return null;
    }

    public void register(AuthRequest authRequest){
        boolean exists = userRepository.existsByUsername(authRequest.username());
        if(!exists){
            Role roleUser = roleRepository.findByName("ROLE_USER");
            userRepository.save(Users
                    .builder()
                    .username(authRequest.username())
                    .password(passwordEncoder.encode(authRequest.password()))
                    .email(authRequest.email())
                    .roles(Set.of(roleUser))
                    .build());
        }
    }
}
