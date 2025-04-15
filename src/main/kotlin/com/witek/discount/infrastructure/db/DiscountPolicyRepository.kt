package com.witek.discount.infrastructure.db

import org.springframework.data.repository.CrudRepository
import java.util.UUID

internal interface DiscountPolicyRepository : CrudRepository<DiscountPolicyEntity, UUID>