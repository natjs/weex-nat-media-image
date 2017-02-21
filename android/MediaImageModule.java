package com.nat.weex;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import com.nat.media_image.HLConstant;
import com.nat.media_image.HLImageModule;
import com.nat.media_image.HLModuleResultListener;
import com.nat.media_image.HLUtil;
import com.nat.permission.PermissionChecker;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;

/**
 * Created by Daniel on 17/2/17.
 * Copyright (c) 2017 Nat. All rights reserved.
 */

public class MediaImageModule extends WXModule {

    JSCallback mPickCallback;
    HashMap<String, Object> mPickParam;

    JSCallback mPreviewCallback;
    String[] mFiles;
    HashMap<String, Object> mPreviewParam;

    public static final int PICK_REQUEST_CODE = 101;
    public static final int PREVIEW_REQUEST_CODE = 102;

    @JSMethod
    public void pick(HashMap<String, Object> param, final JSCallback jsCallback){
        mPickCallback = jsCallback;
        mPickParam = param;

        boolean b = PermissionChecker.lacksPermissions(mWXSDKInstance.getContext(), Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (b) {
            HashMap<String, String> dialog = new HashMap<>();
            dialog.put("title", "权限申请");
            dialog.put("message", "请允许打开相机，相册");
            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), dialog, new com.nat.permission.HLModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if (o != null && o.toString().equals("true")) {
                        jsCallback.invoke(HLUtil.getError(HLConstant.CAMERA_PERMISSION_DENIED, HLConstant.CAMERA_PERMISSION_DENIED_CODE));
                    }
                }
            }, PICK_REQUEST_CODE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            HLImageModule.getInstance(mWXSDKInstance.getContext()).pick((Activity) mWXSDKInstance.getContext(), param);
        }
    }

    @JSMethod
    public void preview(String[] files, HashMap<String, Object> param, final JSCallback jsCallback){
        mPreviewCallback = jsCallback;
        mPreviewParam = param;
        mFiles = files;

        boolean b = PermissionChecker.lacksPermissions(mWXSDKInstance.getContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        if (b) {
            HashMap<String, String> dialog = new HashMap<>();
            dialog.put("title", "权限申请");
            dialog.put("message", "请允许打开sdCard");
            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), dialog, new com.nat.permission.HLModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if (o != null && o.toString().equals("true")) {
                        jsCallback.invoke(HLUtil.getError(HLConstant.MEDIA_INTERNAL_ERROR, HLConstant.MEDIA_INTERNAL_ERROR_CODE));
                    }
                }
            }, PREVIEW_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            HLImageModule.getInstance(mWXSDKInstance.getContext()).preview(files, param, new HLModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    jsCallback.invoke(o);
                }
            });
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Object o = HLImageModule.getInstance(mWXSDKInstance.getContext()).onPickActivityResult(requestCode, resultCode, data);
        if (mPickCallback != null) {
            mPickCallback.invoke(o);
            mPickCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                HLImageModule.getInstance(mWXSDKInstance.getContext()).pick((Activity) mWXSDKInstance.getContext(), mPickParam);
            } else {
                mPickCallback.invoke(HLUtil.getError(HLConstant.CAMERA_PERMISSION_DENIED, HLConstant.CAMERA_PERMISSION_DENIED_CODE));
            }
        }

        if (requestCode == PREVIEW_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                HLImageModule.getInstance(mWXSDKInstance.getContext()).preview(mFiles, mPreviewParam, new HLModuleResultListener() {
                    @Override
                    public void onResult(Object o) {
                        mPreviewCallback.invoke(o);
                    }
                });
            } else {
                mPreviewCallback.invoke(HLUtil.getError(HLConstant.MEDIA_INTERNAL_ERROR, HLConstant.MEDIA_INTERNAL_ERROR_CODE));
            }
        }
    }
}
