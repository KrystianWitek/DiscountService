package com.witek.discount.web

import com.witek.discount.infrastructure.cache.DiscountPolicyCache
import com.witek.discount.infrastructure.db.DiscountPolicyRepository
import com.witek.product.ProductRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(controllers = [DiscountMaintenanceController::class])
@ActiveProfiles("test")
@MockitoBean(types = [DiscountPolicyRepository::class, ProductRepository::class])
class DiscountMaintenanceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var cacheService: DiscountPolicyCache

    @Test
    fun `should return unauthorized status, when token is invalid`() {
        mockMvc.post(PATH) {
            header(CustomHeaders.ADMIN_TOKEN_HEADER, "invalid_token")
        }.andExpect {
            status { isUnauthorized() }
        }

        verifyNoMoreInteractions(cacheService)
    }

    @Test
    fun `should refresh discount cache`() {
        mockMvc.post(PATH) {
            header(CustomHeaders.ADMIN_TOKEN_HEADER, TEST_TOKEN)
        }.andExpect {
            status { isNoContent() }
        }

        verify(cacheService).refresh()
        verifyNoMoreInteractions(cacheService)
    }

    private companion object {
        const val PATH = "/maintenance/cache/refresh"
        const val TEST_TOKEN = "token123"
    }
}
