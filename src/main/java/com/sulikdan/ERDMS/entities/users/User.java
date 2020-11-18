package com.sulikdan.ERDMS.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Daniel Šulik on 03-Sep-20
 * <p>
 * Class User is used for .....
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    private static final long serialVersionUID = -3399148226279293885L;

    @Id
    @Builder.Default
    private String id =  new ObjectId().toString();

//    private String firstName;
//    private String lastName;

    @Email
    private String email;

    @Size(min = 4, max = 255)
    private String username;

    @JsonIgnore
    @Size(min = 5, max = 1024)
    private String password;

    @Builder.Default
    private UserRole userRole = UserRole.User;

    @Builder.Default
    private Boolean locked = false;

    @Builder.Default
    private Boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
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
        return enabled;
    }

}
