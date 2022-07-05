package com.triple.mileage.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
public class Place {

    @Id
    private String placeId;

    @Column(nullable = false)
    private Long reviewCnt;

}
