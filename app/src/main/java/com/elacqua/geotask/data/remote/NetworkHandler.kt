package com.elacqua.geotask.data.remote

import com.elacqua.geotask.data.remote.dao.DirectionService
import com.elacqua.geotask.data.remote.dao.OptimizationService
import com.elacqua.geotask.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHandler {

    @Volatile
    private lateinit var instance: Retrofit

    private fun getRetrofit(): Retrofit {
        synchronized(this) {
            if (!NetworkHandler::instance.isInitialized) {
                val builder = OkHttpClient.Builder()
                builder.addInterceptor { chain ->
                    val request =
                        chain.request().newBuilder().addHeader(
                            "Authorization",
                            Constants.ORS_API_KEY
                        ).build()
                    chain.proceed(request)
                }
                val client = builder.build()

                instance = Retrofit.Builder()
                    .baseUrl(Constants.OPEN_ROUTE_SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return instance
        }
    }

    fun getDirectionService(): DirectionService {
        return if (NetworkHandler::instance.isInitialized) {
            instance.create(DirectionService::class.java)
        } else {
            getRetrofit().create(DirectionService::class.java)
        }
    }

    fun getOptimizationService() : OptimizationService {
        return if (NetworkHandler::instance.isInitialized) {
            instance.create(OptimizationService::class.java)
        } else {
            getRetrofit().create(OptimizationService::class.java)
        }
    }
}