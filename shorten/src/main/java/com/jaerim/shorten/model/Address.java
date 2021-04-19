package com.jaerim.shorten.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


//Redis의 Entity라는 것을 표현
@RedisHash("address")
@Getter
@ToString
public class Address {

    @Id
    private long id;
    
    @Indexed
    private String shortkey;
    
    private String longaddress;

    // 필드값으로 데이터를 찾을 수 있음 findbyShortaddress

    @Builder
    public Address(String longaddress, String shortkey, long id) {
        this.longaddress = longaddress;
        this.shortkey = shortkey;
        this.id = id;
    }
}
