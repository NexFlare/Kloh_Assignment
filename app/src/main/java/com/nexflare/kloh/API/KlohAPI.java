package com.nexflare.kloh.API;

import com.nexflare.kloh.Model.PostRequest;
import com.nexflare.kloh.Model.ResponseAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nexflare on 12/08/17.
 */

public interface KlohAPI {

    @POST("list")
    Call<ResponseAPI> getAllEvents(@Body PostRequest request);
}
