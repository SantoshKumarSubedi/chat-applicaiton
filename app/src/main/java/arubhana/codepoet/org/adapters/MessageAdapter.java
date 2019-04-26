package arubhana.codepoet.org.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import arubhana.codepoet.org.arubhana.R;
import arubhana.codepoet.org.arubhana.UserGlobal;
import arubhana.codepoet.org.database.Messages;

public class MessageAdapter extends RecyclerView.Adapter {
    private List<Messages> messageList;
    String friendName;
    public MessageAdapter(String friendName){
        this.friendName=friendName;
    }



    @Override
    public int getItemViewType(int position) {
        Messages message=messageList.get(position);
        if(message.getMessageFrom().equals(UserGlobal.username)){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i==0) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.outgoing_message, viewGroup, false);
            return new MessageViewHolder(view);
        }else{
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.incoming_message, viewGroup, false);
            return new IncomingMessageViewHolders(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(messageList!=null) {
            Messages message=messageList.get(i);

            switch (holder.getItemViewType()) {
                case 0:
                    ((MessageViewHolder) holder).textView.setText(message.getMessage());
                    break;
                case 1:
                    ((IncomingMessageViewHolders) holder).username.setText(message.getMessageFrom());
                    ((IncomingMessageViewHolders) holder).msg.setText(message.getMessage());

                    break;
            }
        }
    }



    @Override
    public int getItemCount() {
        if(messageList!=null) {
            return messageList.size();
        }else{
            return 0;
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.textView);
        }
    }

    public class IncomingMessageViewHolders extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView msg;
        public IncomingMessageViewHolders(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.incoming_message_user_name);
            msg=itemView.findViewById(R.id.incoming_message_incoming_message);

        }
    }

    public void setMessageList(List<Messages> messageList){
        this.messageList=messageList;
        notifyDataSetChanged();

    }
}

