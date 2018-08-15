package socialapp.rathore.com.myapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import socialapp.rathore.com.myapp.Model.User;

public class SignUp extends AppCompatActivity {

    EditText edtName,edtPhone,edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName=(EditText) findViewById(R.id.NameText);
        edtPhone=(EditText)findViewById(R.id.PhoneText1);
        edtPassword=(EditText)findViewById(R.id.PasswordText1);
        btnSignUp=(Button)findViewById(R.id.SignUpBtn);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Signing Up");
                progressDialog.show();
                if( edtName.getText().toString().length() == 0 )
                { edtName.setError( "Name is required!" );
                    progressDialog.dismiss();}
                if( edtPhone.getText().toString().length() == 0 ){
                    edtPhone.setError( "Phone is required!" );  progressDialog.dismiss();}
                if( edtPassword.getText().toString().length() == 0 ){
                    edtPassword.setError( "Password is required!" );  progressDialog.dismiss();}
                else {
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUp.this, "Contact already exists", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up Successful!!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
    }
}
