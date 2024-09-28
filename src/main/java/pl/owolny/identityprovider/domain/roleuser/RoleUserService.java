package pl.owolny.identityprovider.domain.roleuser;

import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.user.UserId;

import java.util.Set;

public interface RoleUserService {

    void assignRoleToUser(UserId userId, RoleId roleId);

    void revokeRoleFromUser(UserId userId, RoleId roleId);

    Set<RoleId> getUserRoles(UserId userId);

    boolean hasRole(UserId userId, RoleId roleId);
}
