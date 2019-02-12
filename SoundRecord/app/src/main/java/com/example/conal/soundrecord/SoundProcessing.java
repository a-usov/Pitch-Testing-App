package com.example.conal.soundrecord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.beatroot.Peaks;
import be.tarsos.dsp.io.PipedAudioStream;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.util.fft.FFT;


public class SoundProcessing {

    // set parameters about sound and FFT
    private int sampleRate;
    private int bufferSize;
    private int windowSize = 0;

    private List<Double> sound = new ArrayList<>();

    private float maxGlobalPower = 0;
    private double maxPowerFreq;

    public SoundProcessing(int sampleRate, int bufferSize){
        this.sampleRate = sampleRate;
        this.bufferSize = bufferSize;
    }

    public double process(File file){
        // setup our audio stream of our wav file
        PipedAudioStream f = new PipedAudioStream(file.toString());
        TarsosDSPAudioInputStream stream = f.getMonoStream(sampleRate,0);

        // create processor that applies fft and filtering to chunks of sound
        AudioDispatcher dispatcher = new AudioDispatcher(stream, bufferSize, windowSize);

        dispatcher.addAudioProcessor(new AudioProcessor() {

            @Override
            public boolean process(AudioEvent audioEvent) {
                FFT fft = new FFT(bufferSize);

                // get the chunk of data, create arrays to hold transformed data and its power
                float [] audioFloatBuffer = audioEvent.getFloatBuffer();

                float [] transformBuffer = new float[bufferSize*2];
                float[] power = new float[bufferSize/2];

                System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);

                // apply fft on data and get power
                fft.forwardTransform(transformBuffer);
                fft.modulus(transformBuffer, power);

                // max power entry
                int maxIdx = 1;
                {
                    float m = power[1];
                    for (int i = 2; i < power.length; ++i) {
                        if (power[i] > m) {
                            m = power[i];
                            maxIdx = i;
                        }
                    }
                }

                if (power[maxIdx] > maxGlobalPower) {
                    maxGlobalPower = power[maxIdx];
                    maxPowerFreq = fft.binToHz(maxIdx, sampleRate);
                }

                // cut off above that power
                for (int i = (maxIdx+1)*2; i < transformBuffer.length; ++i)
                    transformBuffer[i] = 0;

                fft.backwardsTransform(transformBuffer);

                System.arraycopy(transformBuffer, 0, audioFloatBuffer, 0, audioFloatBuffer.length);

                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        //dispatcher.addAudioProcessor(new BandPass((float) maxPowerFreq, 100, sampleRate));

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float [] audioFloatBuffer = audioEvent.getFloatBuffer();

                for (float value : audioFloatBuffer){
                    sound.add((double) value);
                }

                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        /*try {
            dispatcher.addAudioProcessor(new WriterProcessor(stream.getFormat(), new RandomAccessFile("transform.wav", "rw")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        Thread t1 = new Thread(dispatcher);

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        double[] soundArray = new double[sound.size()];
        for (int i = 0; i < soundArray.length; i++) {
            soundArray[i] = sound.get(i);
        }

        List<Integer> peaks = Peaks.findPeaks(soundArray, 150, 0.3);

        return ( (peaks.get(1) - (double)peaks.get(0)) / sampleRate);
    }
}
