package ru.practicum.user.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
// GrantedAuthority отражает разрешения выданные пользователю в масштабе всего приложения,
// такие разрешения (как правило называются «роли»), например ROLE_ANONYMOUS, ROLE_USER, ROLE_ADMIN.
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // должно соответствовать шаблону: «ROLE_ИМЯ», например, ROLE_USER
    private String name;

    @Transient
    @ManyToMany(mappedBy = "user_roles")
    private Set<User> users;

    @Override
    public String getAuthority() {
        return getName();
    }
}