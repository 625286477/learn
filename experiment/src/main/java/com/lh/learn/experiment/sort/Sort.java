package com.lh.learn.experiment.sort;

/**
 * 描述类的作用
 *
 * @author liuhai
 *         Create at: 2018/3/26
 */
class Sort {
    void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] < a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }
    }

    void insetSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j >= 1; j--) {
                if (a[j] > a[j - 1]) {
                    swap(a, j, j - 1);
                }
            }
        }
    }

    void selectSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int minPos = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[minPos] > a[j]) {
                    minPos = j;
                }
            }
            swap(a, minPos, i);
        }
    }

    void quickSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = getQuickSortMid(a, left, right);
            quickSort(a, left, mid - 1);
            quickSort(a, mid + 1, right);
        }
    }

    private int getQuickSortMid(int[] a, int left, int right) {
        int key = a[left];
        while (left < right) {
            while (left < right && a[right] >= key) {
                right--;
            }
            a[left] = a[right];
            while (left < right && a[left] <= key) {
                left++;
            }
            a[right] = a[left];
        }
        a[left] = key;
        return left;
    }

    void mergeSort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, mid + 1, right);
    }

    private void merge(int[] a, int left, int lRight, int rLeft, int right) {
        int[] tempArr = new int[a.length];
        int index = left;
        int temp = left;
        while (left <= lRight && rLeft <= right) {
            if (a[left] < a[rLeft]) {
                tempArr[index++] = a[left++];
            } else {
                tempArr[index++] = a[rLeft++];
            }
        }

        while (left <= lRight) {
            tempArr[index++] = a[left++];
        }
        while (rLeft <= right) {
            tempArr[index++] = a[rLeft++];
        }
        while (temp <= right) {
            a[temp] = tempArr[temp++];
        }
    }

    void heapSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            createSmallHeap(a, a.length - 1 - i);
            swap(a, 0, a.length - 1 - i);
        }
    }

    @SuppressWarnings("all")
    public void createSmallHeap(int[] a, int last) {
        for (int i = (last - 1) / 2; i >= 0; i--) {
            int parent = i;
            // 左孩子
            while (2 * parent + 1 <= last) {
                int bigger = 2 * parent + 1;
                if (bigger + 1 <= last && a[bigger + 1] > a[bigger]) {
                    bigger++;
                }
                if (a[parent] < a[bigger]) {
                    swap(a, parent, bigger);
                    parent = bigger;
                } else {
                    break;
                }
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    void printArr(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(i == 0 ? a[i] : " " + a[i]);
        }
        System.out.println();
    }
}
