using UnityEngine;
using UnityEngine.Rendering;

public static class AudioAnalysis
{
    static float[] samples = new float[1024];
    static float samplingFrequency = AudioSettings.outputSampleRate;

    public static float[] GetSamples(AudioSource source)
    {
        source.GetOutputData(samples, 0);
        return samples;
    }

    public static float[] GetSpectrum(AudioSource source)
    {
        source.GetSpectrumData(samples, 0, FFTWindow.Hamming);
        return samples;
    }

    public static float ConcentrationAroundPeak(float peakFrequency)
    {
        int index = (int) (peakFrequency * samples.Length / (0.5f * samplingFrequency));
        int centredBand = samples.Length / 200;
        float total = 0;
        float insideBand = 0;

        for (int i = 0; i < samples.Length - 5; i++)
        {
            total += samples[i] * samples[i];
            if(Mathf.Abs(i - index) < centredBand)
            {
                insideBand += samples[i] * samples[i];
            }
        }
        return insideBand/total;
    }

    public static float ComputeSpectrumPeak(AudioSource source, bool interpolate)
    {
        source.GetSpectrumData(samples, 0, FFTWindow.Hamming);
        float max = 0;
        float index = 0;

        for (int i = 0; i< 3 * samples.Length / 4; i++) 
        { 
            if (samples[i] > max)
            {
                max = samples[i];
                index = i;
            }
        }

        if(interpolate && index > 0 && index < samples.Length - 1)
        {
            float dL = samples[(int)index] - samples[(int) index - 1];
            float dR = samples[(int)index] - samples[(int) index + 1];
            index += (dL - dR) / (2 * (dL + dR));
        }
        float peak = index * 0.5f * samplingFrequency / samples.Length;

        return peak;
    }

    public static float MeanEnergy(AudioSource source)
    {
        source.GetOutputData(samples, 0);
        float sum = 0;

        for (int i = 0; i < samples.Length; i++)
        {
            sum += samples[i] * samples[i];
        }

        return sum/samples.Length;
    }

    public static float ComputeRMS(AudioSource source)
    {
        float meanEnergy = MeanEnergy(source);
        return Mathf.Sqrt(meanEnergy);
    }

    public static float ConvertToDB(float value)
    {
        float reference = 1e-12f;
        if (value < reference) 
            value = reference;
        return 10f * Mathf.Log10(value/reference); 
    }

    public static float[] FreqBands(AudioSource source)
    {
        source.GetSpectrumData(samples, 0, FFTWindow.Hamming);
        int nbans = 7;
        float[] bands = new float[nbans];
        int numSamples = 8 * samples.Length / 1024;
        int iniSample = 0;

        for(int i = 0; i<nbans; i++)
        {
            float val = 0;
            for(int j = iniSample; j< iniSample + numSamples; j++)
            {
                val += samples[j] * samples[j];
            }
            bands[i] = ConvertToDB(val/numSamples);
            iniSample += numSamples;
            numSamples *= 2;
        }
        return bands;
    }
}
