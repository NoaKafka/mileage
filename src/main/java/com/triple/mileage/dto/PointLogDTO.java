package com.triple.mileage.dto;

import com.triple.mileage.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointLogDTO {
    private Long logId;
    private String reviewId;
    private Long amount;
    private String action;
    private String userId;
}
