package com.jaerim.shorten;

import java.util.HashMap;
import java.util.Optional;

import com.jaerim.shorten.model.Address;
import com.jaerim.shorten.repository.AddressRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;

// @RunWith(SpringRunner.class)
@SpringBootTest
public class AddressTest {
    String longAddress = "http://www.kakao.com";
    String shortAddress ;
    
    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void 주소줄이기() {
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(1, 2);
        //given
        shortAddress="http://riimy.jjang/"+longAddress.hashCode();

        System.out.println(shortAddress);
        Address address = Address.builder()
                .longaddress(longAddress)
                .shortaddress(shortAddress)
                .build();
        addressRepository.save(address);
    }

    @Test
    public void 짧은주소가져오기(){
        String shortAddress = addressRepository.findByLongaddress(longAddress).get().getShortaddress();
        System.out.println(shortAddress);
    }

    @Test
    public void 긴주소가져오기(){
		Optional<Address> addressid =  addressRepository.findByShortaddress(shortAddress);
        System.out.println(addressid.get().getId());
        // for (int i = 0; i < al.size(); i++) {
        //     System.out.println(al.get(i).toString());
        // }
    }

    @Test
	@CacheEvict(value = "address")
	public void 지우기(){
        
	}
}
