package com.microsoft.windowsazure.services.serviceBus.models;


public class GetTopicResult {

    private Topic value;

    public GetTopicResult(Topic value) {
        this.setValue(value);
    }

    public void setValue(Topic value) {
        this.value = value;
    }

    public Topic getValue() {
        return value;
    }

}