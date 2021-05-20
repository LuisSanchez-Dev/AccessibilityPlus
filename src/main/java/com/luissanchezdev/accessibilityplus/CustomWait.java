package com.luissanchezdev.accessibilityplus;

import java.util.Map;

public class CustomWait extends Thread {
    private boolean running=false;

    public void run() {
    	while(running) {
    		try {
				if(!AccessibilityPlus.mainThreadMap.isEmpty()) {
					for (Map.Entry<String, Integer> entry : AccessibilityPlus.mainThreadMap.entrySet()) {
						entry.setValue(entry.getValue()-1);
						if(entry.getValue()<=10) {
							System.out.println("removed "+entry.getKey());
							AccessibilityPlus.mainThreadMap.remove(entry.getKey());
						}
					}
				}
				Thread.sleep(1);
			} catch (Exception e) {
			}
    	}
    }

    public void stopThread() {
        running = false;
        interrupt();
    }

    public void startThread(){
        running = true;
        this.start();
    }
}
