package com.anubhaw;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class BF {


    public static void bloomeFilter() {

        int M = 100000007;  int N = 10000000;
//        int M = 1000000007; int N = 100000000;

        BitSet bitSet = new BitSet(M);

        int ind1, ind2, ind3, ind4, ind5;
        int countExists = 0;

        for (int i=0; i<N; i++) {

            String randString = RandomStringUtils.random(8, "abcdefghijklmnopqrstuvwxyz");
            ind1 = Math.abs(Hashing.crc32().hashBytes(randString.getBytes()).asInt())%M;
            ind2 = Math.abs(Hashing.murmur3_32().hashBytes(randString.getBytes()).asInt())%M;
            ind3 = Math.abs(Hashing.adler32().hashBytes(randString.getBytes()).asInt())%M;
            ind4 = Math.abs(Hashing.crc32c().hashBytes(randString.getBytes()).asInt())%M;
            ind5 = Math.abs(randString.hashCode())%M;

            boolean exists = bitSet.get(ind1) &&
                    bitSet.get(ind1) &&bitSet.get(ind2) &&
                    bitSet.get(ind1) &&bitSet.get(ind3) &&
                    bitSet.get(ind1) &&bitSet.get(ind4) &&
                    bitSet.get(ind1) &&bitSet.get(ind5);
            if (exists) {
                countExists++;
            }
            bitSet.set(ind1);
            bitSet.set(ind2);
            bitSet.set(ind3);
            bitSet.set(ind4);
            bitSet.set(ind5);
        }
        System.out.println("Number of collisions = " + countExists);
        System.out.println("Error % = " + (countExists * 100.0)/N);
    }

    public static void main(String[] args) {
        bloomeFilter();
    }
}
