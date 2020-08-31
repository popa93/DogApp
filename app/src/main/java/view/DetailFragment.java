package view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dogapp.R;
import com.example.dogapp.databinding.FragmentDetailBinding;
import com.example.dogapp.databinding.ItemDogBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.DogBreed;
import model.DogPalette;
import util.Util;
import viewmodel.DetailViewModel;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View view=inflater.inflate(R.layout.fragment_detail, container, false);
        FragmentDetailBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false);
        this.binding=binding;

        //binding.dogName.setText("Dog name"); //if you like to have later access in code at the elements from xml you could do this

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            dogUuid=DetailFragmentArgs.fromBundle(getArguments()).getDogUuid(); //this retrieves data sent from lsit fragment

        }
        viewModel= ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(dogUuid);
        observeViewModel();
    }

    private  void observeViewModel(){

        viewModel.dogLiveData.observe(this, new Observer<DogBreed>() {
            @Override
            public void onChanged(DogBreed dogBreed) {
                if(dogBreed!=null && dogBreed instanceof DogBreed){

                    binding.setDog(dogBreed);

                    setupBackgroundColor(dogBreed.imageUrl);

                    /*Util.loadImage(dogImage,dogBreed.imageUrl,new CircularProgressDrawable(dogImage.getContext()));
                    dogName.setText(dogBreed.dogBreed);
                    dogPurpose.setText(dogBreed.bredFor);
                    dogTemperament.setText(dogBreed.temperament);
                    dogLifespan.setText(dogBreed.lifeSpan);*/
                }
            }
        });
    }

    private void setupBackgroundColor(String url){
        Glide.with(this).asBitmap().load(url).into(new CustomTarget<Bitmap>() {     //creates from url a bitmap then gives it to palette to genereta color
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@Nullable Palette palette) {
                        if(palette!=null&& palette.getLightMutedSwatch()!=null) {
                            int intColor = palette.getLightMutedSwatch().getRgb();
                            DogPalette myPalette=new DogPalette(intColor);
                            binding.setPalette(myPalette);

                        }
                    }
                });
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });


    }

}

