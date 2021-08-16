package com.yongji.interview.utils

import android.content.Context
import android.util.Log
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yongji.interview.network.RouteData
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream

object JsonUtil {

    fun getRoutes(context: Context): List<RouteData>? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val listType = Types.newParameterizedType(List::class.java, RouteData::class.java)
        val adapter: JsonAdapter<List<RouteData>> = moshi.adapter(listType)

        val file = "drivers.json"

        val myjson = context.assets.open(file).bufferedReader().use{ it.readText()}

        Log.e("yongjiPS", myjson)

        var jsonArray: List<RouteData> = ArrayList()
        myjson?.let {

            jsonArray = getJSONArray(it)
            Log.e("yongjiPS", getJSONArray(it).toString())

        }


        return jsonArray
    }

//    @JsonClass(generateAdapter = true)
//    data class JsonPodcast(
//        val Name: String,
//        val Description: String,
//        val Artwork: String,
//        val URL: String
//    )


    private fun getJSONArray(txtJson: String): List<RouteData>{

         var driverArray : ArrayList<String> = ArrayList()
         var addressArray : ArrayList<String> = ArrayList()


        var jObj : JSONObject = JSONObject(txtJson)

        var jsonArray: JSONArray = jObj.getJSONArray("drivers")
        var jsonArrayAddress: JSONArray = jObj.getJSONArray("shipments")


        for(j in 0 until jsonArray.length()) {
            driverArray.add(jsonArray.getString(j))
        }

        for(j in 0 until jsonArrayAddress.length()) {
            addressArray.add(jsonArrayAddress.getString(j))
        }

//        println(driverArray.permutations())
        var bestRoute: MutableList<RouteData> = ArrayList()
        var max = 0.0
        var ssSum = 0.0

        driverArray.permutations().forEach {
            val resultReduce =  it.zip(addressArray) { a, b -> "$a+$b" }
//            println(resultReduce.toString())
            var ssSum :Double = 0.0

            resultReduce.forEach{

                var driver = it.split("+")[0]
                var address = it.split("+")[1]
                var ss: Double


                if(address.length % 2 == 0) {
                    ss = vowels(driver, "vowels").toDouble()
//                    println(driver)
//                    println(driver.length)
//                    println(address)
//                    println(address.length)
//                    println(ss)
                    if(gcd(driver.length,address.length) > 1) {
                        ss = ss * 1.5
//                        println(ss)
                    }
                    ssSum += ss
//                    println("ssSum" + ssSum.toString())

                } else {
                    ss = vowels(driver, "consonants").toDouble()
//                    println(driver)
//                    println(driver.length)
//                    println(address)
//                    println(address.length)
//                    println(ss)
                    if(gcd(driver.length,address.length) > 1) {
                        ss = ss * 1.5
//                        println(ss)
                    }

                    ssSum += ss
//                    println(ssSum)
                }


            }

//            println(max)
//            println(ssSum)

            if(ssSum > max) {
                max = ssSum
//                bestRoute = resultReduce

                bestRoute.clear()
                resultReduce.forEach{
                    var driver = it.split("+")[0]
                    var address = it.split("+")[1]
                    val hd = RouteData(driver, address )

                    bestRoute.add(hd)
                    Log.e("tesla",driver)
                    Log.e("tesla",address)
                    Log.e("tesla", bestRoute.toString())
                }
//                println(max)
//                println("best" + ssSum)
                Log.e("tesla","bestRoute")
                Log.e("tesla", bestRoute.toString())

            } else {
                ssSum = 0.0
            }
        }


        return bestRoute
    }

    fun <V> List<V>.permutations(): List<List<V>> {
        val retVal: MutableList<List<V>> = mutableListOf()

        fun generate(k: Int, list: MutableList<V>) {
            // If only 1 element, just output the array
            if (k == 1) {
                retVal.add(list.toList())
            } else {
                for (i in 0 until k) {
                    generate(k - 1, list)
                    if (k % 2 == 0) {
                        list.swap(i,k-1)
                    } else {
                        list.swap(0,k-1)
                    }
                }
            }
        }

        generate(this.count(), this.toMutableList())
        return retVal
    }


    fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1] // 'this' corresponds to the list
        this[index1] = this[index2]
        this[index2] = tmp
    }

    public fun gcd(a:Int,b:Int) : Int {
        var n1:Int  = a
        var n2:Int  = b
        while (n1 != n2) {
            if (n1 > n2)
                n1 -= n2
            else
                n2 -= n1
        }
        return n1
    }

    private fun vowels(s: String, vowelsOrConsonants :String) : Int {

        var vowels = 0
        var consonants = 0

        var line:String = s.lowercase()

        for (i in 0..line.length - 1) {
            val ch = line[i]
            if (ch == 'a' || ch == 'e' || ch == 'i'
                || ch == 'o' || ch == 'u') {
                vowels++
            } else if (ch in 'a'..'z') {
                consonants++
            }

        }

        if(vowelsOrConsonants == "vowels") {
            return vowels
        } else {
            return consonants
        }

    }


}