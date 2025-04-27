package com.bansi.myapplication;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ApiService apiService;
    private ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public LiveData<List<User>> getUsersSortedByName() {
        return userDao.getUsersSortedByName();
    }

    public LiveData<List<User>> getUsersSortedByEmail() {
        return userDao.getUsersSortedByEmail();
    }

    public LiveData<List<User>> getUsersSortedByLastName() {
        return userDao.getUsersSortedByLastName();
    }

    public void fetchUsers(int limit, int skip) {
        apiService.getUsers(limit, skip).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> userDao.insertUsers(response.body().users));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UserRepository", "Error fetching users", t);
            }
        });
    }
}
