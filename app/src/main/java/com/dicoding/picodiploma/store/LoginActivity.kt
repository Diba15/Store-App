package com.dicoding.picodiploma.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dicoding.picodiploma.store.data.login.ResponseLogin
import com.dicoding.picodiploma.store.databinding.ActivityLoginActivityBinding
import com.dicoding.picodiploma.store.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginActivityBinding
    private var username: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            username = binding.usernameEditText.text?.trim().toString()
            password = binding.passwordEditText.text?.trim().toString()

            if (username == "") {
                binding.registerTl.error = "Username tidak boleh kosong!"
            } else if(password == "") {
                binding.passRegisTl.error = "Password tidak boleh kosong!"
            } else {
                getData()
            }
        }
    }

    private fun getData() {
        val api = RetrofitClient().getInstance()
        with(api) {
            login(username, password).enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.response == true) {
                            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG)
                                .show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            val bundle = Bundle()
                            with(bundle) {
                                putInt("id", response.body()!!.sanstore.id_user)
                                putString("username", response.body()!!.sanstore.username)
                                putString("password", response.body()!!.sanstore.password)
                            }
                            intent.putExtras(bundle)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    t.message?.let { Log.e("ErrorRetro", it) }
                }
            })
        }
    }
}