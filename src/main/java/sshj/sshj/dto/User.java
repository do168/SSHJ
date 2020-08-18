package sshj.sshj.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class User implements UserDetails {
//    private long user_id;

    private String id;

    private String password;

    private String nickname;

    private String email;

    private String created_time;

    private String role;

    @Builder.Default
    private List<String> roles = new ArrayList<>();  // 여기서 오류날 수 있으니 유의하자

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        this.roles.add(this.getRole());
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return id;
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


}
