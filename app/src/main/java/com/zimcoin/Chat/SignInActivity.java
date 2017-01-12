package com.zimcoin.Chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private Button _registerOrSignOut;
    private EditText _email;
    private EditText _password;
    private TextView _signInOrChat;
    private ProgressDialog _progressDialog;
    private FirebaseAuth _firebaseAuth;
    private Boolean _signedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        _registerOrSignOut = (Button) findViewById(R.id.btnRegister);
        _email = (EditText) findViewById(R.id.email_s);
        _password = (EditText) findViewById(R.id.password_s);
        _signInOrChat = (TextView) findViewById(R.id.btnSignIn);
        _progressDialog = new ProgressDialog(this);
        _firebaseAuth = FirebaseAuth.getInstance();

        _registerOrSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(_signedIn) { //user signed in
                        signOutUser();
                }
                else
                {
                    Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        _signInOrChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User already signed in, go to the chat room

                if(_signedIn)
                {
                    Intent intent1 = new Intent(SignInActivity.this, ChatRoomActivity.class);
                    intent1.putExtra("room_name", "EXFestAlgeria");
                    _email.setText("");
                    _password.setText("");
                    startActivity(intent1);
                }else
                {
                    signInUser();
                }
            }
        });
    }

    public void signOutUser() {
        //Validation Ok. Show a progressDialog
        _progressDialog.setMessage("Sign out ...");
        _progressDialog.show();
        //send email and password to firebase (we will attach a listner (optional) to track the completion of the registration)
        _firebaseAuth.signOut();
        _progressDialog.dismiss();
        _registerOrSignOut.setText("REGISTER");
        _signInOrChat.setText("SIGN IN");
        _signedIn = false;
        Toast toast = Toast.makeText(SignInActivity.this, "Successfully signed out", Toast.LENGTH_SHORT);
        toast.show();
    }

    //==============================================//
    //Register user
    public void signInUser() {

        String email= _email.getText().toString().trim();
        String password = _password.getText().toString().trim();

        if(TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".com"))
        {//empty email provided
            Toast toast = Toast.makeText(this,"Valid email address is required",Toast.LENGTH_SHORT);
            toast.show();
        }else if(TextUtils.isEmpty(password) || password.length()<6)
        {//empty password provided
            Toast toast = Toast.makeText(this,"Valid password is required",Toast.LENGTH_SHORT);
            toast.show();
        }else
        {
            //Validation Ok.
            _progressDialog.setMessage("Sign in ...");
            _progressDialog.show();
            //send email and password to firebase (we will attach a listner (optional) to track the completion of the registration)
            _firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                //user is successfully registered and loged in
                                _progressDialog.dismiss();
                                _registerOrSignOut.setText("SIGN OUT");
                                _signInOrChat.setText("CHAT!");
                                Intent intent1 = new Intent(SignInActivity.this, ChatRoomActivity.class);
                                intent1.putExtra("room_name", "EXFestAlgeria");
                                _email.setText("");
                                _password.setText("");
                                _signedIn = true;
                                startActivity(intent1);
                            }else
                            {
                                _progressDialog.dismiss();
                                Toast toast = Toast.makeText(SignInActivity.this, "Could not sign in. Please try again", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
        }

    }

    public void signInUser(String newEmail, String newPassword) {

        String email= newEmail;
        String password = newPassword;

        if(TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".com"))
        {//empty email provided
            Toast toast = Toast.makeText(this,"Valid email address is required",Toast.LENGTH_SHORT);
            toast.show();
        }else if(TextUtils.isEmpty(password) || password.length()<6)
        {//empty password provided
            Toast toast = Toast.makeText(this,"Valid password is required",Toast.LENGTH_SHORT);
            toast.show();
        }else
        {
            //Validation Ok.
            _progressDialog.setMessage("Sign in ...");
            _progressDialog.show();
            //send email and password to firebase (we will attach a listner (optional) to track the completion of the registration)
            _firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                //user is successfully registered and loged in
                                _progressDialog.dismiss();
                                _registerOrSignOut.setText("SIGN OUT");
                                _signInOrChat.setText("CHAT!");
                                Intent intent1 = new Intent(SignInActivity.this, ChatRoomActivity.class);
                                intent1.putExtra("room_name", "EXFestAlgeria");
                                _email.setText("");
                                _password.setText("");
                                startActivity(intent1);
                            }else
                            {
                                _progressDialog.dismiss();
                                Toast toast = Toast.makeText(SignInActivity.this, "Could not sign in. Please try again", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
        }

    }

}
