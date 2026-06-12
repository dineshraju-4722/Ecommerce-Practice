package com.ecommerce.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @NotBlank
    @Size(min = 3 , max = 15)
    private String userName;

    @NotBlank
    @Email
    @Size(max=25)
    private String email;

    @NotBlank
    @Size(max=30)
    private String password;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn( name = "role_id" )
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user" ,
        cascade = {CascadeType.MERGE},
            orphanRemoval = true )
    private Set<Product> products = new HashSet<>();

    @Getter
    @Setter
    @ManyToMany(cascade = { CascadeType.MERGE,CascadeType.PERSIST } )
    @JoinTable(
            name = "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address>  addresses = new ArrayList<>();


    public User( String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
