package pl.owolny.identityprovider.domain.role;

import pl.owolny.identityprovider.vo.RoleName;

import java.util.Set;

public interface RoleInfo {

    RoleId getId();

    RoleName getName();

    Set<String> getAuthorities();
}
