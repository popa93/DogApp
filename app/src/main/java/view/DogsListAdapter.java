package view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.R;
import com.example.dogapp.databinding.ItemDogBinding;

import java.util.ArrayList;
import java.util.List;

import model.DogBreed;
import util.Util;

public
class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListener{

    private ArrayList<DogBreed> dogsList;

    public DogsListAdapter(ArrayList<DogBreed> dogList){
        this.dogsList=dogList;
    }

    public void updateDogsList(List<DogBreed> newDogsList){
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        ItemDogBinding view= DataBindingUtil.inflate(inflater,R.layout.item_dog,parent,false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {

        holder.itemView.setDog(dogsList.get(position));
        holder.itemView.setListener(this);

        /*ImageView image=holder.itemView.findViewById(R.id.imageView);       //find view by id is expensive to Android =>better with data binding.
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView lifeSpan=holder.itemView.findViewById(R.id.lifeSpan);

        LinearLayout layout=holder.itemView.findViewById(R.id.dogLayout);
        layout.setOnClickListener(new View.OnClickListener() {      //better not set click listener here. can be optimized with set tag maybe and get tag
            @Override
            public void onClick(View view) {
              ListFragmentDirections.ActionListFragmentToDetailFragment  action=ListFragmentDirections.actionListFragmentToDetailFragment();
              action.setDogUuid(dogsList.get(position).uuid);
              Navigation.findNavController(layout).navigate(action);
            }
        });

        name.setText(dogsList.get(position).dogBreed);
        lifeSpan.setText(dogsList.get(position).lifeSpan);
        Util.loadImage(image,dogsList.get(position).imageUrl,Util.getProgressDrawable(image.getContext()));*/



    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    @Override
    public void onDogClicked(View v) {
        String uuidString=((TextView)v.findViewById(R.id.dogID)).getText().toString();
        int uuid=Integer.parseInt(uuidString);
        ListFragmentDirections.ActionListFragmentToDetailFragment  action=ListFragmentDirections.actionListFragmentToDetailFragment();
        action.setDogUuid(uuid);
        Navigation.findNavController(v).navigate(action);
    }

    class DogViewHolder extends RecyclerView.ViewHolder{

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
        }
    }
}
