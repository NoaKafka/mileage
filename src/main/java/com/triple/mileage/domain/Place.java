package com.triple.mileage.domain;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Place {

    @Id
    private String placeId;

    @Column
    private Long reviewCnt;

}
