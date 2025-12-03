// YANGI FAYL YARATING: src/main/java/uz/pdp/kiyim_online_dokon/config/InitDataLoader.java

package uz.pdp.kiyim_online_dokon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.kiyim_online_dokon.entity.Role;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.RoleRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("\n🚀 InitDataLoader ishga tushdi...\n");

        Role roleAdmin = createRoleIfNotExists("ROLE_ADMIN");
        Role roleManager = createRoleIfNotExists("ROLE_MANAGER");
        Role roleUser = createRoleIfNotExists("ROLE_USER");

    }

    private Role createRoleIfNotExists(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            role = roleRepository.save(role);
            System.out.println("  ➕ Role: " + roleName);
        }
        return role;
    }

}