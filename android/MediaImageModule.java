package com.nat.weex;

import android.app.Activity;
import android.content.Intent;

import com.nat.media_image.HLImageModule;
import com.nat.media_image.HLModuleResultListener;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;

/**
 * Created by Daniel on 17/2/17.
 */

public class MediaImageModule extends WXModule {

    JSCallback mPickCallback;
    @JSMethod
    public void pick(HashMap<String, Object> param, JSCallback jsCallback){
        mPickCallback = jsCallback;
        HLImageModule.getInstance(mWXSDKInstance.getContext()).pick((Activity) mWXSDKInstance.getContext(), param, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Object o = HLImageModule.getInstance(mWXSDKInstance.getContext()).onPickActivityResult(requestCode, resultCode, data);
        if (mPickCallback != null) {
            mPickCallback.invoke(o);
            mPickCallback = null;
        }
    }

    @JSMethod
    public void preview(String[] files, HashMap<String, Object> param, final JSCallback jsCallback){
        HLImageModule.getInstance(mWXSDKInstance.getContext()).preview(files, param, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void info(String path, final JSCallback jsCallback){
        HLImageModule.getInstance(mWXSDKInstance.getContext()).info(path, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void exif(String path, final JSCallback jsCallback) {
        HLImageModule.getInstance(mWXSDKInstance.getContext()).exif(path, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }
}
