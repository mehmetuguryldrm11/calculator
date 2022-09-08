import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    final private static int AUDIO_DEVICE_INDEX = 0;
    final private static int FRAME_RATE = 30;


    public static void main(String[] args) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception {

        run();
    }
                public static void run() {
                    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                            "rtmp://my-streaming-server/app_name_here/instance_name/stream_name", 2);
                    // Thread for audio capture, this could be in a nested private class if you prefer...
                    try {
                        AudioFormat audioFormat = new AudioFormat(48000.0F, 16, 2, true, false);

                        // Get TargetDataLine with that format
                        Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
                        Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);
                        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                        // Open and start capturing audio
                        TargetDataLine line = (TargetDataLine) mixer.getLine(dataLineInfo);
                        line.open(audioFormat);
                        line.start();

                        int sampleRate = (int) audioFormat.getSampleRate();
                        int numChannels = audioFormat.getChannels();

                        // Let's initialize our audio buffer...
                        int audioBufferSize = sampleRate * numChannels;
                        byte[] audioBytes = new byte[audioBufferSize];

                        // Using a ScheduledThreadPoolExecutor vs a while loop with
                        // a Thread.sleep will allow
                        // us to get around some OS specific timing issues, and keep
                        // to a more precise
                        // clock as the fixed rate accounts for garbage collection
                        // time, etc
                        // a similar approach could be used for the webcam capture
                        // as well, if you wish
                        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
                        exec.scheduleAtFixedRate(new Runnable() {

                            public void run() {
                                try {
                                    // Read from the line... non-blocking
                                    int nBytesRead = line.read(audioBytes, 0, line.available());

                                    // Since we specified 16 bits in the AudioFormat,
                                    // we need to convert our read byte[] to short[]
                                    // (see source from FFmpegFrameRecorder.recordSamples for AV_SAMPLE_FMT_S16)
                                    // Let's initialize our short[] array
                                    int nSamplesRead = nBytesRead / 2;
                                    short[] samples = new short[nSamplesRead];

                                    // Let's wrap our short[] into a ShortBuffer and
                                    // pass it to recordSamples
                                    ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                                    ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);

                                    // recorder is instance of
                                    // org.bytedeco.javacv.FFmpegFrameRecorder
                                    recorder.recordSamples(sampleRate, numChannels, sBuff);
                                } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 0, (long) 1000 / FRAME_RATE, TimeUnit.MILLISECONDS);
                    } catch (LineUnavailableException e1) {
                        e1.printStackTrace();
                        System.exit(0);
                    }
                }
}
