package com.example.mless.nishino;

import java.io.Serializable;
import java.util.List;

public class SerializableFormValues implements Serializable {
    public String surName, firstName;
    public String sex;
    public String phone;
    public List<String> hobby;
    public String work;
}
