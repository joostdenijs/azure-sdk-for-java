package com.microsoft.windowsazure.services.queue.models;

public class CreateMessageOptions extends QueueServiceOptions {
    private Integer visibilityTimeoutInSeconds;
    private Integer timeToLiveInSeconds;

    public Integer getVisibilityTimeoutInSeconds() {
        return visibilityTimeoutInSeconds;
    }

    public CreateMessageOptions setVisibilityTimeoutInSeconds(Integer visibilityTimeoutInSeconds) {
        this.visibilityTimeoutInSeconds = visibilityTimeoutInSeconds;
        return this;
    }

    public Integer getTimeToLiveInSeconds() {
        return timeToLiveInSeconds;
    }

    public CreateMessageOptions setTimeToLiveInSeconds(Integer timeToLiveInSeconds) {
        this.timeToLiveInSeconds = timeToLiveInSeconds;
        return this;
    }
}