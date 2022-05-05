package com.plcoding.stockmarketapp.data.local

import com.opencsv.CSVReader
import com.plcoding.stockmarketapp.data.mappers.toCompanyListing
import com.plcoding.stockmarketapp.data.remote.StockAPI
import com.plcoding.stockmarketapp.domain.StockRepository
import com.plcoding.stockmarketapp.domain.model.CompanyListing
import com.plcoding.stockmarketapp.util.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val api : StockAPI,
    private val db: StockDatabase
): StockRepository  {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resources<List<CompanyListing>>> {
       return  flow {
           emit(Resources.Loading(true))
            val localListings = dao.searchCompanyListing(query)
           emit(Resources.Success(
               data = localListings.map { it.toCompanyListing() }
           ))

           val isDBEmpty= localListings.isEmpty() && query.isBlank()
           val shouldJustLoadFromCache = !isDBEmpty && !fetchFromRemote
           if (shouldJustLoadFromCache){
               emit(Resources.Loading(false))
               return@flow
           }
           val remoteListings = try {
                val response = api.getListings()
               val csvReader = CSVReader(InputStreamReader(response.byteStream()))
           }catch (e: IOException){
               e.printStackTrace()
               emit(Resources.Error("Couldn't load data"))
           }catch (e: HttpException){
                e.printStackTrace()
               emit(Resources.Error("Couldn't load data"))
           }

       }

    }
}