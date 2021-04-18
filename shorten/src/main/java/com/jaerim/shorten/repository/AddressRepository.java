package com.jaerim.shorten.repository;


import java.util.Optional;

import com.jaerim.shorten.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Integer>{
    
    Optional<Address> findByLongaddress(String longAddress);

    Optional<Address> findByShortaddress(String shortAddress);

    String findShortaddressByLongaddress(String longAddress);


}