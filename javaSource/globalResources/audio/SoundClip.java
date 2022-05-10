package globalResources.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Used to load and play audio files
 */
public class SoundClip
{
	private Clip clip = null;
	private FloatControl gainControl;
	private AudioInputStream inputStream;
	
	/**
	 * Loads a sounds file
	 * @param path file path leading to sound file
	 */
	public SoundClip(String path)
	{
		try
		{
			InputStream audioSource = SoundClip.class.getResourceAsStream(path);
			InputStream bufferedIn = new BufferedInputStream(audioSource);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
			AudioFormat baseFormat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
														baseFormat.getSampleRate(),
														16,
														baseFormat.getChannels(),
														baseFormat.getChannels() * 2,
														baseFormat.getSampleRate(),
														false);
			AudioInputStream decodedAudioInputStram = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
			inputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
			inputStream.mark((int)inputStream.getFrameLength());
			
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStram);
			
			gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Begins playing the sound
	 */
	public void play()
	{
		if (clip == null)
		{
			return;
		}
		stop();
		clip.setFramePosition(0);
		while (!clip.isRunning())
		{
			clip.start();
		}
	}
	
	/**
	 * Stops playing the sounds
	 */
	public void stop()
	{
		if (clip.isRunning())
		{
			clip.stop();
		}
	}
	
	/**
	 * Closes the data stream
	 */
	public void close()
	{
		stop();
		clip.drain();
		clip.close();
	}
	
	/**
	 * Sets the sound to play on loop
	 */
	public void loop()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		play();
	}
	
	/**
	 * Sets the volume of the sound
	 * @param volume sound volume
	 */
	public void setVolume(float volume)
	{
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	/**
	 * Checks if the sound is playing
	 * @return if the sound is playing
	 */
	public boolean isRunning()
	{
		if (clip == null) return false;
		return clip.isRunning();
	}
}
