package com.microsoft.windowsazure.services.serviceBus.models;


public class GetQueueResult {

    private Queue value;

    public GetQueueResult(Queue value) {
        this.setValue(value);
    }

    public void setValue(Queue value) {
        this.value = value;
    }

    public Queue getValue() {
        return value;
    }

}