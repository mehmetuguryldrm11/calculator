package cl.eye;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

public interface SimplifiedGrabber {

    public Frame grab() throws FrameGrabber.Exception;

    public int getAudioChannels();

    public int getImageWidth();

    public int getImageHeight();

    public void start() throws FrameGrabber.Exception;

    public void stop() throws FrameGrabber.Exception;
}