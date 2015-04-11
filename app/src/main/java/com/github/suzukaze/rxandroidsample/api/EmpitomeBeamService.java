package com.github.suzukaze.rxandroidsample.api;

import com.github.suzukaze.rxandroidsample.model.EpitomeBeam;

import retrofit.http.GET;

public interface EmpitomeBeamService {

  public static final String ENDPOINT = "https://ja.epitomeup.com";

  @GET("/feed/beam")
  EpitomeBeam getBeam();
}
