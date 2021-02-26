package com.example.bank_app_dopham

import android.provider.MediaStore.Video
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class APIConnection {
    //private val URL : String = "https://60102f166c21e10017050128.mockapi.io/labbbank"
    //public val URL_CONFIG : String = URL+"/config/"
    //public val URL_ACCOUNTS : String = URL+"/accounts" //https://60102f166c21e10017050128.mockapi.io/labbbank/accounts"


    public fun getTLS(URL : String, isDebuging: Boolean = true)  : String {
        var value : String = ""
        val threadTLS = thread {
            try {
                val url = URL(URL)
                val urlConnection : HttpURLConnection = url.openConnection() as HttpURLConnection //HTTPS Connection , TLS
                urlConnection.connect()
                val inputStream: InputStream = urlConnection.getInputStream()
                val inputAsString = inputStream.bufferedReader().use { it.readText() }


                if(isDebuging) {
                    Log.d("debugConsole", inputAsString.toString())
                }
                value = inputAsString
            }
            catch (e: Exception)
            {
            }
        }
        threadTLS.join() //wait for the end of the thread
        return value
        //val inputStream: InputStream = urlConnection.getInputStream()
        //Log.d("debugConsole", urlConnection().toString())
        //copyInputStreamToOutputStream(inputStream, System.out)
    }

    public fun fromJSONToAccounts(json : String):  List<Account>
    {
        val gson = Gson()
        var accounts : List<Account> = listOf()
        try {
            val itemType = object : TypeToken<List<Account>>() {}.type
            accounts = gson.fromJson<List<Account>>(json, itemType) //from json to class
        }
        catch (e: java.lang.Exception) {
            Log.d("debugConsole", e.toString())
        }
        return accounts
    }
    public fun fromJSONToUser(json : String) : User?{
        var user : User? = null
        try {
            user = Gson().fromJson(json, User::class.java) //from json to class
        }
        catch (e: java.lang.Exception) {
            Log.d("debugConsole", e.toString())
        }
        return user
    }
}