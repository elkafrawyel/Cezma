package com.cezma.app.ui.mainActivity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.cezma.app.R
import com.cezma.app.ui.splashActivity.SplashActivity
import com.cezma.app.utiles.*
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.koraextra.app.utily.observeEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*

class MainActivity : AppCompatActivity() , GraphRequest.GraphJSONObjectCallback{

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }


    var callbackManager: CallbackManager? = null
    private var faceBookAccessToken: AccessToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //============================= FaceBook ========================================
        callbackManager = CallbackManager.Factory.create()
        login_button.setPermissions(listOf("email", "public_profile"))
        LoginManager.getInstance().logOut()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if (isLoggedIn) {
            LoginManager.getInstance().logOut()
        }

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                if (isLoggedIn) {
                    loadUserProfile(accessToken)
                } else {
                    toast("Facebook login failed")
                }
            }

            override fun onCancel() {
                toast("Facebook login Cancelled")
            }

            override fun onError(exception: FacebookException) {
                toast("Facebook login failed$exception")
            }
        })
    }

    fun loginWithFacebook() {
        login_button.performClick()
    }

    private fun loadUserProfile(accessToken: AccessToken?) {
        val graphRequest = GraphRequest.newMeRequest(accessToken, this)
        faceBookAccessToken = accessToken
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

    override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
        try {
            val firstName:String = `object`!!.get("first_name").toString()
            val lastName:String = `object`.get("last_name").toString()
            val email:String = `object`.get("email").toString()
//            val id:String = `object`.get("id").toString()
//            val imageUrl:String = "https://graph.facebook.com/$id/picture?type=normal"

            val bundle = Bundle()
            bundle.putString("firstName", firstName)
            bundle.putString("lastName", lastName)
            bundle.putString("email", email)
            this.findNavController(R.id.fragment).navigate(
                R.id.signUpFragment,
                bundle
            )

        } catch (ex: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            callbackManager?.onActivityResult(requestCode, resultCode, data)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun generateSSHKey(context: Context){
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.getEncoder().encode(md.digest()))
                Log.i("AppLog key", "$hashKey")
            }
        } catch (e: Exception) {
            Log.e("AppLog", "error:", e)
        }

    }
}
