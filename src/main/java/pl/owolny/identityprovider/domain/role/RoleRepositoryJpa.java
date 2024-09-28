package pl.owolny.identityprovider.domain.role;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.vo.RoleName;

import java.util.Optional;

interface RoleRepositoryJpa extends RoleRepository, JpaRepository<Role, RoleId> {

    Optional<Role> findByName(RoleName name);
}
