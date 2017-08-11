package com.nexflare.kloh.API;

import com.nexflare.kloh.Model.EventResponse;
import com.nexflare.kloh.Model.PostRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by nexflare on 12/08/17.
 */

public interface KlohAPI {

    @POST("list")
    Call<EventResponse> getAllEvents(@Body PostRequest request);
}
