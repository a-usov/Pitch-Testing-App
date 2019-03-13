package com.example.conal.soundrecord;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.beatroot.Peaks;
import be.tarsos.dsp.io.PipedAudioStream;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.util.fft.FFT;

class SoundProcessing {
    // set parameters about sound and FFT
    private final int sampleRate;
    private final int bufferSize;
    private final List<Double> sound = new ArrayList<>();
    // this array is used for displaying graph in results
    private double[] soundArray;

    public double[] getSoundArray() {
        return soundArray;
    }

    public SoundProcessing(int sampleRate, int bufferSize) {
        this.sampleRate = sampleRate;
        this.bufferSize = bufferSize;
    }

    public Result process(String file) {
        // setup our audio stream of our wav file
        PipedAudioStream f = new PipedAudioStream(file);
        TarsosDSPAudioInputStream stream = f.getMonoStream(sampleRate, 0);

        // create processor that applies fft and filtering to chunks of sound
        AudioDispatcher dispatcher = new AudioDispatcher(stream, bufferSize, 0);

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                FFT fft = new FFT(bufferSize);

                // get the chunk of data, create arrays to hold transformed data and its power
                float[] audioFloatBuffer = audioEvent.getFloatBuffer();

                float[] transformBuffer = new float[bufferSize * 2];
                float[] power = new float[bufferSize / 2];

                // copy data to our new array
                System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);

                // apply fft on data and get power
                fft.forwardTransform(transformBuffer);
                fft.modulus(transformBuffer, power);

                // TODO redo filtering

                // invert fft
                fft.backwardsTransform(transformBuffer);

                // copy new data back into original stream
                System.arraycopy(transformBuffer, 0, audioFloatBuffer, 0, audioFloatBuffer.length);

                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] audioFloatBuffer = audioEvent.getFloatBuffer();

                int counter = 0;
                double average = 0;

                // we downsample sound from 44100hz to 11025hz
                // for faster processing
                for (float value : audioFloatBuffer) {
                    if (counter == 3) {
                        sound.add(average / 4);
                        counter = 0;
                        average = 0;
                    } else {
                        average += (double) value;
                        counter++;
                    }
                }


                return true;
            }

            @Override
            public void processingFinished() {
            }
        });

        // create, run and wait for thread to finish
        Thread t1 = new Thread(dispatcher);

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // copy over Arraylist to primitive array
        // this is as Peak finding uses primitive array
        // but can't use it from the start as we don't know
        // what size is should be at the start
        soundArray = new double[sound.size()];
        for (int i = 0; i < soundArray.length; i++) {
            soundArray[i] = sound.get(i);
        }

        List<Integer> peaks = Peaks.findPeaks(soundArray, 6250, 0.10);

        // return Result or null if peak finding wasn't successful
        try {
            return new Result(peaks.get(0), peaks.get(1), (peaks.get(1) - (double) peaks.get(0)) / sampleRate * 4);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
