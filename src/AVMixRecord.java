
import muymuy.StreamRecorder;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import javax.sound.sampled.*;

public class AVMixRecord {

    public static void recordWebcamAndMicrophone(int WEBCAM_DEVICE_INDEX, int AUDIO_DEVICE_INDEX, String outputFile,
                                                 int captureWidth, int captureHeight, int FRAME_RATE) throws org.bytedeco.javacv.FrameGrabber.Exception {
        long startTime = 0;
        long videoTS = 0;

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
        grabber.setImageWidth(captureWidth);
        grabber.setImageHeight(captureHeight);
        System.out.println("Start grabbing the camera...");
        int isTrue = 0;
        try {
            grabber.start();
            isTrue += 1;
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e2) {
            if (grabber != null) {
                try {
                    grabber.restart();
                    isTrue += 1;
                } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                    isTrue -= 1;
                    try {
                        grabber.stop();
                    } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                        isTrue -= 1;
                    }
                }
            }
        }
        if (isTrue < 0) {
            System.err.println("The camera failed to turn on for the first time, and the attempt to restart also failed!");
            return;
        } else if (isTrue < 1) {
            System.err.println("Failed to turn on the camera！");
            return;
        } else if (isTrue == 1) {
            System.err.println("Camera turned on successfully！");
        } else if (isTrue == 1) {
            System.err.println("The camera failed to turn on for the first time, but restarted successfully！");
        }

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, captureWidth, captureHeight, 2);
        recorder.setInterleaved(true);

        recorder.setVideoOption("tune", "zerolatency");

        recorder.setVideoOption("preset", "ultrafast");


        recorder.setVideoOption("crf", "25");
        recorder.setVideoBitrate(2000000);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("flv");
        recorder.setFrameRate(FRAME_RATE);
        recorder.setGopSize(FRAME_RATE * 2);
        recorder.setAudioOption("crf", "0");
        recorder.setAudioQuality(0);
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setAudioChannels(2);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        System.out.println("start recording...");

        try {
            recorder.start();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e2) {
            if (recorder != null) {
                System.out.println("Failed to close, try restarting");
                try {
                    recorder.stop();
                    recorder.start();
                } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                    try {
                        System.out.println("Failed to open, close recording");
                        recorder.stop();
                        return;
                    } catch (org.bytedeco.javacv.FrameRecorder.Exception e1) {
                        return;
                    }
                }
            }

        }

        new Thread(new StreamRecorder(recorder,AUDIO_DEVICE_INDEX,FRAME_RATE)).start();


        CanvasFrame cFrame = new CanvasFrame("Capture Preview", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        Frame capturedFrame = null;

        while ((capturedFrame = grabber.grab()) != null) {
            if (cFrame.isVisible()) {

                cFrame.showImage(capturedFrame);
            }

            if (startTime == 0)
                startTime = System.currentTimeMillis();


            videoTS = 1000 * (System.currentTimeMillis() - startTime);

            if (videoTS > recorder.getTimestamp()) {
                System.out.println("Lip-flap correction: " + videoTS + " : " + recorder.getTimestamp() + " -> "
                        + (videoTS - recorder.getTimestamp()));

                recorder.setTimestamp(videoTS);
            }

            try {
                recorder.record(capturedFrame);
            } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
                System.out.println("An exception occurred in the recording frame and nothing was done");
            }
        }

        cFrame.dispose();
        try {
            if (recorder != null) {
                recorder.stop();
            }
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            System.out.println("Failed to close recorder");
            try {
                if (recorder != null) {
                    grabber.stop();
                }
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                System.out.println("Failed to close the camera");
                return;
            }
        }
        try {
            if (recorder != null) {
                grabber.stop();
            }
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            System.out.println("Failed to close the camera");
        }
    }


    public static void call() throws InterruptedException, LineUnavailableException ,FrameGrabber.Exception{
        int FRAME_RATE = 720;
        recordWebcamAndMicrophone(0,4, "localAudio.flv",50,50,FRAME_RATE);//对应上面的方法体
    }

    public static void main(String[] args) throws InterruptedException, LineUnavailableException ,FrameGrabber.Exception{
        call();
    }

}