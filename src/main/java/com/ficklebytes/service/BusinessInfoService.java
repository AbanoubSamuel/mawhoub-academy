package com.ficklebytes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ficklebytes.entity.BusinessInfo;
import com.ficklebytes.repository.BusinessInfoRepository;
import com.ficklebytes.repository.DocumentRepository;

@Service
public class BusinessInfoService {

    @Autowired
    private BusinessInfoRepository businessInfoRepository;

    /**
     * ✅ Cache the result of this method under the key 'businessInfo'.
     * Next time it's called, Spring will return the cached value
     * instead of hitting the database.
     */
    @Cacheable(value = "businessInfoCache", key = "'singleton'")
    public BusinessInfo getBusinessInfo() {
        System.out.println("🔍 Fetching BusinessInfo from DB..."); // For testing
        return businessInfoRepository.findAll().get(0);
    }

    /**
     * ✅ When business info is updated, remove it from the cache.
     * The next getBusinessInfo() call will refresh the cache automatically.
     */
    @CacheEvict(value = "businessInfoCache", key = "'singleton'")
    public BusinessInfo updateBusinessInfo(BusinessInfo businessInfo) {
        return businessInfoRepository.save(businessInfo);
    }
}
