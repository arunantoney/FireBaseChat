package com.inapp.firebasechat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.inapp.firebasechat.R;
import com.inapp.firebasechat.model.User;


public class UserListActivity extends AppCompatActivity {

    private Firebase refFirebase;
    private Firebase childref;
    private static String BASEURL = "https://arunsamplechat.firebaseio.com/";
    private FirebaseRecyclerAdapter<User, ChatMessageViewHolder> mAdapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = (RecyclerView) findViewById(R.id.rvList);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        refFirebase = new Firebase(BASEURL);
        childref = refFirebase.child("User");
        childref.keepSynced(true);

        childref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot msgSnapshot : snapshot.getChildren()) {
                    User user = msgSnapshot.getValue(User.class);
                    Log.i("Chat", user.name);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mAdapter = new FirebaseRecyclerAdapter<User, ChatMessageViewHolder>(User.class, android.R.layout.two_line_list_item, ChatMessageViewHolder.class, childref) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder viewHolder, User user, final int i) {
                viewHolder.nameText.setText(user.name);
                viewHolder.nameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.getRef(i).removeValue();

                    }
                });
            }


        };

        recycler.setAdapter(mAdapter);

    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(android.R.id.text1);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // mAdapter.cleanup();
    }

}
