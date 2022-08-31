package cl.eye;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class JavaAudio extends Application {

    private static final int frameRate = 24;// Recorded frame rate
    private static boolean isStop = false;
    private static TargetDataLine line;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("lingkang- Desktop video master ");
        ImageView imageVideo = new ImageView();// For software recording and display
        imageVideo.setFitWidth(800);
        imageVideo.setFitHeight(600);
        Button button = new Button(" Stop recording ");
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                isStop = true;
                if (line != null) {
// Stop sound input immediately
                    try {

                        line.close();
                    } catch (Exception e) {

                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("info");
                alert.setHeaderText(" Recording has stopped ");
                alert.setOnCloseRequest(event1 -> alert.hide());
                alert.showAndWait();
            }
        });
        VBox box = new VBox();
        box.getChildren().addAll(button, imageVideo);
        primaryStage.setScene(new Scene(box));
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
// Stop on exit
                isStop = true;
                System.exit(0);
            }
        });
// Frame recording
// window It is recommended to use FFmpegFrameGrabber("desktop") Screen capture
        FrameGrabber grabber = new FFmpegFrameGrabber("desktop");
        grabber.setFormat("gdigrab");
        grabber.setFrameRate(frameRate);// Frame acquisition interval
// Capture the specified area , If not set, it will be full screen
        grabber.setImageHeight(600);
        grabber.setImageWidth(800);
// grabber.setOption("offset_x", "200");
// grabber.setOption("offset_y", "200");// The size must be set to specify the starting point of the area , Parameters can refer to FFmpeg Enter the reference
        grabber.start();
        File file = new File("D://output.avi");
        if (file.exists())
            file.delete();
        /* // Live streaming final FFmpegFrameRecorder recorder = new FFmpegFrameRecorder( "rtmp://my-streaming-server/app_name_here/instance_name/stream_name", grabber.getImageWidth(), grabber.getImageHeight(), 2);*/
// Used to store video , call stop after , Need to release , The file will be output in the specified location ,, Here I save it to D disc
        FFmpegFrameRecorder recorder = FFmpegFrameRecorder.createDefault(file, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setInterleaved(true);
        recorder.setVideoOption("tune", "zerolatency");// Speed up
        recorder.setFrameRate(frameRate);// Set frame rate , important ！
// Key frame interval, in our case every 2 seconds -> 30 (fps) * 2 = 60
        recorder.setGopSize(frameRate * 2);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// code , Using encoding can make video occupy less memory , Choose according to the actual situation
// Add audio recording
// Immutable audio
        recorder.setAudioOption("crf", "0");
// The highest sound quality
        recorder.setAudioQuality(0);
// 192 Kbps
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setAudioChannels(2);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.start();
// 44100 16 channel
        AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
// Can capture different channels
        line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
// Recording sound
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    line.open(audioFormat);
                    line.start();
                    final int sampleRate = (int) audioFormat.getSampleRate();
                    final int numChannels = audioFormat.getChannels();
// buffer
                    final int audioBufferSize = sampleRate * numChannels;
                    final byte[] audioBytes = new byte[audioBufferSize];
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {

                            try {

                                if (isStop) {
// Stop the recording
                                    line.stop();
                                    line.close();
                                    System.out.println(" Has stopped ！");
                                    timer.cancel();
                                }
// Read audio
// read It will block
                                int readLenth = 0;
                                while (readLenth == 0)
                                    readLenth = line.read(audioBytes, 0, line.available());
// audioFormat Defines the audio input as 16 Base number , Bytes need to be [] Convert to short bytes []
// FFmpegFrameRecorder.recordSamples In the source AV_SAMPLE_FMT_S16
                                int rl = readLenth / 2;
                                short[] samples = new short[rl];
// short[] Convert to ShortBuffer
                                ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                                ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, rl);
// Record
                                recorder.recordSamples(sampleRate, numChannels, sBuff);
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }
                    }, 1000, 1000 / frameRate);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

// Get a frame of screen capture
                    Frame frame = null;
// Screen Recording , Since the audio has been recorded , You need to adjust the recording time
// That is, the above calls recorder.recordSamples Need to reallocate time , Otherwise, the video output duration is equal to the actual time Of 2 times
                    while ((frame = grabber.grab()) != null) {

                        if (isStop) {

                            try {

// stop it
                                recorder.stop();
                                grabber.stop();
// Free memory , We all know c/c++ Resources need to be released manually
                                recorder.release();
                                grabber.release();
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            break;
                        }
// Put this frame on the record
                        recorder.record(frame);
                        Image convert = new JavaFXFrameConverter().convert(frame);
                        imageVideo.setImage(convert);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void main(String[] args) {

        launch(args);
    }
}