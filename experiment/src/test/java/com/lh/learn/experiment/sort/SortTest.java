package com.lh.learn.experiment.sort;

import org.junit.Before;
import org.junit.Test;

/**
 * 描述类的作用
 *
 * @author liuhai
 *         Create at: 2018/3/26
 */
public class SortTest {
    private Sort sort = new Sort();
    private int[] a = {2, 1, 45, 2, 7, 34, 9, 0};

    @Before
    public void init() {
        sort.printArr(a);
    }

    @Test
    public void bubbleSort() throws Exception {
        sort.bubbleSort(a);
        sort.printArr(a);
    }

    @Test
    public void selectSort() throws Exception {
        sort.selectSort(a);
        sort.printArr(a);
    }

    @Test
    public void insetSort() throws Exception {
        sort.insetSort(a);
        sort.printArr(a);
    }

    @Test
    public void quickSort() throws Exception {
        sort.quickSort(a, 0, a.length - 1);
        sort.printArr(a);
    }

    @Test
    public void mergeSort() throws Exception {
        sort.mergeSort(a, 0, a.length - 1);
        sort.printArr(a);
    }

    @Test
    public void heapSort() throws Exception {
        sort.heapSort(a);
        sort.printArr(a);
    }

}