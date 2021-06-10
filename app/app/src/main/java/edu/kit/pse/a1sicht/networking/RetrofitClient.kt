package edu.kit.pse.a1sicht.networking


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.sql.Timestamp


/**
 * The RetrofitClient class provides the entry point for the communication with the server
 *
 * @author Stanimir Bozhilov, Martin Zahariev, Tihomir Georgiev
 */
class RetrofitClient {

    companion object {
        private var instance: Retrofit? = null
        private val BASE_URL: String = "http://193.196.36.40:9000"

        /**
         * Gets the only instance from this class
         * @return [Retrofit] singleton object
         */
        fun getInstance(): Retrofit? {
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }

            return instance
        }
    }

    /**
     * This is an inner class for date time json converter.
     */
    inner class DateAdapter : TypeAdapter<Timestamp>() {

        /**
         * This method create an json object from a [Timestamp] object.
         */
        @SuppressLint("SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Timestamp?) {
            out.beginObject()
            out.name("iMillis").value(value!!.time)
            out.endObject()
        }

        /**
         * This method makes a [Timestamp] object from json file.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Timestamp {
            var iMillis:Long =0
            `in`.beginObject()
            while(`in`.hasNext()){
                var name = `in`.nextName()
                if(name == "iMillis"){
                    iMillis = `in`.nextLong()
                }else{
                    `in`.skipValue()
                }
            }
            `in`.endObject()
            return Timestamp(iMillis)
        }

    }
}