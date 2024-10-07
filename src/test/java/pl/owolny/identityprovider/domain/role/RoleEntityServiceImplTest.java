package pl.owolny.identityprovider.domain.role;

import org.junit.jupiter.api.Test;
import pl.owolny.identityprovider.vo.RoleName;
import pl.owolny.identityprovider.domain.role.exception.RoleNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class RoleEntityServiceImplTest {

    private final RoleConfig roleConfig = new RoleConfig(new MockRoleRepository());
    private final RoleService roleService = roleConfig.roleService();

    @Test
    void createNew_ShouldCreateAndSaveRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        RoleInfo roleInfo = roleService.createNew(roleName);
        assertEquals(roleInfo.getName(), roleName);
    }

    @Test
    void addAuthority_ShouldAddAuthorityToRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        roleService.createNew(roleName);
        roleService.addAuthority(roleName, "USER_READ");
        RoleInfo roleInfo = roleService.getByName(roleName);
        assertTrue(roleInfo.getAuthorities().contains("USER_READ"));
    }

    @Test
    void removeAuthority_ShouldRemoveAuthorityFromRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        roleService.createNew(roleName);
        roleService.addAuthority(roleName, "USER_READ");
        roleService.removeAuthority(roleName, "USER_READ");
        RoleInfo roleInfo = roleService.getByName(roleName);
        assertFalse(roleInfo.getAuthorities().contains("USER_READ"));
    }

    @Test
    void deleteByName_ShouldDeleteRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        roleService.createNew(roleName);
        roleService.deleteByName(roleName);
        assertThrows(RoleNotFoundException.class, () -> roleService.getByName(roleName));
    }

    @Test
    void findById_ShouldReturnRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        RoleInfo createdRole = roleService.createNew(roleName);
        RoleInfo foundRole = roleService.getById(createdRole.getId());
        assertEquals(foundRole.getName(), roleName);
    }

    @Test
    void findByName_ShouldReturnRole() {
        RoleName roleName = RoleName.ROLE_ADMIN;
        roleService.createNew(roleName);
        RoleInfo foundRole = roleService.getByName(roleName);
        assertEquals(foundRole.getName(), roleName);
    }

    @Test
    void findByName_ShouldThrowExceptionWhenRoleNotFound() {
        assertThrows(RoleNotFoundException.class, () -> roleService.getByName(RoleName.ROLE_ADMIN));
    }
}
