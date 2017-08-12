package com.nexflare.kloh.API;

import com.nexflare.kloh.Model.EventDetailResponse;
import com.nexflare.kloh.Model.EventResponse;
import com.nexflare.kloh.Model.PostRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nexflare on 12/08/17.
 */

public interface KlohAPI {

    @POST("list")
    Call<EventResponse> getAllEvents(@Body PostRequest request);

    @GET("{activityId}")
    Call<EventDetailResponse> getEventDetail(@Path("activityId") String activityId);
}
