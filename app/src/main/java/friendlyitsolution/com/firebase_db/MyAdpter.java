package friendlyitsolution.com.firebase_db;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyViewHolder> {

    private List<DataModel> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView etmsg,etdate,ettime;
        Button btnread,btndelete;
        RelativeLayout rel;

        public MyViewHolder(View view) {
            super(view);

            rel=view.findViewById(R.id.rel);
            etmsg=view.findViewById(R.id.etmsg);
            etdate=view.findViewById(R.id.etdate);
            ettime=view.findViewById(R.id.ettime);
            btndelete=view.findViewById(R.id.btndelete);
            btnread=view.findViewById(R.id.btnread);




        }
    }


    public MyAdpter(List<DataModel> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DataModel model = moviesList.get(position);

       holder.etdate.setText(model.date);
       holder.etmsg.setText(model.msg);
       holder.ettime.setText(model.time);


        if(model.read.equals("true"))
        {
            holder.rel.setBackgroundColor(Color.parseColor("#D2E5B1"));

        }
        else
        {

            holder.rel.setBackgroundColor(Color.parseColor("#FFB2B2"));
        }

       holder.btndelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               MainActivity.pd.show();

               MainActivity.ref.child("chat").child(model.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       MainActivity.pd.dismiss();

                   }
               });



           }
       });

       holder.btnread.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MainActivity.pd.show();

               if(model.read.equals("true"))
               {
                   MainActivity.ref.child("chat").child(model.id).child("read").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           MainActivity.pd.dismiss();

                       }
                   });
               }
               else {

                   MainActivity.ref.child("chat").child(model.id).child("read").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           MainActivity.pd.dismiss();

                       }
                   });
               }

           }
       });


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
