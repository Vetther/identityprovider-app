package pl.owolny.identityprovider.domain.role.exception;

import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.vo.RoleName;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(RoleName roleName) {
        super("Role with name " + roleName + " not found");
    }

    public RoleNotFoundException(RoleId roleId) {
        super("Role with id " + roleId + " not found");
    }
}
