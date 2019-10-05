package friendlyitsolution.com.firebase_db;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etmsg;

   static  DatabaseReference ref;
    FirebaseDatabase db;
    RecyclerView recy;
    MyAdpter adpter;
    List<DataModel> data;


    Map<String,DataModel> maps;

    static ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        maps=new HashMap<>();
        pd=new ProgressDialog(MainActivity.this);
        pd.setMessage("please wait...");
        pd.setCancelable(false);


        data=new ArrayList<>();
        recy=findViewById(R.id.recy);
        adpter=new MyAdpter(data);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recy.setLayoutManager(mLayoutManager);

        recy.setAdapter(adpter);

        etmsg=findViewById(R.id.etmsg);

        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              saveData();
            }
        });
      //  getData();

        getDataFromChildListener();
    }



    void getDataFromChildListener()
    {


        ref.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Map<String,String> temp=(Map<String, String>)dataSnapshot.getValue();

                DataModel d=new DataModel(dataSnapshot.getKey(),temp.get("msg"),temp.get("date"),temp.get("time"),temp.get("read"));

                maps.put(dataSnapshot.getKey(),d);
                data.add(d);
                adpter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DataModel d=maps.get(dataSnapshot.getKey());
                Map<String,String> temp=(Map<String, String>)dataSnapshot.getValue();

                d.read=temp.get("read");
                adpter.notifyDataSetChanged();



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                DataModel d=maps.get(dataSnapshot.getKey());
                data.remove(d);
                maps.remove(d);
                adpter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    void saveData()
    {

        pd.show();
        Map<String,String> data=new HashMap<>();
        data.put("msg",etmsg.getText().toString());
        data.put("read","false");

        String pattern = "dd MMM, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        String pattern1 = "hh:mm a";
        SimpleDateFormat simpletimeFormat = new SimpleDateFormat(pattern1);
        String time = simpletimeFormat.format(new Date());

        data.put("time",time);
        data.put("date",date);



        ref.child("chat").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                pd.dismiss();
                etmsg.setText("");

            }
        });







    }

}
