package muymuy;

import org.bytedeco.javacv.FFmpegFrameRecorder;

import javax.sound.sampled.TargetDataLine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;


public class AudioScheduleTaskThread implements Runnable{

    FFmpegFrameRecorder recorder;

    TargetDataLine line;

    byte[] audioBytes;

    int sampleRate;

    int numChannels;

    AudioScheduleTaskThread(FFmpegFrameRecorder recorder, TargetDataLine line, byte[] audioBytes, int sampleRate, int numChannels) {
        this.recorder = recorder;
        this.line = line;
        this.audioBytes = audioBytes;
        this.sampleRate = sampleRate;
        this.numChannels = numChannels;
    }

    public void run() {
        try {

            int nBytesRead = line.read(audioBytes, 0, line.available());

            int nSamplesRead = nBytesRead / 2;
            short[] samples = new short[nSamplesRead];
            /**
             * ByteBuffer.wrap(audioBytes)
             * ByteBuffer.order(ByteOrder)
             * ByteBuffer.asShortBuffer()
             * ShortBuffer.get(samples)
             */
            ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);

            ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);
            //shortBuffer
            recorder.recordSamples(sampleRate, numChannels, sBuff);
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }
}
