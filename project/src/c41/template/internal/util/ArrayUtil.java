package c41.template.internal.util;

public final class ArrayUtil {

	public static boolean exist(int[] array, int ch) {
		for (int value : array) {
			if(value == ch) {
				return true;
			}
		}
		return false;
	}
	
}
