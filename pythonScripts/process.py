import sys
import numpy as np
import scipy.io.wavfile
from scipy import fftpack
from scipy.signal import find_peaks

def __main__():

# Get input file from command line, has to be wav format
    input_file = str(sys.argv[1])

# read in file as wav file
    rate, data = scipy.io.wavfile.read(input_file)

# the FFT of the signal
    sig_fft = fftpack.fft(data)

# and the power
    power = np.abs(sig_fft)

# corresponding frequencies
    sample_freq = fftpack.fftfreq(data.size, d=(1/rate))

    pos_mask = np.where(sample_freq > 0)
    freqs = sample_freq[pos_mask]
    peak_freq = freqs[power[pos_mask].argmax()]

# get the filtered signal, filtering only to have high frequencies
    high_freq_fft = sig_fft.copy()
    high_freq_fft[np.abs(sample_freq) > peak_freq] = 0
    filtered_sig = fftpack.ifft(high_freq_fft)

# now using filtered signal, find the peaks in our signal
    peaks, _ = find_peaks(filtered_sig, height=0, width=150, prominence=8000, distance=2000)

    peak1 = peaks[0] / rate
    peak2 = peaks[1] / rate

    time_difference = peak2 - peak1

    bounce_height = (1.23 * (time_difference - 0.025) ** 2.0)

    return bounce_height



