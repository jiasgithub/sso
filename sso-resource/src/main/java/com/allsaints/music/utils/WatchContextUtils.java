package com.allsaints.music.utils;

import org.springframework.util.StopWatch;

public class WatchContextUtils {
	
	private static ThreadLocal<StopWatch> traceWatchStop = new ThreadLocal<>();
	
	public static void start() {
		if(traceWatchStop.get() != null) {
			traceWatchStop.get().start();
		}
    }
	
	public static void start(String taskName) {
		if(traceWatchStop.get() != null) {
			traceWatchStop.get().start(taskName);
		}
    }
	
	public static void stop() {
		if(traceWatchStop.get() != null && traceWatchStop.get().isRunning()) {
			traceWatchStop.get().stop();
		}
    }
	
	public static String prettyPrint() {
		if(traceWatchStop.get() != null) {
			return traceWatchStop.get().prettyPrint();
		}
		return null;
    }
	
	public static String shortSummary() {
		if(traceWatchStop.get() != null) {
			return traceWatchStop.get().shortSummary();
		}
		return null;
    }
	
	public static StopWatch get() {
        return traceWatchStop.get();
    }

    public static void set(StopWatch value) {
    	traceWatchStop.set(value);
    }

}
