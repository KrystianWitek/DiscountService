package com.witek.product

import com.witek.product.model.Product
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ProductRepository : CrudRepository<Product, UUID>
