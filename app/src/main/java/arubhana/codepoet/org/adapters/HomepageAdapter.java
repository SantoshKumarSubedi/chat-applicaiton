package arubhana.codepoet.org.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import arubhana.codepoet.org.arubhana.R;
import arubhana.codepoet.org.database.Friends;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.HomePageViewHolder>{

    private List<Friends> mFriends;
    private final HomePageAdapterOnClickHandler mclickhandler;

    public HomepageAdapter(HomePageAdapterOnClickHandler mclickhandler){
        this.mclickhandler=mclickhandler;

    }

    public interface HomePageAdapterOnClickHandler{
        void onClick(String friend,String roomName);
    }
    @Override
    public HomePageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_friend,parent,false);

        return new HomePageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomePageViewHolder holder, int position) {
        if(mFriends!=null) {
            Friends friend = mFriends.get(position);
            holder.friendname.setText(friend.getFriendName());
            holder.status.setText(friend.getStatus());
            holder.lastMessage.setText(friend.getLastMessage());
            System.out.println(friend.getLastMessage());
        }else{
            holder.friendname.setText("You don't have any friends");
        }
    }

    @Override
    public int getItemCount() {
        if(mFriends!=null)
            return mFriends.size();
        else
            return 0;
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView friendname;
        TextView status;
        TextView lastMessage;
        public HomePageViewHolder(View itemView) {
            super(itemView);
            friendname=(TextView) itemView.findViewById(R.id.friend_list_username);
            status=(TextView) itemView.findViewById(R.id.friend_list_status);
            lastMessage=itemView.findViewById(R.id.last_message_friend_list);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterpos=getAdapterPosition();
            mclickhandler.onClick(mFriends.get(adapterpos).getFriendName(),mFriends.get(adapterpos).getRoomName());


        }

    }

    public void setMFriends(List<Friends> mFriends){
        this.mFriends=mFriends;
        notifyDataSetChanged();
    }

}
