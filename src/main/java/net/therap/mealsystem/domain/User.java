package net.therap.mealsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 2/28/22
 */
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends Persistent implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "User name cannot be null")
    private String username;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")})
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @OrderColumn(name = "idx")
    private List<UserRole> roles;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_notif_list", joinColumns = {@JoinColumn(name = "user_id")})
    @Column(name = "notif")
    @OrderColumn(name = "idx")
    private List<String> notifList;

    @Column(name = "account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "account_non_locked")
    private boolean isAccountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Column(name = "account_confirmed")
    private boolean isAccountConfirmed;

    public User() {
        roles = new ArrayList<>();
        notifList = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
