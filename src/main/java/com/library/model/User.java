package com.library.model;

import com.library.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/*
UserDetails is an interface provided by Spring Security.

Spring Security needs information about a user, such as:

username
password
roles/authorities
whether the account is enabled
whether the account is locked
whether credentials have expired

By implementing UserDetails, you're saying:

"My User class contains all the information Spring Security needs about an authenticated user."

 */
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;  // stored as bcrypt hash

    @Enumerated(EnumType.STRING)
    private Role role;

    // UserDetails methods Spring Security requires:

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //"What permissions/roles does this user have?"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;  // Spring uses this as the unique identifier
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}