package view;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dogapp.R;
import com.example.dogapp.databinding.FragmentDetailBinding;
import com.example.dogapp.databinding.ItemDogBinding;
import com.example.dogapp.databinding.SendSmsDialogBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.DogBreed;
import model.DogPalette;
import model.SmsInfo;
import util.Util;
import viewmodel.DetailViewModel;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;

    private Boolean sendSmsStarted=false;
    private DogBreed currentDog;

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
        setHasOptionsMenu(true);
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
                    currentDog=dogBreed;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_send_sms:
                if(!sendSmsStarted){
                    sendSmsStarted=true;
                    ((MainActivity)getActivity()).checkSmsPermission(); //only activity can handle permissions ,not fragments
                }
                break;
            case R.id.action_share:
                Toast.makeText(getContext(),"Action share",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted){
        if(isAdded() && sendSmsStarted && permissionGranted){

            SmsInfo smsInfo=new SmsInfo("",currentDog.dogBreed+ " bred for " + currentDog.bredFor,currentDog.imageUrl);

            SendSmsDialogBinding dialogBinding=DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.send_sms_dialog,null,false);

            new AlertDialog.Builder(getContext()).setView(dialogBinding.getRoot()).setPositiveButton("Send SMS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(!dialogBinding.smsDestination.getText().toString().isEmpty()){
                        smsInfo.to=dialogBinding.smsDestination.getText().toString();
                        sendSms(smsInfo);
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
            sendSmsStarted=false;

            dialogBinding.setSmsInfo(smsInfo);
        }
    }

    private void sendSms(SmsInfo smsInfo){
        Intent intent=new Intent(getContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getContext(),0,intent,0);
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to,null,smsInfo.text,pi,null);

    }
}

