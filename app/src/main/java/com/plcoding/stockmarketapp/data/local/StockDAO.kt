package com.plcoding.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntity: List<CompanyListingEntity>
    )

    @Query("DELETE from companylistingentity")
    suspend fun clearCompanyListings()

    @Query("""
        SELECT * From companylistingentity
        WHERE LOWER(name) 
        LIKE '%' || LOWER(:query) || '%' OR UPPER(:query)== symbol """)
    suspend fun searchCompanyListing(query:String):List<CompanyListingEntity>
}