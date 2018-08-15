package socialapp.rathore.com.myapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import socialapp.rathore.com.myapp.Model.Orders;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="eat_itDatabase.db";
    private static final int DB_VER=1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Orders> getCarts(){

        SQLiteDatabase db= getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        String [] sqlSelect={"ProductId","ProductName","Quantity","Price","Discount"};
        String sqlTable="OrderDetails";
        qb.setTables(sqlTable);
        Cursor c=qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Orders> results=new ArrayList<>();
        if(c.moveToNext()){
            do{
                results.add(new Orders(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));

            }while(c.moveToNext());
        }
        return results;

    }

    public void addToCart(Orders orders){
        SQLiteDatabase db=getWritableDatabase();
        String query=String.format("INSERT INTO OrderDetails(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                orders.getProductId(),
                orders.getProductName(),
                orders.getQuantity(),
                orders.getPrice(),
                orders.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM OrderDetails");
        db.execSQL(query);
    }
}
