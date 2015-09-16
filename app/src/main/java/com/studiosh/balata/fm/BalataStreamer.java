package com.studiosh.balata.fm;

import android.media.AudioManager;
import android.media.MediaPlayer;

public class BalataStreamer implements MediaPlayer.OnPreparedListener {
	private final String OGG_STREAM = "http://stream.balata.fm/stream.ogg";
	private final String MP3_STREAM = "http://stream.balata.fm/stream.mp3";
	private MediaPlayer mMediaPlayer;
	private Boolean mStreamStarted = false;
	private Boolean mPrevStreamState = false;
	private BalataController mController;

	public BalataStreamer() {
		mController = BalataController.getInstance();

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try {
			mMediaPlayer.setDataSource(OGG_STREAM);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mMediaPlayer.setOnPreparedListener(this);
	}

	public Boolean isStreamStarted() {
		return mStreamStarted;
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mStreamStarted) {
			mp.start();
		}

		mController.setStreamingState(BalataController.StreamingState.PLAYING);
	}

	public void play() {
		if (!mMediaPlayer.isPlaying() && !mStreamStarted) {
			mStreamStarted = true;
			mMediaPlayer.prepareAsync();
			mController.setStreamingState(BalataController.StreamingState.BUFFERING);
		}
	}

	public void pause(Boolean pause) {
		if (pause) {
			mPrevStreamState = mStreamStarted;
			mMediaPlayer.pause();
			mController.setStreamingState(BalataController.StreamingState.PAUSED);
		} else {
			if (mPrevStreamState) {
				mMediaPlayer.start();
				mController.setStreamingState(BalataController.StreamingState.PLAYING);
			}
		}
	}

	public void stop() {
		if (mMediaPlayer.isPlaying()) {
			mStreamStarted = false;
			mMediaPlayer.stop();
			mController.setStreamingState(BalataController.StreamingState.STOPPED);
		}
	}
	
	public void destroy() {
		stop();
		mMediaPlayer = null;
	}
}
