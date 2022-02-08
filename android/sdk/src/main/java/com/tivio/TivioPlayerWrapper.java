package com.tivio;

import com.tivio.bridge.TivioPlayerWrapperBridge;

public class TivioPlayerWrapper {

    public int playerWrapperId;
    public TivioPlayerWrapperDelegate delegate;

    public TivioPlayerWrapper(int playerWrapperId) {
        this.playerWrapperId = playerWrapperId;
    }

    public void reportTimeProgress(int miliseconds) {
        TivioPlayerWrapperBridge.reportTimeProgress(miliseconds, this.playerWrapperId);
    }

    public void seekTo(int miliseconds) {
        TivioPlayerWrapperBridge.seekTo(miliseconds, this.playerWrapperId);
    }

    public void setSource(TivioPlayerSource source) {
        TivioPlayerWrapperBridge.setSource(source, this.playerWrapperId);
    }

}
