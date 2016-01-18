package com.dwl.dbtemplate.util;

import java.lang.reflect.Array;

public class ArrayUtils {
	
	public static <T> T[] addAll(T[] array1, T[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		Class type1 = array1.getClass().getComponentType();

		Object[] joinedArray = (Object[]) Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		} catch (ArrayStoreException ase) {
			Class type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException(
						"Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase;
		}
		return (T[]) joinedArray;
	}

	public static boolean[] addAll(boolean[] array1, boolean[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		boolean[] joinedArray = new boolean[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static char[] addAll(char[] array1, char[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		char[] joinedArray = new char[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static short[] addAll(short[] array1, short[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		short[] joinedArray = new short[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static int[] addAll(int[] array1, int[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		int[] joinedArray = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static long[] addAll(long[] array1, long[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		long[] joinedArray = new long[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static float[] addAll(float[] array1, float[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		float[] joinedArray = new float[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static double[] addAll(double[] array1, double[] array2) {
		if (array1 == null)
			return clone(array2);
		if (array2 == null) {
			return clone(array1);
		}
		double[] joinedArray = new double[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static <T> T[] add(T[] array, T element) {
		Class type;
		if (array != null) {
			type = array.getClass();
		} else {
			if (element != null)
				type = element.getClass();
			else
				throw new IllegalArgumentException("Arguments cannot both be null");
		}
		Object[] newArray = (Object[]) copyArrayGrow1(array, type);
		newArray[(newArray.length - 1)] = element;
		return (T[]) newArray;
	}

	public static boolean[] add(boolean[] array, boolean element) {
		boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static byte[] add(byte[] array, byte element) {
		byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static char[] add(char[] array, char element) {
		char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static double[] add(double[] array, double element) {
		double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static float[] add(float[] array, float element) {
		float[] newArray = (float[]) copyArrayGrow1(array, Float.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static int[] add(int[] array, int element) {
		int[] newArray = (int[]) copyArrayGrow1(array, Integer.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static long[] add(long[] array, long element) {
		long[] newArray = (long[]) copyArrayGrow1(array, Long.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	public static short[] add(short[] array, short element) {
		short[] newArray = (short[]) copyArrayGrow1(array, Short.TYPE);
		newArray[(newArray.length - 1)] = element;
		return newArray;
	}

	private static Object copyArrayGrow1(Object array, Class<?> newArrayComponentType) {
		if (array != null) {
			int arrayLength = Array.getLength(array);
			Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}

	public static <T> T[] add(T[] array, int index, T element) {
		Class clss = null;
		if (array != null)
			clss = array.getClass().getComponentType();
		else if (element != null)
			clss = element.getClass();
		else {
			throw new IllegalArgumentException("Array and element cannot both be null");
		}

		Object[] newArray = (Object[]) add(array, index, element, clss);
		return (T[]) newArray;
	}

	public static boolean[] add(boolean[] array, int index, boolean element) {
		return (boolean[]) add(array, index, Boolean.valueOf(element), Boolean.TYPE);
	}

	public static char[] add(char[] array, int index, char element) {
		return (char[]) add(array, index, Character.valueOf(element), Character.TYPE);
	}

	public static byte[] add(byte[] array, int index, byte element) {
		return (byte[]) add(array, index, Byte.valueOf(element), Byte.TYPE);
	}

	public static short[] add(short[] array, int index, short element) {
		return (short[]) add(array, index, Short.valueOf(element), Short.TYPE);
	}

	public static int[] add(int[] array, int index, int element) {
		return (int[]) add(array, index, Integer.valueOf(element), Integer.TYPE);
	}

	public static long[] add(long[] array, int index, long element) {
		return (long[]) add(array, index, Long.valueOf(element), Long.TYPE);
	}

	public static float[] add(float[] array, int index, float element) {
		return (float[]) add(array, index, Float.valueOf(element), Float.TYPE);
	}

	public static double[] add(double[] array, int index, double element) {
		return (double[]) add(array, index, Double.valueOf(element), Double.TYPE);
	}

	private static Object add(Object array, int index, Object element, Class<?> clss) {
		if (array == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
			}
			Object joinedArray = Array.newInstance(clss, 1);
			Array.set(joinedArray, 0, element);
			return joinedArray;
		}
		int length = Array.getLength(array);
		if ((index > length) || (index < 0)) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		Object result = Array.newInstance(clss, length + 1);
		System.arraycopy(array, 0, result, 0, index);
		Array.set(result, index, element);
		if (index < length) {
			System.arraycopy(array, index, result, index + 1, length - index);
		}
		return result;
	}

	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return (T[]) array.clone();
	}

	public static long[] clone(long[] array) {
		if (array == null) {
			return null;
		}
		return (long[]) array.clone();
	}

	public static int[] clone(int[] array) {
		if (array == null) {
			return null;
		}
		return (int[]) array.clone();
	}

	public static short[] clone(short[] array) {
		if (array == null) {
			return null;
		}
		return (short[]) array.clone();
	}

	public static char[] clone(char[] array) {
		if (array == null) {
			return null;
		}
		return (char[]) array.clone();
	}

	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}

	public static double[] clone(double[] array) {
		if (array == null) {
			return null;
		}
		return (double[]) array.clone();
	}

	public static float[] clone(float[] array) {
		if (array == null) {
			return null;
		}
		return (float[]) array.clone();
	}

	public static boolean[] clone(boolean[] array) {
		if (array == null) {
			return null;
		}
		return (boolean[]) array.clone();
	}
}
