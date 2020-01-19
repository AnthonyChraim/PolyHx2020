package com.cc.polyhx2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int RECORD_REQUEST_CODE = 100;

    DatabaseHelper db;

    String police = "4389289807";
    String txtMobile = "5143588519";
    LocationManager locationManager;
    LocationListener locationListener;
    Location l;

    ArrayList<User> users = new ArrayList<>();
    RelativeLayout introLayout, logInLayout, signUpLayout, appLayout;
    TextView logInEmailTextView, logInPasswordTextView;
    EditText signUpFirstNameTextView, signUpLastNameTextView, signUpDateOfBirthTextView,
            signUpPhoneNumberTextView, signUpEmailTextView, signUpPasswordTextView,
            signUpCertificationIdTextView;
    Button signUpSignButton;
    ImageButton alertImageButton;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission for sending SMS
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS}, RECORD_REQUEST_CODE);
        alertImageButton = findViewById(R.id.alertImageButton);

        //Sending SMS containing location
        alertImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("WARNING");
                alertDialog.setMessage("Are you sure there’s an emergency " +
                                       "that requires immediate assistance?");
                alertDialog.setCancelable(true);

                // Add positive scenario to send coordinates to helper and call 911
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes, call 911",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendSMSMessage();
                                String s = "tel:" + police;
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(s));
                                startActivity(intent); //Put phone permission on the app
                            }
                        });
                alertDialog.show();
            }
        });

        initializeLayouts();
        initializeLogInTextViews();

        //Date formatting
        final Calendar cal = Calendar.getInstance();
        final EditText signUpDateOfBirthEditText = findViewById(R.id.signUpDateOfBirthTextView);
        final DatePickerDialog.OnDateSetListener  date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; // In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

                signUpDateOfBirthEditText.setText(sdf.format(cal.getTime()));
            }
        };

        signUpDateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Phone number formatting
        EditText signUpPhoneNumberTextView = findViewById(R.id.signUpPhoneNumberTextView);
        signUpPhoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    public void sendSMSMessage() {

        try{
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i("Location", location.toString());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else{
                l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (l != null) {
               double lat = l.getLatitude();
               double lon = l.getLongitude();
               String message = "MRGENCY WARNING!\n medical assistance needed at location: " +
                                            "\nhttp://www.google.com/maps/place/" + lat + "," + lon;
               SmsManager smgr = SmsManager.getDefault();
               smgr.sendTextMessage(txtMobile, null, message, null,
                                   null);
            }
            else{
                double lat = 45.52531509;
                double lon = -73.6444297;
                String message = "MRGENCY WARNING!\n medical assistance needed at location: " +
                                            "\nhttp://www.google.com/maps/place/" + lat + "," + lon;
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(txtMobile, null, message, null,
                                    null);
            }
        }
        catch (Exception e){
            Log.e("The Error", e.toString());
        }
    }

    // Initialize all Relative Layouts
    public void initializeLayouts() {
        introLayout = findViewById(R.id.introLayout);
        logInLayout = findViewById(R.id.logInLayout);
        signUpLayout = findViewById(R.id.signUpLayout);
        appLayout = findViewById(R.id.appLayout);
    }

    // Initialize all LogInLayout Text Views
    public void initializeLogInTextViews() {
        logInEmailTextView = findViewById(R.id.logInEmailTextView);
        logInPasswordTextView = findViewById(R.id.logInPasswordTextView);
    }

    // BUTTONS LOGIC
    public void introLogIn(View view) {
        introLayout.setVisibility(View.INVISIBLE);
        logInLayout.setVisibility(View.VISIBLE);
    }

    public void introSignUp(View view) {
        introLayout.setVisibility(View.INVISIBLE);
        signUpLayout.setVisibility(View.VISIBLE);
    }

    public void logIn(View view) {
        db = new DatabaseHelper(this);
        logInEmailTextView = (EditText) findViewById(R.id.logInEmailTextView);
        logInPasswordTextView = findViewById(R.id.logInPasswordTextView);
        String email = logInEmailTextView.getText().toString();
        String password = logInPasswordTextView.getText().toString();
        Boolean Chkemailpassword = db.emailpassword(email, password);

        if(Chkemailpassword == true) {
            Toast.makeText(getApplicationContext(), "Logging In...",
                    Toast.LENGTH_SHORT).show();
            logInLayout.setVisibility(View.INVISIBLE);
            appLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Email or Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view) {
        signUpFirstNameTextView = findViewById(R.id.signUpFirstNameTextView);
        signUpLastNameTextView = findViewById(R.id.signUpLastNameTextView);
        signUpDateOfBirthTextView = findViewById(R.id.signUpDateOfBirthTextView);
        signUpPhoneNumberTextView = findViewById(R.id.signUpPhoneNumberTextView);
        signUpEmailTextView = findViewById(R.id.signUpEmailTextView);
        signUpPasswordTextView = findViewById(R.id.signUpPasswordTextView);
        signUpCertificationIdTextView = findViewById(R.id.signUpCertificationIdTextView);
        signUpSignButton = findViewById(R.id.signUpSignUpButton);
        db = new DatabaseHelper(this);

        String firstName = signUpFirstNameTextView.getText().toString();
        String lastName = signUpLastNameTextView.getText().toString();
        String dateOfBirth = signUpDateOfBirthTextView.getText().toString();
        String phoneNumber = signUpPhoneNumberTextView.getText().toString();
        String email = signUpEmailTextView.getText().toString();
        String password = signUpPasswordTextView.getText().toString();
        String certificationId = signUpCertificationIdTextView.getText().toString();

        if (firstName.equals("") || lastName.equals("") || dateOfBirth.equals("")||
                phoneNumber.equals("") || email.equals("") || password.equals(""))
            Toast.makeText(getApplicationContext(), "Fill in the required fields",
                    Toast.LENGTH_SHORT).show();
        else{
            Boolean checkmail =db.chkmail(email);
            if(checkmail == true){
                Boolean insert = db.insert(email, password, firstName, lastName, dateOfBirth,
                                                phoneNumber, certificationId);
                if(insert == true){
                    Toast.makeText(getApplicationContext(), "Registering...",
                            Toast.LENGTH_SHORT).show();
                    signUpLayout.setVisibility(View.INVISIBLE);
                    appLayout.setVisibility(View.VISIBLE);
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Email already exists",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backFromLogIn(View view) {
        logInLayout.setVisibility(View.INVISIBLE);
        introLayout.setVisibility(View.VISIBLE);
    }

    public void backFromSignUp(View view) {
        signUpLayout.setVisibility(View.INVISIBLE);
        introLayout.setVisibility(View.VISIBLE);
    }

    public void backToMainMenu(View view) {
        appLayout.setVisibility(View.INVISIBLE);
        introLayout.setVisibility(View.VISIBLE);
    }

}