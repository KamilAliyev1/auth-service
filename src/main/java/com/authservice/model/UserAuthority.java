package com.authservice.model;


import com.authservice.model.grant.AppUserPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@ToString(exclude = "userSecurityDetails")
@EqualsAndHashCode(of = "ID")
@Table(name = "auths")
public class UserAuthority {

    @Id
    Long ID;

    @Enumerated(EnumType.STRING)
    AppUserPermission Permission;


    @JsonIgnore
    @ManyToMany(mappedBy = "grantedAuths",fetch = FetchType.LAZY)
    Set<UserSecurityDetail> userSecurityDetails;

}
