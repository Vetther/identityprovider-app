package pl.owolny.identityprovider.domain.roleuser;

import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Optional;
import java.util.Set;

interface RoleUserRepository {

    <S extends RoleUser> S save(S RoleUser);

    void delete(RoleUser roleUser);

    Optional<RoleUser> findById(RoleUserId id);

    Optional<RoleUser> findByUserIdAndRoleId(UserId userId, RoleId roleId);

    Set<RoleUser> findByUserId(UserId userId);
}
