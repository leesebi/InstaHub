package com.sparta.instahub.s3.entity;

import com.sparta.instahub.timeStamp.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private  String url;

    public Image(String name, String url){
        this.name = name;
        this.url = url;
    }
}
