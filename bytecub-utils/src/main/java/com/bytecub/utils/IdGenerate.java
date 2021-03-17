package com.bytecub.utils;

import java.security.SecureRandom;

public class IdGenerate {
    private static Integer startIndex=0;
    private static Integer endIndex=6;
    private static SecureRandom random = new SecureRandom();
    private static IdWorker idWorker = new IdWorker(-1L, -1L);

    public IdGenerate() {
    }

    public static String genId() {
        return String.valueOf(Math.abs(random.nextLong()));
    }

    public static String nextId() {
        return String.valueOf(idWorker.nextId());
    }

    public static void main(String[] args) {
        for(int i = 0;i<10;i++){
            System.out.println(nextId());
        }
    }
}
