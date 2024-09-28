package pl.owolny.identityprovider.domain.role;

import pl.owolny.identityprovider.common.InMemoryJpaRepository;
import pl.owolny.identityprovider.vo.RoleName;

import java.util.Optional;

class MockRoleRepository extends InMemoryJpaRepository<Role, RoleId> implements RoleRepository {

    MockRoleRepository() {
        super(Role::getId);
    }

    @Override
    public Optional<Role> findByName(RoleName name) {
        return super.database.values().stream()
                .filter(role -> role.getName().equals(name))
                .findFirst();
    }
}
