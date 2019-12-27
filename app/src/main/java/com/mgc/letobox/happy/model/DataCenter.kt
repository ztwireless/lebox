package com.mgc.letobox.happy.model

import android.app.Application
import android.util.Log
import com.leto.game.base.util.BaseAppUtil
import com.mgc.letobox.happy.floattools.MGCService
import com.mgc.letobox.happy.util.LeBoxConstant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import okhttp3.logging.HttpLoggingInterceptor


class DataCenter {
    companion object {
        private var TEST_ENV: Boolean = false
        fun init(app: Application) {
            TEST_ENV = BaseAppUtil.getMetaBooleanValue(app, "MGC_TEST_ENV")
        }

        val TAG = DataCenter::class.java.simpleName
        fun buildRetrofit(): Retrofit {
            Log.i(TAG, "buildRetrofit $TEST_ENV")
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                //打印retrofit日志
                if (TEST_ENV) Log.i("RetrofitLog", "retrofitBack = $message")
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            return Retrofit.Builder()
                    .baseUrl(if (TEST_ENV) LeBoxConstant.MGCServerUrlDev else LeBoxConstant.MGCServerUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        fun obtainFcmConfig(channel_id: Int, open_token: String): FcmConfig? {
            val service = buildRetrofit().create(MGCService::class.java)
            try {
                val response = service.obtainFcmConfig(channel_id, open_token).execute().body()
                if (response.isSuccess()) {
                    return response.data
                }
            } catch (e: Exception) {
            }
            return null
        }

        fun requestCertification(mobile: String, open_token: String): Certification? {
            val service = buildRetrofit().create(MGCService::class.java)
            try {
                val response = service.requestCertification(mobile, open_token).execute().body()
                if (response.isSuccess()) {
                    return response.data
                }
            } catch (e: Exception) {
            }
            return null
        }

        fun requestIdCard(name: String, cardno: String): IdCard? {
            val service = buildRetrofit().create(MGCService::class.java)
            val response = service.requestIdCard(name, cardno).execute().body()
            if (response.isSuccess()) {
                return response.data
            }
            return null
        }
    }
}