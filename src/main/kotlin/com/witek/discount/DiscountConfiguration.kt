package com.witek.discount

import com.witek.discount.api.DiscountCalculator
import com.witek.discount.application.DefaultDiscountCalculator
import com.witek.discount.domain.DiscountEngine
import com.witek.discount.domain.DiscountPolicy
import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.cache.InMemoryDiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import com.witek.discount.infrastructure.policy.PercentageBasedDiscountPolicy
import com.witek.discount.infrastructure.policy.QuantityBasedDiscountPolicy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class DiscountConfiguration {

    @Bean
    fun discountPolicyCacheService(repository: DiscountPolicyRepository): DiscountPolicyCache {
        return InMemoryDiscountPolicyCache(repository)
    }

    @Bean
    fun discountCalculator(cache: DiscountPolicyCache): DiscountCalculator {
        return DiscountEngineFactory.create(cache)
    }

    private object DiscountEngineFactory {
        fun create(cache: DiscountPolicyCache): DiscountCalculator {
            val policies: List<DiscountPolicy> = listOf(
                PercentageBasedDiscountPolicy(cache),
                QuantityBasedDiscountPolicy(cache)
            )
            val engine = DiscountEngine(policies)
            return DefaultDiscountCalculator(engine)
        }
    }
}