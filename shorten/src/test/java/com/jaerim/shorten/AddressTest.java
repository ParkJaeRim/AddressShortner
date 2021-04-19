package com.jaerim.shorten;

import java.util.Optional;

import com.jaerim.shorten.model.Address;
import com.jaerim.shorten.repository.AddressRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;

import lombok.extern.slf4j.Slf4j;

// @RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class AddressTest {
    String longAddress = "http://www.kakao.com";
    String shortAddress ;
    
    private final String URL_PREFIX = "http://riimy.com/";
	private final int BASE62 = 62;
	private final String BASE62_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void 주소줄이기() {
		Optional<Address> address = addressRepository.findByLongaddress(longAddress);
		String shortAddress = "";
		// 음... 이미 있는 거는 어디로 통과되는거지...?
		if (address.isEmpty()) {
			long sizes = addressRepository.count();
			long num = sizes + 100000000;
			shortAddress = encoding(num);
			Address newAddress = Address.builder().longaddress(longAddress).shortkey(shortAddress).id(sizes + 1)
					.build();
			addressRepository.save(newAddress);
			log.info("짧은 키  : " + shortAddress);
			log.info("짧은 주소 : " + URL_PREFIX + shortAddress);
		}
    }

    private String encoding(long param) {
		StringBuffer sb = new StringBuffer();
		while (param > 0) {
			sb.append(BASE62_CHAR.charAt((int) (param % BASE62)));
			param /= BASE62;
		}
		return sb.toString();
	}

    @Test
    public void 짧은주소가져오기(){
        String shortAddress = addressRepository.findByLongaddress(longAddress).get().getShortkey();
        System.out.println(shortAddress);
    }

    @Test
    public void 긴주소가져오기(){
        Optional<Address> address = addressRepository.findByShortkey("OjkvG");
        System.out.println(address);
        log.info(address.toString());
        // System.out.println(x);
    }

    @Test
	@CacheEvict(value = "address")
	public void 지우기(){
        
	}
}
