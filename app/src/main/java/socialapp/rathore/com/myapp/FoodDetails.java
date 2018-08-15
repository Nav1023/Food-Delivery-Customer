package socialapp.rathore.com.myapp;

import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import socialapp.rathore.com.myapp.Database.Database;
import socialapp.rathore.com.myapp.Model.Food;
import socialapp.rathore.com.myapp.Model.Orders;

public class FoodDetails extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        //Init Views
        numberButton=(ElegantNumberButton)findViewById(R.id.number_button);
        btnCart=(FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Orders(
                        foodId,
                        food.getName(),
                        numberButton.getNumber(),
                        food.getPrice(),
                        food.getDiscount()
                ));
                Toast.makeText(FoodDetails.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }

        });

        //Firebase
        database=FirebaseDatabase.getInstance();
        foods=database.getReference("Foods");

        food_name=(TextView)findViewById(R.id.foodName);
        food_price=(TextView)findViewById(R.id.foodPrice);
        food_description=(TextView)findViewById(R.id.foodDescription);

        food_image=(ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppBar);

        //Geting value from intent
        if(getIntent()!=null){
            foodId=getIntent().getStringExtra("FoodId");
        }

        if(!foodId.isEmpty() && foodId!=null){
          
            loadListFood(foodId);
        }

    }

    private void loadListFood(final String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 food=dataSnapshot.getValue(Food.class);

                //Setting the Image
                Picasso.with(getBaseContext()).load(food.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(food.getName());
                food_price.setText(food.getPrice());
                food_description.setText(food.getDescription());
                food_name.setText(food.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
