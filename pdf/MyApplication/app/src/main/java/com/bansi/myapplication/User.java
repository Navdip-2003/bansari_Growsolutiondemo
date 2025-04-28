package com.bansi.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int id;
    public String firstName;
    public String lastName;
    public String email;
}
