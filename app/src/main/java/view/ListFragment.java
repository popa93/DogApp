package view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dogapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.DogBreed;
import viewmodel.ListViewModel;


public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private DogsListAdapter dogsListAdapter=new DogsListAdapter(new ArrayList<>());

    @BindView(R.id.dogsList)
    RecyclerView dogsList;

    @BindView(R.id.listError)
    TextView listError;

    @BindView(R.id.loadingView)
    ProgressBar loadingView;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;


    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel= ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        dogsList.setLayoutManager(new LinearLayoutManager(getContext()));
        dogsList.setAdapter(dogsListAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dogsList.setVisibility(View.GONE);
                listError.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                viewModel.refreshBypassCache();
                refreshLayout.setRefreshing(false);// hides the spinner
            }
        });

        observeViewModel();
    }

    private void observeViewModel(){
        viewModel.dogs.observe(this, new Observer<List<DogBreed>>() {
            @Override
            public void onChanged(List<DogBreed> dogs) {
                if(dogs!=null&& dogs instanceof List){

                    dogsList.setVisibility(View.VISIBLE);
                    dogsListAdapter.updateDogsList(dogs);
                }
            }
        });

        viewModel.dogLoadError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if(isError!=null&& isError instanceof Boolean){

                    listError.setVisibility(isError? View.VISIBLE : View.GONE);
                }
            }
        });

        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if(isLoading!=null&& isLoading instanceof Boolean){

                    loadingView.setVisibility(isLoading? View.VISIBLE : View.GONE);
                }
                if(isLoading){
                    listError.setVisibility(View.GONE);
                    dogsList.setVisibility(View.GONE);
                }
            }
        });

    }


}