package com.lazareff.taskmanager.entity;

import com.lazareff.taskmanager.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleType role;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
}
