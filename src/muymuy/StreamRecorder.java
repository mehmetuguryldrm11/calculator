package muymuy;

import org.bytedeco.javacv.FFmpegFrameRecorder;

import javax.sound.sampled.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class StreamRecorder implements Runnable{

    FFmpegFrameRecorder recorder;

    int AUDIO_DEVICE_INDEX;

    int FRAME_RATE;

    public StreamRecorder(FFmpegFrameRecorder recorder, int AUDIO_DEVICE_INDEX, int FRAME_RATE) {
        this.recorder = recorder;
        this.AUDIO_DEVICE_INDEX = AUDIO_DEVICE_INDEX;
        this.FRAME_RATE = FRAME_RATE;
    }

    public void run() {

        AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);

        Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
        Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {

            // =(TargetDataLine)mixer.getLine(dataLineInfo);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            line.open(audioFormat);
            line.start();

            int sampleRate = (int) audioFormat.getSampleRate();

            int numChannels = audioFormat.getChannels();

            int audioBufferSize = sampleRate * numChannels;
            byte[] audioBytes = new byte[audioBufferSize];

            ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
            exec.scheduleAtFixedRate(new AudioScheduleTaskThread(recorder,line,audioBytes,sampleRate,numChannels), 0, (long) 1000 / FRAME_RATE, TimeUnit.MILLISECONDS);
        } catch (LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }
}