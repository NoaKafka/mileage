package com.triple.mileage.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    private String placeId;

    @Column(nullable = false)
    private Long reviewCnt;

}
