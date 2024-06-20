package com.example.internet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class IP(
    var city: String? = null,
    var country: String? = null,
    var loc: String? = null,
    var postal: String? = null,
    var timezone: String? = null,
)

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

interface APIServ{
    @GET("{ip}?token=14e2211c00a876")
    fun getData(@Path("ip") ip: String): Call<IP>;
}

object Common {
    private val BASE_URL = "https://ipinfo.io/"
    val retrofitServ: APIServ
        get() = RetrofitClient.getClient(BASE_URL).create(APIServ::class.java)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mService = Common.retrofitServ

        findViewById<Button>(R.id.search).setOnClickListener{
            val ipData = findViewById<TextInputEditText>(R.id.ip).text.toString()
            if (ipData != "") {
                mService.getData(ipData).enqueue(object : Callback<IP> {
                    override fun onFailure(p0: Call<IP>, p1: Throwable) {
                        Log.d("NOT YEAP:(", "F")
                    }

                    override fun onResponse(p0: Call<IP>, p1: Response<IP>) {
                        val ans = p1.body()
                        if (ans != null){
                            findViewById<TextView>(R.id.city).setText(ans.city.toString())
                            findViewById<TextView>(R.id.country).setText(ans.country.toString())
                            findViewById<TextView>(R.id.loc).setText(ans.loc.toString())
                            findViewById<TextView>(R.id.postal).setText(ans.postal.toString())
                            findViewById<TextView>(R.id.timezone).setText(ans.timezone.toString())
                        }
                    }
                })
            }
        }
    }
}