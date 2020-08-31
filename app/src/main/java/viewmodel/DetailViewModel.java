package viewmodel;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import model.DogBreed;
import model.DogDatabase;

public
class DetailViewModel extends ViewModel { //only ViewModel because we do not need any context

    public MutableLiveData<DogBreed> dogLiveData=new MutableLiveData<>();

    private RetrieveDogTask dogDetail;



    public void fetch(int dogUuid){

        dogDetail=new RetrieveDogTask();
        dogDetail.execute(dogUuid);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(dogDetail!=null){
            dogDetail.cancel(true);
            dogDetail=null;
        }
    }

    private class RetrieveDogTask extends AsyncTask<Integer , Void, DogBreed>{
        @Override
        protected DogBreed doInBackground(Integer... ints) {

            return DogDatabase.getInstance(null).dogDao().getDog(ints[0].intValue());
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {

            dogLiveData.setValue(dogBreed);
        }
    }
}
