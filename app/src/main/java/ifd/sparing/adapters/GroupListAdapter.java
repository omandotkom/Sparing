package ifd.sparing.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ifd.sparing.MessageListActivityNew;
import ifd.sparing.R;
import ifd.sparing.model.GroupModel;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private List<GroupModel> groupList;
    private Context ctx;
    public GroupListAdapter(List<GroupModel> grp, Context c){
        this.groupList = grp;this.ctx =c;
    }
    @NonNull
    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View inviteView = inflater.inflate(R.layout.item_recent_chat, viewGroup, false);

        // Return a new holder instance
        GroupListAdapter.ViewHolder viewHolder = new GroupListAdapter.ViewHolder(inviteView);
        return viewHolder;
    }
private final String TAG = "GroupListAdapterLOG";
    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.ViewHolder holder, int i) {
        GroupModel group = groupList.get(i);

        // Set item views based on your views and data model
        TextView title = holder.titleGroup;
        title.setText(group.getTitle());
        TextView lastChat=holder.lastChat;
        lastChat.setText(group.getLastChat());
        TextView lastChatTime=holder.lastTime;
        lastChatTime.setText(group.getTime());

        ConstraintLayout constraintLayout = holder.recent_chat_back;
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Opening chat room :" + group.getSparing().getChatID());
                Intent i = new Intent(ctx,MessageListActivityNew.class);
                i.putExtra("chatID",group.getSparing().getChatID());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView titleGroup, lastChat, lastTime;
    public ConstraintLayout recent_chat_back;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleGroup = (TextView) itemView.findViewById(R.id.groupName);
            lastChat = (TextView) itemView.findViewById(R.id.lastChat);
            lastTime = (TextView) itemView.findViewById(R.id.lastChatTime);
            recent_chat_back = (ConstraintLayout) itemView.findViewById(R.id.recent_chat_back);
            /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx,MessageListActivityNew.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }
            });*/
        }
    }
}
