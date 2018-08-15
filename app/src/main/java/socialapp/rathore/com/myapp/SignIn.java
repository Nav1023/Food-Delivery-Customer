package socialapp.rathore.com.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import socialapp.rathore.com.myapp.Common.Common;
import socialapp.rathore.com.myapp.Model.User;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignIn";
    Button btnSignIn;
    EditText edtPhone, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnSignIn=(Button)findViewById(R.id.SignInBtn);
        edtPhone=(EditText)findViewById(R.id.PhoneText);
        edtPassword=(EditText)findViewById(R.id.PasswordText);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog=new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Logging In");
                progressDialog.show();

                Log.d(TAG, "Outside setOnclicklistener");
                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG, "onDataChange: Inside Datachange");
                        try {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                progressDialog.dismiss();

                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString());//Set Phone
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Intent homeintent=new Intent(SignIn.this,Home.class);
                                    Common.current_user=user;
                                    startActivity(homeintent);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Failed to Login", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "User Doesn't Exists", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(SignIn.this, " "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "onDataChange: End of  Datachange");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }
        });
    }
}
