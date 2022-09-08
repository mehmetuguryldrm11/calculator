
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

public class AudioConvert {

    /**
     * 通用音频格式参数转换
     *
     * @param inputFile
     *            -导入音频文件
     * @param outputFile
     *            -导出音频文件
     * @param audioCodec
     *            -音频编码
     * @param sampleRate
     *            -音频采样率
     * @param audioBitrate
     *            -音频比特率
     */
    public static void convert(String inputFile, String outputFile, int audioCodec, int sampleRate, int audioBitrate,int audioChannels) {
        Frame audioSamples = null;
        // 音频录制（输出地址，音频通道）
        FFmpegFrameRecorder recorder = null;
        //抓取器
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);

        // 开启抓取器
        if (start(grabber)) {
            recorder = new FFmpegFrameRecorder(outputFile, audioChannels);
            recorder.setAudioOption("crf", "0");
            recorder.setAudioCodec(audioCodec);
            recorder.setAudioBitrate(audioBitrate);
            recorder.setAudioChannels(audioChannels);
            recorder.setSampleRate(sampleRate);
            recorder.setAudioQuality(0);
            recorder.setAudioOption("aq", "10");
            // 开启录制器
            if (start(recorder)) {
                try {
                    // 抓取音频
                    while ((audioSamples = grabber.grab()) != null) {
                        recorder.setTimestamp(grabber.getTimestamp());
                        recorder.record(audioSamples);
                    }

                } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                    System.err.println("Crawl failed");
                } catch (Exception e) {
                    System.err.println("Recording failed");
                }
                stop(grabber);
                stop(recorder);
            }
        }

    }

    public static boolean start(FrameGrabber grabber) {
        try {
            grabber.start();
            return true;
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e2) {
            try {
                System.err.println("Failed to open the grabber for the first time, ready to restart the grabber...");
                grabber.restart();
                return true;
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                try {
                    System.err.println("Failed to restart grabber, closing grabber...");
                    grabber.stop();
                } catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
                    System.err.println("Failed to stop crawler！");
                }
            }

        }
        return false;
    }

    public static boolean start(FrameRecorder recorder) {
        try {
            recorder.start();
            return true;
        } catch (Exception e2) {
            try {
                System.err.println("Failed to open the recorder for the first time! Ready to restart the recorder...");
                recorder.stop();
                recorder.start();
                return true;
            } catch (Exception e) {
                try {
                    System.err.println("Failed to restart the recorder! Stopping recorder...");
                    recorder.stop();
                } catch (Exception e1) {
                    System.err.println("Failed to close recorder！");
                }
            }
        }
        return false;
    }

    public static boolean stop(FrameGrabber grabber) {
        try {
            grabber.flush();
            grabber.stop();
            return true;
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            return false;
        } finally {
            try {
                grabber.stop();
            } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
                System.err.println("Failed to close the grabber");
            }
        }
    }

    public static boolean stop(FrameRecorder recorder) {
        try {
            recorder.stop();
            recorder.release();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                recorder.stop();
            } catch (Exception e) {

            }
        }
    }

    /**
     * 以wav转mp3为例
     * @param args
     */
    public static void main(String[] args) {
        //pcm参数转换
        //convert("东部信息.wav", "eguid.wav", avcodec.AV_CODEC_ID_PCM_S16LE, 8000, 16000,1);
        //pcm转mp3编码示例
        //convert("C:\\1967.wav", "C:\\boonya.mp3", avcodec.AV_CODEC_ID_MP3, 8000, 1600,1);
        //FLV参数转换==本次研究方向
        convert("C:\\device-1001232032.h264", "C:\\device-1001232032.flv", avcodec.AV_CODEC_ID_FLV1, 8000, 16000,1);
    }

}