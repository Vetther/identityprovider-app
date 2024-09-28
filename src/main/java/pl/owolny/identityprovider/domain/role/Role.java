package pl.owolny.identityprovider.domain.role;

import jakarta.persistence.*;
import pl.owolny.identityprovider.vo.RoleName;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "roles")
class Role implements RoleInfo {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private RoleId id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_authorities", joinColumns = @JoinColumn(name = "role_id"))
    private Set<String> authorities = new HashSet<>();

    protected Role() {}

    Role(RoleName name) {
        this.id = RoleId.generate();
        this.name = name;
        this.authorities = new HashSet<>();
    }

    void addAuthority(String authority) {
        this.authorities.add(authority);
    }

    void removeAuthority(String authority) {
        this.authorities.remove(authority);
    }

    @Override
    public RoleId getId() {
        return id;
    }

    @Override
    public RoleName getName() {
        return name;
    }

    @Override
    public Set<String> getAuthorities() {
        return authorities.stream()
                .collect(Collectors.toUnmodifiableSet());
    }
}