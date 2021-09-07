package com.habib.habib_user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatSendingAdapter extends RecyclerView.Adapter<ChatSendingAdapter.myView> {
    private List<MessageModel> data;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public ChatSendingAdapter(List<MessageModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ChatSendingAdapter.myView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, parent, false);
        return new ChatSendingAdapter.myView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatSendingAdapter.myView holder, final int position) {

holder.blog_detail_desc.setText(data.get(position).getMessage());
firebaseAuth=FirebaseAuth.getInstance();
firebaseFirestore=FirebaseFirestore.getInstance();


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myView extends RecyclerView.ViewHolder {
        TextView blog_detail_desc,add_notes_title;

        public myView(@NonNull View itemView) {

            super(itemView);
            blog_detail_desc=itemView.findViewById(R.id.messageTextLayout);


        }
    }
}

