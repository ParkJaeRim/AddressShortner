package com.jaerim.shorten.controller;

import java.net.URI;
import java.util.Optional;

import com.jaerim.shorten.model.Address;
import com.jaerim.shorten.repository.AddressRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {
	// private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
	private final String URL_PREFIX = "http://riimy.com/";
	private final int BASE62 = 62;
	private final String BASE62_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	@Autowired
	AddressRepository addressRepository;

	@GetMapping
	@Cacheable(value = "user")
	public String get(@RequestParam(value = "id") String id) {
		// logger.info("get user - userId:{}", id);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return id;
	}

	@DeleteMapping
	@CacheEvict(value = "user")
	public void delete(@RequestParam(value = "id") String id) {
		// logger.info("delete user - userId:{}", id);
	}

	@PostMapping("/shorten")
	@Cacheable(value = "address")
	public String addShortAddres(@RequestBody String longAddress) {
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
		return shortAddress;
	}


	@GetMapping("/{shortkey}")
	public ResponseEntity<?> redirection(@PathVariable String shortkey){
			// log.info(shortkey);
			Optional<Address> address = addressRepository.findByShortkey(shortkey);
			log.info(address.toString());
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create(address.get().getLongaddress()));
			return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}


	private String encoding(long param) {
		StringBuffer sb = new StringBuffer();
		while (param > 0) {
			sb.append(BASE62_CHAR.charAt((int) (param % BASE62)));
			param /= BASE62;
		}
		return sb.toString();
	}

	// private long decoding(String param) {
    //     long sum = 0;
    //     long power = 1;
    //     for (int i = 0; i < param.length(); i++) {
    //         sum += BASE62_CHAR.indexOf(param.charAt(i)) * power;
    //         power *= BASE62;
    //     }
	// 	System.out.println(sum);
    //     return sum;
    // }
}