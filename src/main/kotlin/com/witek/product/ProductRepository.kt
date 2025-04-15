package com.witek.product

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ProductRepository : CrudRepository<Product, UUID>
