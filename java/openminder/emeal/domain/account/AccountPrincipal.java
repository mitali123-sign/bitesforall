package openminder.emeal.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter @Setter
public class AccountPrincipal implements UserDetails {

    private Long accountId;

    private String username;

    @JsonIgnore
    private String password;


    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    private Collection<? extends GrantedAuthority> authorities;

    public AccountPrincipal(Long accountId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static AccountPrincipal create(Account account) {
        List<GrantedAuthority> authorities = account.getAuthorities().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getAuthorityName().name())).collect(Collectors.toList());

        return new AccountPrincipal(
                account.getAccountId(),
                account.getUsername(),
                account.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountPrincipal that = (AccountPrincipal) o;
        return Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
