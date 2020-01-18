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

    LocationManager locationManager;
    LocationListener locationListener;
    Location l;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    String police = "4389289807";
    ImageButton alertImageButton;
    String txtMobile = "5143588519";
    private static final int RECORD_REQUEST_CODE = 100;

    ArrayList<User> users = new ArrayList<>();

    RelativeLayout introLayout, logInLayout, signUpLayout, appLayout;
    TextView logInEmailTextView, logInPasswordTextView;
    TextView signUpFirstNameTextView, signUpLastNameTextView, signUpDateOfBirthTextView,
             signUpPhoneNumberTextView, signUpEmailTextView, signUpPasswordTextView,
             signUpCertificationIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.i("Location", location.toString());
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//        } else{
//
//            l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//
//        }

        //permission for sending SMS
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, RECORD_REQUEST_CODE);
        alertImageButton = findViewById(R.id.alertImageButton);

        //sending SMS containing location
        alertImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("WARNING");
                alertDialog.setMessage("Are you sure there’s an emergency that requires immediate assistance?");
                alertDialog.setCancelable(true);

                //add positive scenario to send coordinates to helper and call 911
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NO",
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
        initializeSignUpTextViews();

        //Date formatting
        final Calendar cal = Calendar.getInstance();
        final EditText signUpDateOfBirthEditText = (EditText) findViewById(R.id.signUpDateOfBirthTextView);
        final DatePickerDialog.OnDateSetListener  date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);

                signUpDateOfBirthEditText.setText(sdf.format(cal.getTime()));

            }
        };

        signUpDateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Phone number fromatting
        EditText signUpPhoneNumberTextView = (EditText) findViewById(R.id.signUpPhoneNumberTextView);
        signUpPhoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    //
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
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else{
                l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (l != null) {
               double lat = l.getLatitude();
               double lon = l.getLongitude();
               String message = "http://www.google.com/maps/place/" + lat + "," + lon;
               SmsManager smgr = SmsManager.getDefault();
               smgr.sendTextMessage(txtMobile, null, message, null, null);
               Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                double lat = 45.52531509;
                double lon = -73.6444297;
                String message = "http://www.google.com/maps/place/" + lat + "," + lon;
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(txtMobile, null, message, null, null);
                Toast.makeText(MainActivity.this, "SMS Sent UnSuccessfully", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.e("The Error", e.toString());
            Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
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

    // Initialize all SignUpLayout Text Views
    public void initializeSignUpTextViews() {
        signUpFirstNameTextView = findViewById(R.id.signUpFirstNameTextView);
        signUpLastNameTextView = findViewById(R.id.signUpLastNameTextView);
        signUpDateOfBirthTextView = findViewById(R.id.signUpDateOfBirthTextView);
        signUpPhoneNumberTextView = findViewById(R.id.signUpPhoneNumberTextView);
        signUpEmailTextView = findViewById(R.id.signUpEmailTextView);
        signUpPasswordTextView = findViewById(R.id.signUpPasswordTextView);
        signUpCertificationIdTextView = findViewById(R.id.signUpCertificationIdTextView);
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
        logInLayout.setVisibility(View.INVISIBLE);
        createUser();
        appLayout.setVisibility(View.VISIBLE);
    }

    public void signUp(View view) {
        signUpLayout.setVisibility(View.INVISIBLE);
        appLayout.setVisibility(View.VISIBLE);
    }

    public void backFromLogIn(View view) {
        logInLayout.setVisibility(View.INVISIBLE);
        introLayout.setVisibility(View.VISIBLE);
    }

    public void backFromSignUp(View view) {
        signUpLayout.setVisibility(View.INVISIBLE);
        introLayout.setVisibility(View.VISIBLE);
    }

    // Creates a User or a Helper depending on if the CertificationID field was filled or not
    public void createUser() {
        String firstName = signUpFirstNameTextView.getText().toString();
        String lastName = signUpLastNameTextView.getText().toString();
        String dateOfBirth = signUpDateOfBirthTextView.getText().toString();
        String phoneNumber = signUpPhoneNumberTextView.getText().toString();
        String email = signUpEmailTextView.getText().toString();
        String password = signUpPasswordTextView.getText().toString();
        String certificationId = signUpCertificationIdTextView.getText().toString();

        if (certificationId.isEmpty()) {
            users.add(new User(firstName, lastName, dateOfBirth, phoneNumber, email, password)); // * ADD TO DATABASE
        } else {
            users.add(new Helper(firstName, lastName, dateOfBirth, phoneNumber, email, password, certificationId)); // * ADD TO DATABASE
        }
    }
}
