package com.triple.mileage.data.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "User")
public class User {
    @Id
    private String userId;

    @Column(nullable = false)
    private Long point;
}
