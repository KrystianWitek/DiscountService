package com.witek.discount.infrastructure.cache

import com.witek.discount.infrastructure.db.DiscountPolicyEntity
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable

internal open class InMemoryDiscountPolicyCache(
    private val repository: DiscountPolicyRepository
) : DiscountPolicyCache {

    @Cacheable(CACHE_NAME)
    override fun getAll(): List<DiscountPolicyEntity> {
        log.info("Creating $CACHE_NAME cache")
        return repository.findAll().toList()
    }

    @CacheEvict(CACHE_NAME, allEntries = true)
    override fun refresh() {
        log.info("Refreshing $CACHE_NAME cache")
    }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(InMemoryDiscountPolicyCache::class.java)
        const val CACHE_NAME = "discountPolicies"
    }
}


