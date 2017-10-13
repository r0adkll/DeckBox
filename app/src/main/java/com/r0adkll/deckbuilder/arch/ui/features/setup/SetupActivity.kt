package com.r0adkll.deckbuilder.arch.ui.features.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksActivity
import com.r0adkll.deckbuilder.util.extensions.snackbar
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber


class SetupActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    val RC_SIGN_IN = 100

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var googleClient: GoogleApiClient? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        setupClient()
        action_signin.setOnClickListener {
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }


    override fun setupComponent(component: AppComponent) {

    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Timber.e("onConnectionFailed(${p0.errorCode} :: ${p0.errorMessage}")
    }


    private fun setupClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }


    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val acct = result.signInAccount
            val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(DecksActivity.createIntent(this@SetupActivity))
                            finish()
                        }
                        else {
                            snackbar("Authentication failed")
                        }
                    }
        }
        else {
            snackbar("Authenticated failed")
        }
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SetupActivity::class.java)
    }
}