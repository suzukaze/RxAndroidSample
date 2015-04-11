package com.github.suzukaze.rxandroidsample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EpitomeBeam {
  @SerializedName("sources")
  public List<EpitomeEntry> sources;
}
