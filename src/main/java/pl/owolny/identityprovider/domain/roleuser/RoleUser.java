package pl.owolny.identityprovider.domain.roleuser;

import jakarta.persistence.*;
import pl.owolny.identityprovider.domain.role.RoleId;
import pl.owolny.identityprovider.domain.user.UserId;

@Entity
class RoleUser {

    @EmbeddedId
    private RoleUserId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "role_id"))
    private RoleId roleId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    RoleUser(RoleId roleId, UserId userId) {
        this.id = RoleUserId.generate();
        this.roleId = roleId;
        this.userId = userId;
    }

    protected RoleUser() {}

    public RoleId getRoleId() {
        return roleId;
    }

    public UserId getUserId() {
        return userId;
    }
}
