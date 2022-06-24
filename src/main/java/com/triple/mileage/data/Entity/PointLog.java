package com.triple.mileage.data.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "PointLog")
public class PointLog {
    @Id
    private String logId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long prePoint;

    @Column(nullable = false)
    private int variance;

    @Column(nullable = false)
    private Long version;
}
