package socialapp.rathore.com.myapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import socialapp.rathore.com.myapp.Common.Common;
import socialapp.rathore.com.myapp.Database.Database;
import socialapp.rathore.com.myapp.Model.Orders;
import socialapp.rathore.com.myapp.Model.Request;
import socialapp.rathore.com.myapp.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;
    Button btnClean;

    List<Orders> cart=new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

    //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //Init
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlace=(Button)findViewById(R.id.PlaceOrder);
        btnClean=(Button)findViewById(R.id.cleanCart);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();
            }
        });
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Cart Cleaned", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        loadListFood();
    }
    private void showAlertDialog(){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter Your Address:");

        final EditText edtAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(layoutParams);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.shopping_cart_24dp);
         alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 Request request=new Request(
                         Common.current_user.getPhone(),
                         Common.current_user.getName(),
                         edtAddress.getText().toString(),
                         txtTotalPrice.getText().toString(),
                         cart
                 );
                        //Submit to Firebase
                 //By Using System.CurrentMilli to key
                 requests.child(String.valueOf(System.currentTimeMillis()))
                         .setValue(request);
                 //Deleting the cart
                 new Database(getBaseContext()).cleanCart();
                 Toast.makeText(Cart.this, "Thankyou, Order Placed", Toast.LENGTH_SHORT).show();
                 finish();
             }
         });
         alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
         alertDialog.show();

    }

    private void loadListFood() {
        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //calculate total Price
        int total=0;
        for(Orders orders:cart){
            total+=(Integer.parseInt(orders.getPrice()))*(Integer.parseInt(orders.getQuantity()));
        }
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
