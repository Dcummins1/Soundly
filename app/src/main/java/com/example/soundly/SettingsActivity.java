package com.example.soundly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private Button btnChangePassword, btnRemoveUser, signOut, changePassword;
    private EditText password, newPassword;
    private ProgressBar progressBar;
    private SeekBar slide;
    private TextView save;
    private TextView sense;
    private Switch swsave;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private int senser1;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String savefile = "saveFile";
    public static final String sensitivity = "sensitivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth = FirebaseAuth.getInstance();
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnChangePassword = (Button) findViewById(R.id.bChangePassword);
        btnRemoveUser = (Button) findViewById(R.id.bDeleteUser);
        signOut = (Button) findViewById(R.id.bLogout);

        password = (EditText) findViewById(R.id.etPassword);
        newPassword = (EditText) findViewById(R.id.etnewPassword);
        save = (TextView) findViewById(R.id.tvSave);
        sense = (TextView) findViewById(R.id.tvSense);
        slide = (SeekBar) findViewById(R.id.seekBar);
        slide.setProgress(loadSharedPreferences());
        slide.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                int senser = slide.getProgress();
                savePref1(senser);
            }
        });


        swsave = (Switch) findViewById(R.id.swSaveFile);

        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        changePassword = (Button) findViewById(R.id.button5);
        changePassword.setVisibility(View.GONE);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                sense.setVisibility(View.GONE);
                slide.setVisibility(View.GONE);
                swsave.setVisibility(View.GONE);

            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingsActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


//        slide.onProgressChanged(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                int senser = slide.getProgress();
//                savePref1(senser);
////
//
//            }
//
//
//
//
//        });

        swsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean saveFile = swsave.isChecked();
                savePref(saveFile);
                Toast.makeText(SettingsActivity.this, "getting here", Toast.LENGTH_SHORT).show();
//

            }


        });

    }

    private void savePref(boolean saveFile) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(savefile, saveFile); //true saved
        editor.commit();
        Toast.makeText(SettingsActivity.this, "Preference Saved", Toast.LENGTH_SHORT).show();
    }

    private void savePref1(int senser) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(sensitivity, senser); //true saved
        editor.commit();
        Toast.makeText(SettingsActivity.this, "Preference Saved", Toast.LENGTH_SHORT).show();
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private int loadSharedPreferences() {


        if (sharedpreferences != null) {
            senser1 = sharedpreferences.getInt(
                    sensitivity, 0);

        } else {
            senser1 = 1;
        }
        return senser1;

    }
}



