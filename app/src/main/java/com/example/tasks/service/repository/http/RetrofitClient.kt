package com.example.tasks.service.repository.http

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private lateinit var mRetrofit: Retrofit
        private val mBaseUrl = "http://devmasterteam.com/CursoAndroidAPI/"
        private val mClient = OkHttpClient.Builder()
        private val mFactory = GsonConverterFactory.create()

        private var personKey = ""
        private var tokenKey = ""

        private fun getRetrofitInstance(): Retrofit {
            mClient.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain
                        .request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                        .build()
                    return chain.proceed(request)
                }
            })
            if (!::mRetrofit.isInitialized) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .client(mClient.build())
                    .addConverterFactory(mFactory)
                    .build()
            }
            return mRetrofit
        }

        fun addHeader(token: String, personKey: String) {
            this.tokenKey = token
            this.personKey = personKey
        }

        fun <T> createService(service: Class<T>): T {
            return this.getRetrofitInstance().create(service)
        }
    }
}