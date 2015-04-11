package com.github.suzukaze.rxandroidsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.suzukaze.rxandroidsample.api.EmpitomeBeamService;
import com.github.suzukaze.rxandroidsample.api.ServiceGenerator;
import com.github.suzukaze.rxandroidsample.model.EpitomeBeam;
import com.github.suzukaze.rxandroidsample.model.EpitomeEntry;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.ViewObservable;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

  private static String TAG = MainActivity.class.getSimpleName();

  private List<String> titles;
  private ListView listView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    titles = new ArrayList<>();

    listView = (ListView) findViewById(R.id.listView);

    final MainActivity finalMainActivity = this;

    Observable observable = Observable.create(new Observable.OnSubscribe<EpitomeEntry>() {
      @Override
      public void call(Subscriber<? super EpitomeEntry> subscriber) {
        EmpitomeBeamService empitomeBeamService = ServiceGenerator.createService(
            EmpitomeBeamService.class, EmpitomeBeamService.ENDPOINT);
        EpitomeBeam epitomeBeam = empitomeBeamService.getBeam();
        for (EpitomeEntry epitomeEntry : epitomeBeam.sources) {
          subscriber.onNext(epitomeEntry);
          android.util.Log.d(TAG, epitomeEntry.title);
        }
        subscriber.onCompleted();
      }
    })
        .subscribeOn(Schedulers.io());

    ViewObservable.bindView(listView, observable);

    observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<EpitomeEntry>() {
          @Override
          public void onNext(EpitomeEntry
                                 epitomeEntry) {
            titles.add(epitomeEntry.title);
          }

          @Override
          public void onCompleted() {
            ArrayAdapter<String> adapter = new ArrayAdapter(finalMainActivity,
                android.R.layout.simple_expandable_list_item_1, titles);
            listView.setAdapter(adapter);
          }

          @Override
          public void onError(Throwable e) {
          }
        });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
