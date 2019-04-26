package arubhana.codepoet.org.adapters;

import android.content.ClipData;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import arubhana.codepoet.org.arubhana.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    List<String> names;
    String action="nothing";
    private RequestAdapterClickListener requestAdapterClickListener;

    public RequestAdapter(RequestAdapterClickListener requestAdapterClickListener){
        this.requestAdapterClickListener=requestAdapterClickListener;
        names=new ArrayList<>();
        names.add("ssss");
    }

    public void addName(String name){
        names.add(name);
        notifyDataSetChanged();
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_box,parent,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, final int position) {
        final String name=names.get(position);
        holder.name.setText(name);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapterClickListener.onClickRequest(name,"accept");
                names.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdapterClickListener.onClickRequest(name,"reject");
                names.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(names!=null) {
            return names.size();
        }else{
            return 0;
        }


        }


    public interface RequestAdapterClickListener{
         void onClickRequest(String name,String action);
        }

    public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        Button accept,reject;
        public RequestViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.request_pop_up_name);
            accept = itemView.findViewById(R.id.pop_Up_accept);
            reject = itemView.findViewById(R.id.pop_Up_reject);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        requestAdapterClickListener.onClickRequest(names.get(getAdapterPosition()),action);
        }
    }
}
