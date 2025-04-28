package com.bansi.myapplication;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<List<User>> getUsersSortedByName() {
        return repository.getUsersSortedByName();
    }

    public LiveData<List<User>> getUsersSortedByEmail() {
        return repository.getUsersSortedByEmail();
    }

    public LiveData<List<User>> getUsersSortedByLastName() {
        return repository.getUsersSortedByLastName();
    }

    public void fetchUsers(int limit, int skip) {
        repository.fetchUsers(limit, skip);
    }
}
