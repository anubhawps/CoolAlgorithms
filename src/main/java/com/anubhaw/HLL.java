package com.anubhaw;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class HLL {


    public static void hll() {
        int iterations = 3;
        int sampleSize = 10000000;
        int bitsReservedForBuckets = 8;
        int bucketCount = (int)Math.pow(2, bitsReservedForBuckets);
        int []zeroRunLengths = new int[bucketCount];

        for (int i=0; i<iterations; i++) {

            for (int ii=0; ii<bucketCount; ii++) {
                zeroRunLengths[ii] = 0;
            }
//            Set<String> allwords = new HashSet<String>();
            Set<Integer> integerSet = new HashSet<Integer>();

            for (int j=0; j<sampleSize; j++) {

                String randString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
//                allwords.add(randString);

                byte[] hashVal = Hashing.sha1().hashBytes(randString.getBytes()).asBytes();

                int bucketIndex = getBucketIndex(hashVal, bitsReservedForBuckets);
                int zrl =  getZerosRunLength(hashVal, bitsReservedForBuckets);
                if (zrl > zeroRunLengths[bucketIndex]) {
                    zeroRunLengths[bucketIndex] = zrl;
                }
            }

            int APMean;
            int runCount = 0;

            for (int ii = 0; ii<bucketCount; ii++) {
                runCount += zeroRunLengths[ii];
            }

            APMean = runCount/bucketCount;

            double HMean;
            double tempSum=0;
            for (int ii = 0; ii<bucketCount; ii++) {
                tempSum += (1.0/zeroRunLengths[ii]);
            }
            HMean = bucketCount/tempSum;


            int totalStringsCount = sampleSize;

            double expectedCount = bucketCount * Math.ceil(Math.pow(2, APMean));
            System.out.println("Actual = [" + totalStringsCount + "] expected = [" + (int)expectedCount + "] Error% = [" + (int)((1 - (expectedCount/totalStringsCount))*100) + "]");

            expectedCount = bucketCount * Math.ceil(Math.pow(2, HMean));
            System.out.println("Actual = [" + totalStringsCount + "] expected = [" + (int)expectedCount + "] Error% = [" + (int)((1 - (expectedCount/totalStringsCount))*100) + "]");

            System.out.println("==============");
        }
    }

    private static int getBucketIndex(byte[] hashVal, int bitsReservedForBuckets) {
        int index = 0;
        int pow = 1;
        for (int i=hashVal.length-1; i>=0; i--) {

            byte b = hashVal[i];
            for ( int mask = 0x01; mask != 0x100; mask <<= 1 ) {
                boolean value = ( b & mask ) != 0;
                if (value) {
                    index += pow;
                }
                pow *= 2;
                bitsReservedForBuckets--;
                if (bitsReservedForBuckets == 0) {
                    return index;
                }
            }
        }
        return index;
    }

    private static int getZerosRunLength(byte[] hashVal, int bitsReservedForBuckets) {
        int zeroRunCount = 0;

        for (int i=hashVal.length-1; i>=0; i--) {
            byte b = hashVal[i];
            for ( int mask = 0x01; mask != 0x100; mask <<= 1 ) {
                if (bitsReservedForBuckets != 0) {
                    bitsReservedForBuckets--;
                    continue;
                }
                boolean value = ( b & mask ) != 0;
                if (value) {
                    return zeroRunCount;
                }
                zeroRunCount++;
            }
        }
        return zeroRunCount;
    }

    private static void printResult(int k, Set<String> allwords, double queueMax) {
        System.out.println("Actual = [" + allwords.size() + "] expected = [" + (int)((k -1)/queueMax) + "] Error% = [" + (int)((1 - (((k -1)/queueMax)/allwords.size()))*100) + "]");
    }

    private static void normalizeAndInsertInQueue(int k, PriorityQueue<Double> kMInQueue, double hashVal) {
        double normalizedHashValue = hashVal/ MAX_VALUE;

        if (kMInQueue.size() < k) {
            kMInQueue.add(normalizedHashValue);
        } else if (kMInQueue.peek() > normalizedHashValue) {
            kMInQueue.poll();
            kMInQueue.add(normalizedHashValue);
        }
    }

    public static void main(String[] args) {
        System.out.println("HLL");
        hll();
    }
}
