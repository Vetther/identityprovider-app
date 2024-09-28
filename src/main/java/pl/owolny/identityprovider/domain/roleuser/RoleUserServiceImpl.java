package pl.owolny.identityprovider.domain.roleuser;

import org.springframework.transaction.annotation.Transactional;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Set;
import java.util.stream.Collectors;

class RoleUserServiceImpl implements RoleUserService {

    private final RoleUserRepository roleUserRepository;

    RoleUserServiceImpl(RoleUserRepository roleUserRepository) {
        this.roleUserRepository = roleUserRepository;
    }

    @Override
    @Transactional
    public void assignRoleToUser(UserId userId, RoleId roleId) {
        var roleUser = new RoleUser(roleId, userId);
        roleUserRepository.save(roleUser);
    }

    @Override
    @Transactional
    public void revokeRoleFromUser(UserId userId, RoleId roleId) {
        var roleUser = roleUserRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " does not have role with id " + roleId));
        roleUserRepository.delete(roleUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<RoleId> getUserRoles(UserId userId) {
        return roleUserRepository.findByUserId(userId).stream()
                .map(RoleUser::getRoleId)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(UserId userId, RoleId roleId) {
        return roleUserRepository.findByUserIdAndRoleId(userId, roleId).isPresent();
    }
}
