package ifd.sparing.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ifd.sparing.AjakTemanLagi;
import ifd.sparing.MessageListActivityNew;
import ifd.sparing.R;
import ifd.sparing.model.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>  {
private Context ctx;
    public UsersAdapter(List<User> userList, Context c) {
        mUsers = userList;ctx = c;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View inviteView = inflater.inflate(R.layout.item_ajak, viewGroup, false);

        // Return a new holder instance
        UsersAdapter.ViewHolder viewHolder = new UsersAdapter.ViewHolder(inviteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        User user = mUsers.get(i);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        CircleImageView profilePic= holder.profilePic;
        profilePic.setImageResource(R.drawable.man);
        textView.setText(user.getName());
        Button button = holder.messageButton;
        button.setText("Ajak");
    }
    private List<User> mUsers;



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button messageButton;
        public CircleImageView profilePic;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_image);
            nameTextView = (TextView) itemView.findViewById(R.id.user_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ctx.startActivity(new Intent(ctx,MessageListActivityNew.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
