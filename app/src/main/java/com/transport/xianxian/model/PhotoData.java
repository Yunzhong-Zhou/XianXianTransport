package com.transport.xianxian.model;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;

/**
 * Created by fafukeji01 on 2017/6/1.
 */

public class PhotoData {
    public Uri uri = Uri.parse("");
    public String serviceUri = "";

    public PhotoData(File file) {
        uri = Uri.fromFile(file);
    }

    public PhotoData(PhotoDataSerializable data) {
        uri = Uri.parse(data.uriString);
        serviceUri = data.serviceUri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public void setServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    @Override
    public String toString() {
        return "PhotoData{" +
                "uri=" + uri +
                ", serviceUri='" + serviceUri + '\'' +
                '}';
    }

    // 因为PhotoData包含Uri，不能直接序列化，所以有了这个类
    public static class PhotoDataSerializable implements Serializable {
        String uriString = "";
        String serviceUri = "";

        public PhotoDataSerializable(PhotoData data) {
            uriString = data.uri.toString();
            serviceUri = data.serviceUri;
        }

    }
}
