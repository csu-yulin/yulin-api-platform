package csu.yulin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 创建一个包含用户角色的授权信息集合
        return new ArrayList<>() {
            {
                // 将用户角色添加到授权信息集合中
                add(() -> user.getRole());
            }
        };
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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