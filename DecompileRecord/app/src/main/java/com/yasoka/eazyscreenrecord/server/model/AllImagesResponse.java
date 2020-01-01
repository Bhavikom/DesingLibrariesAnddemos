package com.yasoka.eazyscreenrecord.server.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AllImagesResponse {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("http_response_code")
    @Expose
    private Integer httpResponseCode;
    @SerializedName("http_response_message")
    @Expose
    private String httpResponseMessage;

    public class Data {
        @SerializedName("Data")
        @Expose
        private List<ServerDatum> data = null;
        @SerializedName("ErrorCode")
        @Expose
        private Integer errorCode;
        @SerializedName("ErrorMessage")
        @Expose
        private String errorMessage;
        @SerializedName("totaldata")
        @Expose
        private Integer totaldata;
        @SerializedName("totalpages")
        @Expose
        private Integer totalpages;

        public Data() {
        }

        public Integer getTotaldata() {
            return this.totaldata;
        }

        public void setTotaldata(Integer num) {
            this.totaldata = num;
        }

        public Integer getTotalpages() {
            return this.totalpages;
        }

        public void setTotalpages(Integer num) {
            this.totalpages = num;
        }

        public Integer getErrorCode() {
            return this.errorCode;
        }

        public void setErrorCode(Integer num) {
            this.errorCode = num;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public void setErrorMessage(String str) {
            this.errorMessage = str;
        }

        public List<ServerDatum> getData() {
            return this.data;
        }

        public void setData(List<ServerDatum> list) {
            this.data = list;
        }
    }

    public Integer getHttpResponseCode() {
        return this.httpResponseCode;
    }

    public void setHttpResponseCode(Integer num) {
        this.httpResponseCode = num;
    }

    public String getHttpResponseMessage() {
        return this.httpResponseMessage;
    }

    public void setHttpResponseMessage(String str) {
        this.httpResponseMessage = str;
    }

    public Data getData() {
        return this.data;
    }

    public void setData(Data data2) {
        this.data = data2;
    }
}
