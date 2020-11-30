package sshj.sshj.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class UserDto implements UserDetails {
    
	@ApiModelProperty(value = "유저 번호")
	private long userId;
	
	@ApiModelProperty(value = "password")
    private String password;

	@ApiModelProperty(value = "닉네임")
    private String nickname;

	@ApiModelProperty(value = "이메일")
    private String email;

	@ApiModelProperty(value = "가입 날짜")
    private String createdTime;

	@ApiModelProperty(value = "권한")
    private String role;

	@ApiModelProperty(value = "프로필 url")
    private String profileUrl;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Collections.singletonList(this.getRole());
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
	public String getUsername() {
		// Implement 채우려고 만든 빈 메소드
		return null;
	}


}
