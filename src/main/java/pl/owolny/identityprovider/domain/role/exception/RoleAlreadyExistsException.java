package pl.owolny.identityprovider.domain.role.exception;

import pl.owolny.identityprovider.vo.RoleName;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(RoleName roleName) {
        super("Role with name " + roleName.name() + " already exists");
    }
}
