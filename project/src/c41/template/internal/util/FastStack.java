package c41.template.internal.util;

import java.util.Arrays;

public class FastStack {

	private int[] array;
	private int i = 0;
	
	public FastStack() {
		this.array = new int[8];
	}
	
	public void push(int val) {
		ensure(i + 1);
		array[i] = val;
		i++;
	}
	
	public int pop() {
		int val = array[i - 1];
		i--;
		ensure(i);
		return val;
	}
	
	public int size() {
		return i;
	}
	
	private void ensure(int size) {
		if(array.length < size) {
			array = Arrays.copyOf(array, array.length * 2);
			return;
		}
		if(array.length > 16 && array.length > size * 4) {
			array = Arrays.copyOf(array, array.length / 2);
		}
	}
	
}
