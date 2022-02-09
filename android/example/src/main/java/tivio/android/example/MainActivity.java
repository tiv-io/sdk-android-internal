package tivio.android.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tivio.Tivio;
import com.tivio.TivioPlayerSource;
import com.tivio.TivioPlayerWrapper;
import com.tivio.TivioPlayerWrapperDelegate;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity implements TivioPlayerWrapperDelegate {

    ExoPlayer exoPlayer;
    PlayerView exoPlayerView;

    TivioPlayerWrapper tivioPlayerWrapper;

    String videoURL = "https://firebasestorage.googleapis.com/v0/b/tivio-production-input-admin/o/organizations%2Fl0Q4o9TigUUTNe6TYAqR%2Fchannels%2FhL1LtUhcsZuygmi1HjJI%2Fsections%2FNQlUj81wIf0Ev6qQzRIs%2Fvideos%2F2hAoiSigTZ6Q4QyAsWAi.mp4?alt=media&token=041e129c-c034-42c5-8db0-9fb13c0e8d4e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Example", "onCreate");
        Tivio.init(this,"QzY8vor8x0G6rooCWqzI", new String[]{});
        tivioPlayerWrapper = Tivio.getPlayerWrapper();

        setContentView(R.layout.activity_main);

        exoPlayer = new ExoPlayer.Builder(this).build();
        exoPlayer.addListener(new Player.Listener() {

            private void getCurrentPlayerPosition() {
                tivioPlayerWrapper.reportTimeProgress((int) exoPlayer.getCurrentPosition());

                if (exoPlayer.isPlaying()) {
                    exoPlayerView.postDelayed(this::getCurrentPlayerPosition, 500);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    exoPlayerView.postDelayed(this::getCurrentPlayerPosition, 500);
                }
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if(playbackState == Player.STATE_ENDED) {
                    tivioPlayerWrapper.reportPlaybackEnded();
                }
            }
        });

        exoPlayerView = findViewById(R.id.idExoPlayerView);
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tivioPlayerWrapper.setSource(TivioPlayerSource.startWithChannel(
                        "prima hd",
                        "timeshift",
                        "https://firebasestorage.googleapis.com/v0/b/tivio-production-input-admin/o/organizations%2Fl0Q4o9TigUUTNe6TYAqR%2Fchannels%2FhL1LtUhcsZuygmi1HjJI%2Fsections%2FNQlUj81wIf0Ev6qQzRIs%2Fvideos%2F2hAoiSigTZ6Q4QyAsWAi.mp4?alt=media&token=041e129c-c034-42c5-8db0-9fb13c0e8d4e",
                        Timestamp.valueOf("2022-01-10 12:00:00").getTime(),
                        Timestamp.valueOf("2022-01-10 13:40:00").getTime(),
                        Timestamp.valueOf("2022-01-10 12:00:00").getTime(),
                        0
                ));
            }
        });

        tivioPlayerWrapper.delegate = this;
    }

    @Override
    public void setSource(TivioPlayerSource source) {
        MediaItem playableItem = MediaItem.fromUri(source.uri);

        exoPlayer.setMediaItem(playableItem);
        exoPlayer.prepare();
        exoPlayer.seekTo(source.startPosition);
        exoPlayer.play();
    }

    @Override
    public void seekTo(int miliseconds) {
        exoPlayer.seekTo(miliseconds);
    }
}
