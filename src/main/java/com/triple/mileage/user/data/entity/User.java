package com.triple.mileage.user.data.entity;

import com.triple.mileage.event.data.entity.LinkPhoto;
import com.triple.mileage.point.data.PointLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column
    private Long point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointLog> pointLogs;

}
