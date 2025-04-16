package com.witek.discount.infrastructure.cache

import com.witek.config.WithMySqlTestContainer
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class InMemoryDiscountPolicyCacheIntegrationTest : WithMySqlTestContainer {

    @MockitoBean
    private lateinit var discountPolicyRepository: DiscountPolicyRepository

    @Autowired
    private lateinit var discountPolicyCache: DiscountPolicyCache

    @BeforeEach
    fun setUp() {
        reset(discountPolicyRepository)
    }

    @Test
    fun `should read policies only once, but refresh cache should call another db interaction`() {
        // first cache and DB call
        discountPolicyCache.getAll()
        verify(discountPolicyRepository).findAll()

        // second cache call without DB
        discountPolicyCache.getAll()
        verify(discountPolicyRepository).findAll()

        // refresh cache
        discountPolicyCache.refresh()

        // third cache call, but with DB call after refresh
        discountPolicyCache.getAll()
        verify(discountPolicyRepository, times(2)).findAll()

        // fourth cache call without DB
        discountPolicyCache.getAll()
        verifyNoMoreInteractions(discountPolicyRepository)
    }
}