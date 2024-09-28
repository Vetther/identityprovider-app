package pl.owolny.identityprovider.domain.role;

import pl.owolny.identityprovider.vo.RoleName;

public interface RoleService {

    RoleInfo createNew(RoleName name);

    void addAuthority(RoleName roleName, String authority);

    void removeAuthority(RoleName roleName, String authority);

    void deleteByName(RoleName roleName);

    RoleInfo getById(RoleId id);

    RoleInfo getByName(RoleName roleName);

}
