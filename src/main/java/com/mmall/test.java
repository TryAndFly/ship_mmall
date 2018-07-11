package com.mmall;

import com.google.common.collect.Maps;

import java.util.Map;

public class test {

    /**
     * search k in a short array
     * return index ,if can't find return -1
     *
     * @param arr
     * @param k
     */
    public static int binarySearch2(int[] arr, int k) {
        int a = 0;
        int b = arr.length - 1;

        while (a <= b) {

            int m = (a + b) / 2;


            //a==b ,m=a,m=b
            //b-a=1,m=a;
            //b=a+2,m=a+1;
            if (k < arr[m]) {
                b = m - 1;
            } else if (k > arr[m]) {
                a = m + 1;
            } else {
                return m;
            }

        }
        return -1;
    }

    public static int binarySearch(int[] arr, int a, int b, int k) {
        int m = a + (b - a) / 2;
        if (arr[m] == k) {
            return m;
        } else if (arr[m] > k) {
            //中间值大于k，m--b
            return binarySearch(arr, m + 1, b, k);
        } else {
            return binarySearch(arr, a, m, k);
        }

    }




    public static void main(String[] args) {

    }

}
