package com.diotek.diospeech.tts;

import android.media.AudioTrack;
import com.diotek.diospeech.tts.AudioOutputInterface;

/* loaded from: classes.dex */
public class DefaultAudioOutputInterfaceImpl implements AudioOutputInterface {
    protected AudioTrack audioTrack;
    protected int bufferSampleSize;
    protected int initialWriteSamplesBeforeStart;
    protected int sampleRate;
    protected int writeSamplesBeforeStart;

    public DefaultAudioOutputInterfaceImpl() {
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        this.sampleRate = 22050;
        this.bufferSampleSize = this.sampleRate / 2;
        this.initialWriteSamplesBeforeStart = this.bufferSampleSize / 5;
        this.writeSamplesBeforeStart = this.initialWriteSamplesBeforeStart;
        this.audioTrack = new AudioTrack(3, this.sampleRate, 2, 2, this.bufferSampleSize * 2, 1);
    }

    @Override // com.diotek.diospeech.tts.AudioOutputInterface
    public void startPlaying(int requestSampleRate) throws AudioOutputInterface.ResultCode {
        if (this.audioTrack != null) {
            this.audioTrack.release();
            this.audioTrack = null;
        }
        this.sampleRate = requestSampleRate;
        this.bufferSampleSize = this.sampleRate / 2;
        this.initialWriteSamplesBeforeStart = this.bufferSampleSize / 5;
        this.writeSamplesBeforeStart = this.initialWriteSamplesBeforeStart;
        this.audioTrack = new AudioTrack(3, this.sampleRate, 2, 2, this.bufferSampleSize * 2, 1);
    }

    @Override // com.diotek.diospeech.tts.AudioOutputInterface
    public void waitUntilProcessed() throws AudioOutputInterface.ResultCode {
        if (-1 < this.writeSamplesBeforeStart) {
            this.audioTrack.play();
        }
        this.audioTrack.flush();
        this.audioTrack.stop();
        this.writeSamplesBeforeStart = this.initialWriteSamplesBeforeStart;
    }

    @Override // com.diotek.diospeech.tts.AudioOutputInterface
    public void writeAudioData(short[] audioData, int sampleCount) throws AudioOutputInterface.ResultCode {
        if (sampleCount < this.writeSamplesBeforeStart) {
            this.writeSamplesBeforeStart -= sampleCount;
        } else if (this.writeSamplesBeforeStart != -1) {
            this.writeSamplesBeforeStart = 0;
        }
        this.audioTrack.write(audioData, 0, sampleCount);
        if (this.writeSamplesBeforeStart == 0) {
            this.audioTrack.play();
            this.writeSamplesBeforeStart = -1;
        }
    }
}
