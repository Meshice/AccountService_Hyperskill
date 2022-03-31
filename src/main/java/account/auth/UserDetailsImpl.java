package account.auth;

import account.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;
    private final boolean locked;

    public UserDetailsImpl(User userTable) {
        this.username = userTable.getEmail();
        this.password = userTable.getPassword();
        this.rolesAndAuthorities = Arrays.stream(userTable.getRole().split(" ")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        this.locked = userTable.isLocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
