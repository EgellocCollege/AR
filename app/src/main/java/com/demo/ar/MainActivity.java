package com.demo.ar;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

//    https://blog.csdn.net/qq_28844947/article/details/75284545

    private TextView mText;
    private Disposable mMarqueeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.timer);
    }

    public void TimerOff(View view){
       if (mMarqueeDisposable != null && !mMarqueeDisposable.isDisposed()) {
           mMarqueeDisposable.dispose();
       }
    }

    public void TimerOn(View view){
        List<String> list = new ArrayList<>();
        list.add("1-");
        list.add("2-");
        list.add("3-");
        list.add("4-");
        list.add("5-");
        list.add("\\u3000\\u3000");

        //fromIterable接收一个Iterable，每次发射一个元素（与for循环效果相同）
        Observable<String> listObservable = Observable.fromIterable(list);
        //interval定时器，间隔1秒发射一次
        Observable<Long> timerObservable =  Observable.interval(100, 5000, TimeUnit.MILLISECONDS).take(list.size());
        //使用zip操作符合并两个Observable
        mMarqueeDisposable = Observable.zip(listObservable, timerObservable, new BiFunction<String, Long, String>() {
            @Override
            public String apply(String s, Long aLong) throws Exception {
                return s + String.valueOf(aLong);
            }
        })
                .repeat()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String aLong) throws Exception {
                        mText.setText(aLong);
                        Log.e("642472", String.valueOf(aLong) );
                    }
                });
    }
}
