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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private Button _signUp ;
    private EditText _userName;
    private EditText _email;
    private EditText _password;
    private EditText _email_conf;
    private EditText _password_conf;
    private ProgressDialog _progressDialog;
    private FirebaseAuth _firebaseAuth;
    private Boolean _registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _signUp = (Button) findViewById(R.id.btnSignUp);
        _userName = (EditText) findViewById(R.id.userName_r);
        _email = (EditText) findViewById(R.id.email_r);
        _password = (EditText) findViewById(R.id.password_r);
        _email_conf = (EditText) findViewById(R.id.email_r_conf);
        _password_conf = (EditText) findViewById(R.id.password_r_conf);
        _progressDialog = new ProgressDialog(this);
        _firebaseAuth = FirebaseAuth.getInstance();

        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(!_registered)
                    {
                        registerUser();
                    }else
                    {
                        Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                    }
            }
        });
    }

    // Register user
    public void registerUser() {
        String email= _email.getText().toString().trim();
        String password = _password.getText().toString().trim();
        String email_conf= _email_conf.getText().toString().trim();
        String password_conf = _password_conf.getText().toString().trim();
        final String username = _userName.getText().toString().trim();


        if(TextUtils.isEmpty(username))
        {//No username provided
            Toast toast = Toast.makeText(this,"Username is required",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(TextUtils.isEmpty(email) || !email.contains("@") || !email.contains(".com"))
        {
            Toast toast = Toast.makeText(this,"Valid email address is required",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (password.length()<6)
        {//short password provided
            Toast toast = Toast.makeText(this,"Valid password is required",Toast.LENGTH_SHORT);
            toast.show();

        }else
        {
            if(!email.equals(email_conf) && !password.equals(password_conf))
            {
                Toast toast = Toast.makeText(RegisterActivity.this, "Email address doesn't match", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(!password.equals(password_conf))
            {
                Toast toast = Toast.makeText(RegisterActivity.this, "Password doesn't match", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (!email.equals(email_conf))
            {
                Toast toast = Toast.makeText(RegisterActivity.this, "Email address  and password doesn't match", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                //Validation Ok. Show a progressbar
                _progressDialog.setMessage("Registring User...");
                _progressDialog.show();
                //send email and password to firebase (we will attach a listner (optional) to track the completion of the registration)

                _firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    //user is successfully registered and loged in
                                    _progressDialog.dismiss();
                                    //To add user name we need to update the profile on firebase
                                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username) // we can add a picutre
                                            .build();
                                    FirebaseUser user = _firebaseAuth.getCurrentUser();
                                    user.updateProfile(profile);
                                    Toast toast = Toast.makeText(RegisterActivity.this, "Successfully registered! Please sign in", Toast.LENGTH_LONG);
                                    toast.show();
                                    _registered = true;
                                    _signUp.setText("GO BACK");

                                } else {
                                    _progressDialog.dismiss();
                                    Toast toast = Toast.makeText(RegisterActivity.this, "Could not register. Please try again", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });

            }

        }
    }
}
