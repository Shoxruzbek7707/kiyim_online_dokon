package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(String name);

}
