package com.mgc.letobox.happy.floattools;

import com.mgc.letobox.happy.model.FloatToolsConfig;
import com.mgc.letobox.happy.model.ShakeResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MGCService {
    @GET("api/v7/benefits/setting")
    Call<FloatToolsConfig> obtainFloatToolsConfig(@Query("channel_id") int channel_id,
                                                  @Query("mobile") String mobile,
                                                  @Query("framework_version") String framework_version,
                                                  @Query("leto_version") String leto_version,
                                                  @Query("open_token") String open_token);

    @FormUrlEncoded
    @POST("api/v7/shake/getaward")
    Call<ShakeResult> obtainShakeResult(@Field("channel_id") int channel_id,
                                        @Field("game_id") int game_id,
                                        @Field("mobile") String mobile,
                                        @Field("open_token") String open_token);
}