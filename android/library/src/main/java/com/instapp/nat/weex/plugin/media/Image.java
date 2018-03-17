package com.instapp.nat.weex.plugin.media.Image;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.instapp.nat.media.image.Constant;
import com.instapp.nat.media.image.ImageModule;
import com.instapp.nat.media.image.ModuleResultListener;
import com.instapp.nat.media.image.Util;
import com.instapp.nat.permission.PermissionChecker;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Acathur on 17/2/17.
 * Copyright (c) 2017 Instapp. All rights reserved.
 */

@WeexModule(name = "nat/media/image")
public class Image extends WXModule {

    JSCallback mPickCallback;
    HashMap<String, Object> mPickParam;

    JSCallback mPreviewCallback;
    String[] mFiles;
    HashMap<String, Object> mPreviewParam;

    public static final int PICK_REQUEST_CODE = 101;
    public static final int PREVIEW_REQUEST_CODE = 102;

    String lang = Locale.getDefault().getLanguage();
    Boolean isChinese = lang.startsWith("zh");

    @JSMethod
    public void pick(HashMap<String, Object> param, final JSCallback jsCallback){
        mPickCallback = jsCallback;
        mPickParam = param;

        boolean b = PermissionChecker.lacksPermissions(mWXSDKInstance.getContext(), Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (b) {
            HashMap<String, String> dialog = new HashMap<>();
            if (isChinese) {
                dialog.put("title", "权限申请");
                dialog.put("message", "请允许应用访问相册");
            } else {
                dialog.put("title", "Permission Request");
                dialog.put("message", "Please allow the app to access photos");
            }

            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), dialog, new com.instapp.nat.permission.ModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if (o != null && o.toString().equals("true")) {
                        jsCallback.invoke(Util.getError(Constant.CAMERA_PERMISSION_DENIED, Constant.CAMERA_PERMISSION_DENIED_CODE));
                    }
                }
            }, PICK_REQUEST_CODE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            ImageModule.getInstance(mWXSDKInstance.getContext()).pick((Activity) mWXSDKInstance.getContext(), param);
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
            if (isChinese) {
                dialog.put("title", "权限申请");
                dialog.put("message", "请允许应用访问相册及文件");
            } else {
                dialog.put("title", "Permission Request");
                dialog.put("message", "Please allow the app to access photos and files on your device");
            }

            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), dialog, new com.instapp.nat.permission.ModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if (o != null && o.toString().equals("true")) {
                        jsCallback.invoke(Util.getError(Constant.MEDIA_INTERNAL_ERROR, Constant.MEDIA_INTERNAL_ERROR_CODE));
                    }
                }
            }, PREVIEW_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            ImageModule.getInstance(mWXSDKInstance.getContext()).preview(files, param, new ModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    jsCallback.invoke(o);
                }
            });
        }
    }

    @JSMethod
    public void info(String path, final JSCallback jsCallback){
        ImageModule.getInstance(mWXSDKInstance.getContext()).info(path, new ModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void exif(String path, final JSCallback jsCallback) {

        ImageModule.getInstance(mWXSDKInstance.getContext()).exif(path, new ModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Object o = ImageModule.getInstance(mWXSDKInstance.getContext()).onPickActivityResult(requestCode, resultCode, data);
        if (mPickCallback != null) {
            mPickCallback.invoke(o);
            // mPickCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                ImageModule.getInstance(mWXSDKInstance.getContext()).pick((Activity) mWXSDKInstance.getContext(), mPickParam);
            } else {
                mPickCallback.invoke(Util.getError(Constant.CAMERA_PERMISSION_DENIED, Constant.CAMERA_PERMISSION_DENIED_CODE));
            }
        }

        if (requestCode == PREVIEW_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                ImageModule.getInstance(mWXSDKInstance.getContext()).preview(mFiles, mPreviewParam, new ModuleResultListener() {
                    @Override
                    public void onResult(Object o) {
                        mPreviewCallback.invoke(o);
                    }
                });
            } else {
                mPreviewCallback.invoke(Util.getError(Constant.MEDIA_INTERNAL_ERROR, Constant.MEDIA_INTERNAL_ERROR_CODE));
            }
        }
    }
}
