package com.cybercity.application.entities;

import com.cybercity.application.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String userName;
    private String email;
    private String phone;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @CreationTimestamp
    private LocalDateTime createDate;

    @CreationTimestamp
    private LocalDateTime updateDate;
    
    private boolean enabled = false;  

    @OneToMany(mappedBy = "userEntity")
    private Set<CourseEntity> courseEntities;

    @OneToMany(mappedBy = "userEntity")
    private Set<PaymentEntity> paymentEntities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof UserEntity user)) return false;
        return Objects.equals(getUserId(),user.getUserId());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getUserId());
    }
}
