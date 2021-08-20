package com.dicoding.picodiploma.store.network

import com.dicoding.picodiploma.store.data.item.InsertItemResponse
import com.dicoding.picodiploma.store.data.item.UpdateItemResponse
import com.dicoding.picodiploma.store.data.login.ResponseLogin
import com.dicoding.picodiploma.store.data.viewItem.Item
import retrofit2.Call
import retrofit2.http.*

interface ApiClient {

    @FormUrlEncoded
    @POST("api/login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("api/insert.php")
    fun insertItem(
        @Field("id_user") id_user: Int?,
        @Field("item_name") item_name: String,
        @Field("item_price") item_price: Int,
        @Field("item_stock") item_stock: Int
    ): Call<InsertItemResponse>

    @FormUrlEncoded
    @POST("api/update.php")
    fun updateItem(
        @Field("toko") toko:Int,
        @Field("item_stock") item_stock: Int,
        @Field("item_id") item_id: Int
    ) : Call<UpdateItemResponse>

    @GET("api/readItem.php")
    fun readItem(
        @Query("toko") toko: Int
    ): Call<ArrayList<Item>>

}