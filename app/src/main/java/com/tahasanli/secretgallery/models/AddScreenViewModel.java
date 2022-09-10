package com.tahasanli.secretgallery.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddScreenViewModel extends ViewModel {
    private MutableLiveData<Integer> index = new MutableLiveData<Integer>();

    public void setIndex(int i){
        index.setValue(i);
    }

    public LiveData<Integer> getIndex(){
        return index;
    }

}
