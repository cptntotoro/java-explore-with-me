package ru.practicum.security.model;

import lombok.*;
import ru.practicum.user.model.User;

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
public class Role  { // implements GrantedAuthority
    @Id
    private Long id;

    // должно соответствовать шаблону: «ROLE_ИМЯ», например, ROLE_USER
    private String name;

    @Transient
    @ManyToMany(mappedBy = "user_roles")
    private Set<User> users;

    public Role(Long id) {
        this.id = id;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

//    @Override
//    public String getAuthority() {
//        return getName();
//    }
}
