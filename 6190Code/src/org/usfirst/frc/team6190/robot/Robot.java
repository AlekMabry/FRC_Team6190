
package org.usfirst.frc.team6190.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.*;
import java.net.*;



public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    
    String autoSelected;
    SendableChooser chooser;
    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick Xbox = new Joystick(0); // set to ID 1 in DriverStation
    Joystick Logitech = new Joystick(1);
    
    boolean SingleAxisControl;
    
    double leftValuea;
    double rightValuea;
    
    
    public Robot() {
        myRobot = new RobotDrive(0,3,1,2);
        myRobot.setExpiration(0.1);
    }
    
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
    }
    
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }


    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here 
    		myRobot.tankDrive(0.7, -0.7);
    	case defaultAuto:
    	default:
    	//Put default auto code here
    		myRobot.tankDrive(-0.7, 0.7);
    	}
    }

 
    public void teleopPeriodic() {
    	
        myRobot.setSafetyEnabled(true);        
        while (isOperatorControl() && isEnabled()) {
        	
        	boolean Button = Xbox.getRawButton(1);
        	
        	if (Button == true) {
        		boolean reverseButton = Logitech.getRawButton(2);
        		double lUp = Logitech.getRawAxis(1);
        		double lRotate = Logitech.getRawAxis(2);
        		
        		
        	    if (reverseButton == true) {
        	    	lUp = -lUp;
        	    }
        		
            	lRotate = -lRotate;

        	
        		myRobot.arcadeDrive(lUp, lRotate);


        	} else {
        		double xLeft = Xbox.getRawAxis(5);
        		double xRight = Xbox.getRawAxis(1);
        		double xLeftButton = Xbox.getRawAxis(3);
        		double xRightButton = Xbox.getRawAxis(4);
        		
        		if (xLeftButton > 0.1) {
        			myRobot.tankDrive(xLeft+xLeftButton, xRight+xRightButton);
        		}
        		if (xRightButton > 0.1) {
        			myRobot.tankDrive(xLeft-xRightButton, xRight-xRightButton);
        		}
        		if (xLeftButton <= 0.1 && xRightButton <= 0.1) {
            		myRobot.tankDrive(xLeft, xRight);
        		}
        		
        		Timer.delay(0.005);
        	}
       
        }
        
    }
    
    public void testPeriodic() {
    
    }
    
}
