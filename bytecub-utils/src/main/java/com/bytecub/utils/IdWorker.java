package com.bytecub.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class IdWorker {
    private static final Logger logger  = LoggerFactory.getLogger(IdWorker.class);
    private static final long   twepoch = 1288834974657L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = 31L;
    private static final long maxDatacenterId = 31L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long datacenterIdShift = 17L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private static long lastTimestamp = -1L;
    private long sequence = 0L;
    private final long workerId;
    private final long datacenterId;
    private static Integer startIndex=0;
    private static Integer endIndex=6;
    public IdWorker(long workerId, long datacenterId) {
        if(workerId <= 31L && workerId >= 0L) {
            this.workerId = workerId;
        } else {
            if(workerId != -1L) {
                throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
            }

            this.workerId = (long)(new Random()).nextInt(31);
        }

        if(datacenterId <= 31L && datacenterId >= 0L) {
            this.datacenterId = datacenterId;
        } else {
            if(datacenterId != -1L) {
                throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
            }

            this.datacenterId = (long)(new Random()).nextInt(31);
        }

    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if(timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
            } catch (Exception e) {
                logger.warn("生成ID异常", e);
            }
        }

        if(lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1L & 4095L;
            if(this.sequence == 0L) {
                timestamp = this.tilNextMillis(lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        lastTimestamp = timestamp;
        long nextId = timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        return nextId;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
            ;
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getNextCode() {
        return Md5Utils.md5(IdWorker.uuid() + System.currentTimeMillis()).substring(startIndex,endIndex);
    }

}
