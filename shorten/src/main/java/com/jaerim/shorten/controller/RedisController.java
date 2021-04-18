package com.jaerim.shorten.controller;

import java.util.Optional;

import com.jaerim.shorten.model.Address;
import com.jaerim.shorten.repository.AddressRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
	private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
	private final String URL_PREFIX = "http://riimy.com/";
	private final int BASE62 = 62;
	private final String BASE62_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	@Autowired
	AddressRepository addressRepository;

	@GetMapping
	@Cacheable(value = "user")
	public String get(@RequestParam(value = "id") String id) {
		logger.info("get user - userId:{}", id);
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
		logger.info("delete user - userId:{}", id);
	}

	@PostMapping("/shorten")
	@Cacheable(value = "address")
	public String addShortAddres(@RequestBody String longAddress) {
		System.out.println(longAddress);
		Optional<Address> address = addressRepository.findByLongaddress(longAddress);
		String shortAddress = "";
		// shortAddress= address.get().getShortaddress();

		if (!address.isEmpty()) {

		} else {
			// int addressId = address.get().getId();
			long sizes = addressRepository.count();
			System.out.println(sizes);
			shortAddress = encoding(sizes + 1);
			System.out.println(shortAddress);
			Address newAddress = Address.builder()
								.longaddress(longAddress)
								.shortaddress(shortAddress)
								.id(sizes)
								.build();
			addressRepository.save(newAddress);
		}
		return shortAddress;
	}

	private String encoding(long param) {
		StringBuffer sb = new StringBuffer();
		while (param > 0) {
			sb.append(BASE62_CHAR.charAt((int) (param % BASE62)));
			param /= BASE62;
		}
		return URL_PREFIX + sb.toString();
	}

}