package com.triple.mileage.domain;

import com.triple.mileage.domain.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@Table(indexes = @Index(name = "pointLogIdx", columnList = "USER_ID"))
public class PointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private String reviewId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
