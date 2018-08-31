package com.json;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by android on 9/29/2017.
 */

public interface ApiInterface {

    @GET("incomingCallBlockerAd.txt")
    Call<ArrayList<MyAd>> getMyAd();
}
