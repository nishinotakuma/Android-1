package com.example.mless.nishino;

import java.io.Serializable;
import java.util.List;

// 直列化はParcelableを実装してください
// refs http://y-anz-m.blogspot.com/2010/03/androidparcelable.html
public class SerializableFormValues implements Serializable {
    public String surName, firstName;
    public String sex;
    public String phone;
    public List<String> hobby;
    public String work;
}
