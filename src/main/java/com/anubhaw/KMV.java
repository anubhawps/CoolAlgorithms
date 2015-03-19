package com.anubhaw;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class KMV {

    public static void kmv1() {
        int iterations = 10;
        int sampleSize = 10000000;
        int K = 1000;

        for (int i=0; i<iterations; i++) {

            Set<Double> uniqueNumbers = new TreeSet<Double>();
            PriorityQueue<Double> kMin = new PriorityQueue<Double>(K, Collections.reverseOrder());

            for (int j=0; j<sampleSize; j++) {

                double temp = Math.random();
                uniqueNumbers.add(temp);

                if (kMin.size() < K) {
                    kMin.add(temp);
                } else if (kMin.peek() > temp) {
                    kMin.poll();
                    kMin.add(temp);
                }
            }
            System.out.println("Actual = [" + uniqueNumbers.size() + "] expected = [" + ((K-1)/kMin.peek()) + "] Error% = [" + (1 - (((K-1)/kMin.peek())/uniqueNumbers.size()))*100);
        }
    }

    public static void kmv2() {
        int iterations = 20;
        int sampleSize = 1000000;
        int K = 100;

        Random randomGenerator = new Random();

        for (int i=0; i<iterations; i++) {

            Set<Double> uniqueNumbers = new TreeSet<Double>();
            PriorityQueue<Double> kMin = new PriorityQueue<Double>(K, Collections.reverseOrder());

            for (int j=0; j<sampleSize; j++) {

                double temp = (randomGenerator.nextInt(Integer.MAX_VALUE) * 1.0)/Integer.MAX_VALUE;

                uniqueNumbers.add(temp);

                if (kMin.size() < K) {
                    kMin.add(temp);
                } else if (kMin.peek() > temp) {
                    kMin.poll();
                    kMin.add(temp);
                }
            }
            System.out.println("Actual = [" + uniqueNumbers.size() + "] expected = [" + ((K - 1) / kMin.peek()) + "] Error% = [" + (1 - (((K - 1) / kMin.peek()) / uniqueNumbers.size())) * 100);
        }
    }

    public static void kmv3() {
        int iterations = 3;
        int sampleSize = 1000000;
        int K = 100;

        for (int i=0; i<iterations; i++) {

            Set<String> allwords = new HashSet<String>();
            PriorityQueue<Double> kMInQueueMUR = new PriorityQueue<Double>(K, Collections.reverseOrder());
            PriorityQueue<Double> kMInQueueCRC = new PriorityQueue<Double>(K, Collections.reverseOrder());
            PriorityQueue<Double> kMInQueueADLR = new PriorityQueue<Double>(K, Collections.reverseOrder());

            for (int j=0; j<sampleSize; j++) {

                String randString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");

                allwords.add(randString);

                double hashVal = Math.abs(Hashing.crc32().hashBytes(randString.getBytes()).asInt());
                normalizeAndInsertInQueue(K, kMInQueueCRC, hashVal);

                hashVal = Math.abs(Hashing.murmur3_32().hashBytes(randString.getBytes()).asInt());
                normalizeAndInsertInQueue(K, kMInQueueMUR, hashVal);

                hashVal = Math.abs(Hashing.murmur3_128().hashBytes(randString.getBytes()).asInt());
                normalizeAndInsertInQueue(K, kMInQueueADLR, hashVal);

            }

            double hMean = (3.0/(Math.abs(1/kMInQueueADLR.peek())+ Math.abs(1 / kMInQueueCRC.peek())+ Math.abs(1/kMInQueueMUR.peek())));



            printResult(K, allwords, kMInQueueMUR.peek());
            printResult(K, allwords, kMInQueueCRC.peek());
            printResult(K, allwords, kMInQueueADLR.peek());
            printResult(K, allwords, hMean);
            System.out.println("==================================");

        }
    }

    private static void printResult(int k, Set<String> allwords, double queueMax) {
        System.out.println("Actual = [" + allwords.size() + "] expected = [" + (int)((k -1)/queueMax) + "] Error% = [" + (int)((1 - (((k -1)/queueMax)/allwords.size()))*100) + "]");
    }

    private static void normalizeAndInsertInQueue(int k, PriorityQueue<Double> kMInQueue, double hashVal) {
        double normalizedHashValue = hashVal/Integer.MAX_VALUE;

        if (kMInQueue.size() < k) {
            kMInQueue.add(normalizedHashValue);
        } else if (kMInQueue.peek() > normalizedHashValue) {
            kMInQueue.poll();
            kMInQueue.add(normalizedHashValue);
        }
    }

    public static void main(String[] args) {
        kmv3();
//        kmv2();
    }
}
