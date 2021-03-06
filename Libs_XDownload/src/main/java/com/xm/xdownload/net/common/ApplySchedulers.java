package com.xm.xdownload.net.common;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: 小民
 * @date: 2017-06-05
 * @time: 17:25
 * @开源地址: https://github.com/2745329043/XDownloadDemo
 * @说明:
 */
public class ApplySchedulers<T> implements ObservableTransformer<T,T> {

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
//                .delay(5, TimeUnit.SECONDS)     //请求延迟五秒，再开始
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(),true)
                .retryWhen(new RetryWhenNetwork());
    }
}
