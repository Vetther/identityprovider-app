package pl.owolny.identityprovider.domain.role;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.vo.RoleName;
import pl.owolny.identityprovider.domain.role.exception.RoleAlreadyExistsException;
import pl.owolny.identityprovider.domain.role.exception.RoleNotFoundException;

class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public RoleInfo createNew(RoleName roleName) {
        if (this.roleRepository.findByName(roleName).isPresent()) {
            throw new RoleAlreadyExistsException(roleName);
        }
        return roleRepository.save(new Role(roleName));
    }

    @Override
    @Transactional
    public void addAuthority(RoleName roleName, String authority) {
        Role role = (Role) this.getByName(roleName);
        role.addAuthority(authority);
    }

    @Override
    @Transactional
    public void removeAuthority(RoleName roleName, String authority) {
        Role role = (Role) this.getByName(roleName);
        role.removeAuthority(authority);
    }

    @Override
    @Transactional
    public void deleteByName(RoleName roleName) {
        Role role = (Role) this.getByName(roleName);
        this.roleRepository.delete(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleInfo getById(RoleId id) {
        return this.roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleInfo getByName(RoleName roleName) {
        return this.roleRepository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException(roleName));
    }
}
