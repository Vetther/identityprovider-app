package pl.owolny.identityprovider.domain.roleuser;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;
import java.util.Set;

interface RoleUserRepositoryJpa extends RoleUserRepository, JpaRepository<RoleUser, RoleUserId> {

    Optional<RoleUser> findByUserIdAndRoleId(UserId userId, RoleId roleId);

    Set<RoleUser> findByUserId(UserId userId);
}
