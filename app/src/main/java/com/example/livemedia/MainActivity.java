package com.example.livemedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference rootref,thumbnalImage;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Channels> options=
                new  FirebaseRecyclerOptions.Builder<Channels>().
                        setQuery(rootref,Channels.class).build();
        FirebaseRecyclerAdapter<Channels,displayChannelView> adapter=
                new FirebaseRecyclerAdapter<Channels, displayChannelView>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final displayChannelView holder, int position, @NonNull Channels model) {
//                        thumbnalImage=FirebaseDatabase.getInstance().getReference()
//                                .child("channels").child(getRef(position).getKey());
//                        thumbnalImage.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()){
//                                    String url=snapshot.child("link").getValue().toString();
//                                    Picasso.get().load(url).resize(120,120)
//                                            .into(holder.channelImage);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                        final String linkstring=model.getLink();
                        String url=model.getLink();
                        holder.channelName.setText(model.getName());
                        Picasso.get().load(url).resize(120,120).into(holder.channelImage);
                        holder.channelName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(MainActivity.this, WebViewActivity.class);
                                intent.putExtra("linkstring",linkstring);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public displayChannelView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.channel_cards,parent,false);
                        displayChannelView view1=new displayChannelView(view);
                        return view1;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        rootref=FirebaseDatabase.getInstance().getReference().child("channels");
    }
    public class displayChannelView extends RecyclerView.ViewHolder{
        public TextView channelName;
        public ImageView channelImage;
        public displayChannelView(@NonNull View view){
            super(view);
            channelImage=(ImageView)view.findViewById(R.id.channelimageview);
            channelName=(TextView)view.findViewById(R.id.channeltextview);
        }
    }
}
