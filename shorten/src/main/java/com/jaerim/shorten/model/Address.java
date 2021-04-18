package com.jaerim.shorten.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


//Redis의 Entity라는 것을 표현
@RedisHash("address")
@Getter
@ToString
public class Address {
       
    @Id
    // @Indexed
    private long id;
    
    //second index 지원
    private String longaddress;
    
    // @Indexed
    //필드값으로 데이터를 찾을 수 있음 findbyShortaddress
    private String shortaddress;

    @Builder
    public Address(String longaddress, String shortaddress, long id) {
        this.longaddress = longaddress;
        this.shortaddress = shortaddress;
        this.id = id;
    }
}
