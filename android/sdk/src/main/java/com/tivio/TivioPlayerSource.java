package com.tivio;

import com.facebook.react.bridge.ReadableMap;

public class TivioPlayerSource {

    public String channelName;
    public int continueFromPosition = -1;
    public long epgFrom = -1;
    public long epgTo = -1;
    public String mode;
    public int startFromPosition = -1;
    public long streamStart = -1;

    public int startPosition = 0;
    public String type;
    public String uri;

    public TivioPlayerSource(ReadableMap data) {
        this.channelName = data.getString("channelName");
        this.mode = data.getString("mode");
        this.startPosition = data.getInt("startFromPosition");
        this.type = data.getString("type");
        this.uri = data.getString("uri");
    }

    private TivioPlayerSource() { }

    public static TivioPlayerSource continueWithChannel(String channelName, String mode, String uri, long epgFrom, long epgTo, long streamStart, int continueFromPosition) {
        TivioPlayerSource source = new TivioPlayerSource();
        source.channelName = channelName;
        source.continueFromPosition = continueFromPosition;
        source.epgFrom = epgFrom;
        source.epgTo = epgTo;
        source.mode = mode;
        source.streamStart = streamStart;
        source.type = "tv_program";
        source.uri = uri;

        return source;
    }

    public static TivioPlayerSource startWithChannel(String channelName, String mode, String uri, long epgFrom, long epgTo, long streamStart, int startFromPosition) {
        TivioPlayerSource source = new TivioPlayerSource();
        source.channelName = channelName;
        source.epgFrom = epgFrom;
        source.epgTo = epgTo;
        source.mode = mode;
        source.startFromPosition = startFromPosition;
        source.streamStart = streamStart;
        source.type = "tv_program";
        source.uri = uri;

        return source;
    }

}
