package com.mgc.letobox.happy.floattools;

import com.mgc.letobox.happy.model.BaseResponse;
import com.mgc.letobox.happy.model.PlayGameResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MGCService {

    @GET("api/v7/benefits/playgametasklist")
    Call<PlayGameResult> obtainPlayGameResult(@Query("channel_id") int channel_id,
                                              @Query("mobile") String mobile,
                                              @Query("open_token") String open_token);
}
