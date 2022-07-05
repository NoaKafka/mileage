package com.triple.mileage.domain;

import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.function.Supplier;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(nullable = false)
    private Long point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointLog> pointLogs;

}
