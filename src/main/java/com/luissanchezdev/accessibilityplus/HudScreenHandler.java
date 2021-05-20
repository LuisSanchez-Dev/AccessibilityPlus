package com.luissanchezdev.accessibilityplus;

import java.awt.AWTException;
import java.awt.Robot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;

public class HudScreenHandler {
	private int minColumn;
	private int maxColumn;
	private int currentColumn;
	private int differenceColumn;
	private int minRow;
	private int maxRow;
	private int currentRow;
	private int differenceRow;
	private int windowPosX;
	private int windowPosY;
	private int hudScreenOffsetX;
	private int hudScreenOffsetY;
    
	public HudScreenHandler() {
		minColumn = 0;
		maxColumn = 0;
		currentColumn = 0;
		differenceColumn = 0;
		minRow = 0;
		maxRow = 0;
		currentRow = 0;
		differenceRow = 0;
		windowPosX = 0;
		windowPosY = 0;
		hudScreenOffsetX = 0;
		hudScreenOffsetY = 0;
	}
	   
    public void screenHandler(Screen screen) {
	 	MutableText titleMutableText = new LiteralText("").append(screen.getTitle());
	 	String titleString = titleMutableText.getString().toLowerCase();
	 	{
	 		if(!AccessibilityPlus.mainThreadMap.containsKey("stonecutter_result_slot") && titleString.contains("stonecutter")) {
	 			try {
	 				stonecutterScreen(screen);
				} catch (Exception e) {
					e.printStackTrace();
				}
	 		} else if((titleString.contains("armorer")||titleString.contains("butcher")||titleString.contains("cartographer")||titleString.contains("cleric")||titleString.contains("farmer")||titleString.contains("fisherman")||titleString.contains("fletcher")||titleString.contains("leatherworker")||titleString.contains("librarian")||titleString.contains("mason")||titleString.contains("shepherd")||titleString.contains("toolsmith")||titleString.contains("weaponsmith")||titleString.contains("wandering trader"))){
				try {
					tradingScreen(screen);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
//	 		else if(titleString.contains("crafting")) {
//				try {
//					craftingScreen(screen);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//	 		else {
//				System.out.println(titleString);
//			}
	 	}
    }

	private void stonecutterScreen(Screen screen) {
    	MinecraftClient client = MinecraftClient.getInstance();
		try {
			minColumn = (int) (client.getWindow().getWidth() * 0.4409);
			differenceColumn = (int) (client.getWindow().getWidth() * 0.0347) + (int) ((double)(480 - screen.width) / 8);
			maxColumn = minColumn + ( 3 * differenceColumn );
			
			minRow = (int) (client.getWindow().getHeight() * 0.3);
			differenceRow = (int) (client.getWindow().getHeight() * 0.0666) + (int) ((double)(300 - screen.height) / 10) ;
			maxRow = minRow + ( 2 * differenceRow );
			
			windowPosX = client.getWindow().getX();
			windowPosY = client.getWindow().getY();
			
			hudScreenOffsetX = (int) ((double)(480 - screen.width) / 4) ;
			hudScreenOffsetY = (int) ((double)(300 - screen.height) / 3) ;
			System.out.println("XX:"+ client.getWindow().getX() +"\tYY:" + client.getWindow().getY());
			Robot robot;
			robot = new Robot();
			currentColumn = AccessibilityPlus.currentColumn;
			currentRow = AccessibilityPlus.currentRow;
			
			if(AccessibilityPlus.isDPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn >= maxColumn-5 && currentColumn <= maxColumn+5) {
					currentColumn += 0;
				} else {
					currentColumn += differenceColumn; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(AccessibilityPlus.isAPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn >= minColumn-5 && currentColumn <= minColumn+5) {
					currentColumn -= 0;
				} else {
					currentColumn -= differenceColumn; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(AccessibilityPlus.isSPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow >= maxRow-5 && currentRow <= maxRow+5) {
					currentRow += 0;
				} else {
					currentRow += differenceRow; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(AccessibilityPlus.isWPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow >= minRow-5 && currentRow <= minRow+5) {
					currentRow -= 0;
				} else {
					currentRow -= differenceRow; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("stonecutter_result_slot", 200);
			}
			
			AccessibilityPlus.currentColumn = currentColumn;
			AccessibilityPlus.currentRow = currentRow;
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void tradingScreen(Screen screen) {
    	MinecraftClient client = MinecraftClient.getInstance();
		try {
			minColumn = (int) (client.getWindow().getWidth() * 0.25347222);
			differenceColumn = (int) (client.getWindow().getWidth() * 0.0625) + (int) ((double)(480 - screen.width) / 8);
			maxColumn = minColumn + ( 2 * differenceColumn );
			
			minRow = (int) (client.getWindow().getHeight() * 0.32222);
			differenceRow = (int) (client.getWindow().getHeight() * 0.066667) + (int) ((double)(300 - screen.height) / 10);
			maxRow = minRow + ( 6 * differenceRow );
			
			windowPosX = client.getWindow().getX();
			windowPosY = client.getWindow().getY();
			
			hudScreenOffsetX = (int) ((double)(480 - screen.width) / 2) ;
			hudScreenOffsetY = (int) ((double)(300 - screen.height) / 3) ;
			
			Robot robot;
			robot = new Robot();
			currentColumn = AccessibilityPlus.currentColumn;
			currentRow = AccessibilityPlus.currentRow;
			
			
			if(!AccessibilityPlus.mainThreadMap.containsKey("trading_screen") && AccessibilityPlus.isDPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn >= maxColumn-5 && currentColumn <= maxColumn+5) {
					currentColumn += 0;
				} else {
					currentColumn += differenceColumn; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("trading_screen", 200);
			} else if(!AccessibilityPlus.mainThreadMap.containsKey("trading_screen") && AccessibilityPlus.isAPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn >= minColumn-5 && currentColumn <= minColumn+5) {
					currentColumn -= 0;
				} else {
					currentColumn -= differenceColumn; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("trading_screen", 200);
			} else if(!AccessibilityPlus.mainThreadMap.containsKey("trading_screen") && AccessibilityPlus.isSPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow >= maxRow-5 && currentRow <= maxRow+5) {
					currentRow += 0;
				} else {
					currentRow += differenceRow; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("trading_screen", 200);
			} else if(!AccessibilityPlus.mainThreadMap.containsKey("trading_screen") && AccessibilityPlus.isWPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow >= minRow-5 && currentRow <= minRow+5) {
					currentRow -= 0;
				} else {
					currentRow -= differenceRow; 
				}
				robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
				AccessibilityPlus.mainThreadMap.put("trading_screen", 200);
			} else if(!AccessibilityPlus.mainThreadMap.containsKey("trading_scrolled_screen") && AccessibilityPlus.isRPressed) {
				robot.mouseWheel(-1);
				AccessibilityPlus.mainThreadMap.put("trading_scrolled_screen", 200);
			} else if(!AccessibilityPlus.mainThreadMap.containsKey("trading_scrolled_screen") && AccessibilityPlus.isFPressed) {
				robot.mouseWheel(+1);
				AccessibilityPlus.mainThreadMap.put("trading_scrolled_screen", 200);
			}
			
			AccessibilityPlus.currentColumn = currentColumn;
			AccessibilityPlus.currentRow = currentRow;
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void craftingScreen(Screen screen) {
		if(AccessibilityPlus.isPointingAtCraftingBlock) {
			System.out.println("crafting block");
		} else {
			System.out.println("crafting");
		}
	}
}
