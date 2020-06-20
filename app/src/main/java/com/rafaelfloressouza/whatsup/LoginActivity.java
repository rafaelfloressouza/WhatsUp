package com.rafaelfloressouza.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    // Constants
    final private int TIMEOUT_DURATION = 60;

    // Variable used to control the country ISO spinner
    CountryCodePicker mSpinner;

    // Variables used to connect layout with code
    private Button mVerifyButton;
    private AutoCompleteTextView mNumberTextView;
    private AutoCompleteTextView mCodeTextView;
    private String mVerificationId; // Stores the code sent by Firebase.

    // Variable to access PhoneAuthProvider service
    private PhoneAuthProvider mPhoneAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks; // Called after phone is verified (as a callback)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Connecting code with respective elements in our layout
        mVerifyButton = (Button) findViewById(R.id.send_verification_button);
        mNumberTextView = (AutoCompleteTextView) findViewById(R.id.phone_number_field_view);
        mCodeTextView = findViewById(R.id.verification_code_field_view);

        // Connecting code with the spinner.
        mSpinner = (CountryCodePicker) findViewById(R.id.country_code_spinner);
        mSpinner.registerCarrierNumberEditText(mNumberTextView);
        mSpinner.setNumberAutoFormattingEnabled(true);


        FirebaseApp.initializeApp(this);

        //Checking if the user is already logged in. If so, we don't do the verification and we go directly to the dashboard.
        userIsLoggedIn();

        // Initializing the PhoneAuthProvider using static method in class.
        mPhoneAuth = PhoneAuthProvider.getInstance();


        // Callback of Phone Number authentication
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                // If the verification was completed succesfully.
                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Error verifying phone number\nMake sure you have reception.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String issuedVerificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(issuedVerificationCode, forceResendingToken);

                // As soon as an sms with the verification code is sent do the following:
                mVerificationId = issuedVerificationCode; // Saving the verification code sent by Firebase.
                mCodeTextView.setEnabled(true);
                mVerifyButton.setText("Insert Code");
            }
        };
    }

    public boolean validateInputs() {


        // Reset all errors displayed on the text views.
        mNumberTextView.setError(null);
        mCodeTextView.setError(null);

        String phoneNumber = mNumberTextView.getText().toString();
        String verificationCode = mCodeTextView.getText().toString();

        boolean isValid = false;
        boolean errorDetected = false;
        View focusView = null;

        // Variables used to customize the error message
        int errorColor;
        final int version = Build.VERSION.SDK_INT;

        //Get the defined errorColor from color resource depending on SDK version
        if (version >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.error_red);
        } else {
            errorColor = getResources().getColor(R.color.error_red);
        }


        // Checking that phone number field is not empty.
        if (TextUtils.isEmpty(phoneNumber)) {

            // Customizing error messsage.
            String errorString =  "Phone Number required";
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            mNumberTextView.setError(spannableStringBuilder);
            focusView = mNumberTextView;
            errorDetected = true;
        }

        if(!mSpinner.isValidFullNumber()){ // If numbers is invalid for the country selected.
            Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            focusView = mNumberTextView;
            errorDetected = true;
        }


        // Checking that verification code field is not empty.
        if (TextUtils.isEmpty(verificationCode) && mVerificationId != null) {


            // Customizing error message.
            String errorString = "Verification Code Required";  // Your custom error message.
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(errorString);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, errorString.length(), 0);
            mCodeTextView.setError(spannableStringBuilder);
            focusView = mCodeTextView;
            errorDetected = true;
        }

        if (!errorDetected)
            isValid = true;

        return isValid;
    }

    public void verificationButton(View view) {

        if (validateInputs()) { // If inputs are valid:

            if (mVerificationId != null) { // If a verification code was issued by Firebase.
                verifyPhoneNumberWithCode();
            } else { // There was no verification code issued, so we start new verification.
                performPhoneNumberVerification();
            }

        }
    }

    private void verifyPhoneNumberWithCode() {
        String code = mCodeTextView.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithCredential(credential);
    }

    private void performPhoneNumberVerification() {

        // Verifying a new phone number for which no verification code has been issued.
        String phoneNumber =  mSpinner.getFullNumberWithPlus();
        mPhoneAuth.verifyPhoneNumber(phoneNumber, TIMEOUT_DURATION, TimeUnit.SECONDS, this, mCallbacks);
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {


        // Using credential provided (phone number) to attempt to sign in.
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) { // If signing process with the respective credential is successful and inputs are valid


                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());

                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();
//                                    userMap.put("name", "unknown");
                                    userMap.put("phone", user.getPhoneNumber());
                                    mDatabase.updateChildren(userMap);
                                }
                                userIsLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });
                    }

                }
            }
        });
    }

    private void userIsLoggedIn() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) { // Signing into the user's dashboard.

            Toast.makeText(LoginActivity.this, "Logging In...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                        finish();
                    } catch (Exception e) {
                        Log.d("WhatsUp", "error: " + e.toString());
                    }
                }
            }, 1000);

        }
    }
}
