package com.github.suzukaze.rxandroidsample.api;

import android.util.Base64;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {

  // No need to instantiate this class.
  private ServiceGenerator() {
  }

  public static <S> S createService(Class<S> serviceClass, String endpoint) {
    // call basic auth generator method without user and pass
    return createService(serviceClass, endpoint, null, null);
  }

  public static <S> S createService(Class<S> serviceClass, String endpoint, String username, String password) {
    // set endpoint url and use OkHTTP as HTTP client
    RestAdapter.Builder builder = new RestAdapter.Builder()
        .setEndpoint(endpoint)
        .setConverter(new GsonConverter(new Gson()))
        .setClient(new OkClient(new OkHttpClient()));

    if (username != null && password != null) {
      // concatenate username and password with colon for authentication
      final String credentials = username + ":" + password;

      builder.setRequestInterceptor(new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
          // create Base64 encoded string
          String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
          request.addHeader("Authorization", string);
          request.addHeader("Accept", "application/json");
        }
      });
    }

    RestAdapter adapter = builder.build();

    return adapter.create(serviceClass);
  }
}
