package pl.owolny.identityprovider.domain.role;

import pl.owolny.identityprovider.vo.RoleName;

import java.util.Optional;

interface RoleRepository {

    <S extends Role> S save(S role);

    void delete(Role role);

    Optional<Role> findById(RoleId id);

    Optional<Role> findByName(RoleName name);
}
