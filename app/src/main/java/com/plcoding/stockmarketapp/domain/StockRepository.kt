package com.plcoding.stockmarketapp.domain

import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.util.Resources
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote:Boolean,
        query:String
    ): Flow<Resources<List<CompanyListing>>>
}