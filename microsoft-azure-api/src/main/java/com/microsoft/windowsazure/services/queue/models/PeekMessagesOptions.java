package com.microsoft.windowsazure.services.queue.models;

public class PeekMessagesOptions extends QueueServiceOptions {
    private Integer numberOfMessages;

    public Integer getNumberOfMessages() {
        return numberOfMessages;
    }

    public PeekMessagesOptions setNumberOfMessages(Integer numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
        return this;
    }
}