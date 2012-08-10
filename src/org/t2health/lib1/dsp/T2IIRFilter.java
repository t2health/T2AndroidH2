package org.t2health.lib1.dsp;


public class T2IIRFilter extends T2Filter {

	int mSmoothingFactor;
	int mCurrentValue;
	
	int[] mZeros; 
	int[] mPoles;	
	int[] mZeroDelayLine;
	int[] mPoleDelayLine;
	double[] mdZeros; 
	double[] mdPoles;	
	double[] mdZeroDelayLine;
	double[] mdPoleDelayLine;
	
	int mNumZeros, mNumPoles;
	int mScale;
	double mGain;
	
	public T2IIRFilter(int[] zeros, int[] poles, int scale) {
		mPoles = poles;
		mNumPoles = mPoles.length;
		mPoleDelayLine = new int[mNumPoles];

		mZeros = zeros;
		mNumZeros = mZeros.length;
		mZeroDelayLine = new int[mNumZeros];
		mScale = scale;
	}

	public T2IIRFilter(double[] zeros, double[] poles, double gain) {
		mdPoles = poles;
		mNumPoles = mdPoles.length;
		mdPoleDelayLine = new double[mNumPoles];

		mdZeros = zeros;
		mNumZeros = mdZeros.length;
		mdZeroDelayLine = new double[mNumZeros];
		mGain = gain;
	}
	
	
	public T2IIRFilter(int[] zeros, double[] poles, int scale) {
		mZeros = zeros;
		mNumZeros = mZeros.length;
		mZeroDelayLine = new int[mNumZeros];

		mNumPoles = poles.length;
		mPoles = new int[mNumPoles];
		mPoleDelayLine = new int[mNumPoles];
		for (int i = 0; i < poles.length; i++ ) {
			double d = poles[i] * (double) scale;
			int ix = (int) d;
			mPoles[i] = ix;
		}
		
		mScale = scale;
	}
	
	@Override    
    public int filter(int inputSample) {
    
		for (int i = 0; i < mNumZeros - 1; i++) {
			mZeroDelayLine[i] = mZeroDelayLine[i + 1];
		}
		mZeroDelayLine[mNumZeros - 1] = inputSample; 

		for (int i = 0; i < mNumPoles - 1; i++) {
			mPoleDelayLine[i] = mPoleDelayLine[i + 1];
		}

		// We need to clear out the last pole delay value or else we'll
		// be adding the last iteration value t the current calculation
		mPoleDelayLine[mNumPoles - 1] = 0;

		for (int i = 0; i < mNumZeros; i++) {
			mPoleDelayLine[mNumPoles - 1] += mZeros[i] * mZeroDelayLine[i];
		}

		for (int i = 0; i < mNumPoles; i++) {
			mPoleDelayLine[mNumPoles - 1] += mPoles[i] * mPoleDelayLine[i];
		}
		
	//	Log.i("SensorData", String.format(",Zeros: %d, %d, %d, %d, Poles: %d, %d, %d, %d *** %d", mDelayLine[0], mDelayLine[1], mDelayLine[2], mDelayLine[3], mPoleDelayLine[0], mPoleDelayLine[1], mPoleDelayLine[2], mPoleDelayLine[3], mPoleDelayLine[3])); 		
		
		return mPoleDelayLine[mNumPoles - 1]/4116 >> 13;
    }	
	
    public double dfilter(int inputSample) {
    
		for (int i = 0; i < mNumZeros - 1; i++) {
			mdZeroDelayLine[i] = mdZeroDelayLine[i + 1];
		}
		mdZeroDelayLine[mNumZeros - 1] = inputSample / mGain; 

		for (int i = 0; i < mNumPoles - 1; i++) {
			mdPoleDelayLine[i] = mdPoleDelayLine[i + 1];
		}
		
		// We need to clear out the last pole delay value or else we'll
		// be adding the last iteration value t the current calculation
		mdPoleDelayLine[mNumPoles - 1] = 0;
		
		for (int i = 0; i < mNumZeros; i++) {
			mdPoleDelayLine[mNumPoles - 1] += mdZeros[i] * mdZeroDelayLine[i];
		}

		for (int i = 0; i < mNumPoles; i++) {
			mdPoleDelayLine[mNumPoles - 1] += mdPoles[i] * mdPoleDelayLine[i];
		}
		
		
		
//		Log.i("SensorData", String.format(",Zeros: %g, %g, %g, %g, Poles: %g, %g, %g, %g", mdDelayLine[0], mdDelayLine[1], mdDelayLine[2], mdDelayLine[3], mdPoleDelayLine[0], mdPoleDelayLine[1], mdPoleDelayLine[2], mdPoleDelayLine[3])); 		
		
		return mdPoleDelayLine[mNumPoles - 1];
    }	
	
}
