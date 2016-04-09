
package org.usfirst.frc.team6190.robot;

import edu.wpi.first.wpilibj.*;
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
    VictorSP intake = new VictorSP(5);
    Encoder rightEncoder = new Encoder(2,3, false, Encoder.EncodingType.k4X);
    Encoder leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    
    CameraServer server;
    
    boolean SingleAxisControl;
    
    double leftValuea;
    double rightValuea;
    
    
    public Robot() {
        myRobot = new RobotDrive(0,1,2,3);
        myRobot.setExpiration(0.1);
        
        server = CameraServer.getInstance();
        server.setQuality(10);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam0");
    }
    
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        

        leftEncoder.setMaxPeriod(.1);
    	leftEncoder.setMinRate(10);
    	leftEncoder.setDistancePerPulse(5);
    	leftEncoder.setReverseDirection(true);
    	leftEncoder.setSamplesToAverage(7);
    	
    	rightEncoder.setMaxPeriod(.1);
    	rightEncoder.setMinRate(10);
    	rightEncoder.setDistancePerPulse(5);
    	rightEncoder.setReverseDirection(true);
    	rightEncoder.setSamplesToAverage(7);
    	
    	
    	
    	leftEncoder.reset();
    	rightEncoder.reset();
    }
    
    public void rotate(){
    	rightEncoder.reset();
    	double currentPostition = -(rightEncoder.get());
    	    if (currentPostition <= 806){
    	    	currentPostition = -(rightEncoder.get());
    		    myRobot.tankDrive(-0.5, 0.5);
    	    }
    	    else {
    		     myRobot.tankDrive(0, 0);
    	    }
    	
    }
    
    
    public void driveStraight(double distance, double speed, double angle){
    	boolean continueLoop = true;
    	double rightCount = -(rightEncoder.get());
		double leftCount = (leftEncoder.get());
		
		if (continueLoop == true) {
		    if (rightCount <= distance){
			    myRobot.tankDrive(-0.8, -0.8);
		    }
		    else {
                continueLoop = false;
			    myRobot.tankDrive(0, 0);

		    }
		}
    }
    
    
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		
		leftEncoder.reset();
		rightEncoder.reset();
    }


    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here 

    	case defaultAuto:
    	default:
    	//Put default auto code here
    		driveStraight(8000 , 0.8, 0);
    	}
    }

 
    public void teleopPeriodic() {
    	
        myRobot.setSafetyEnabled(true);        
        while (isOperatorControl() && isEnabled()) {
        	
        	boolean Button = Xbox.getRawButton(1);
        	
        	//System.out.println(rightEncoder.get());
        	
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
        		double xLeftButton = Xbox.getRawAxis(2);
        		double xRightButton = Xbox.getRawAxis(3);
        		boolean intakeout = Xbox.getRawButton(6);
        		boolean intakein = Xbox.getRawButton(5);
        		boolean SLOWMOTION = Xbox.getRawButton(4);
        		boolean Turn90 = Xbox.getRawButton(3);
        		
        		if (Turn90 == true){
        			rotate();
        		}
        		
        		System.out.print("\n LEFT ENCODER: ");
        		System.out.print(leftEncoder.get());
        		System.out.print("\n RIGHT ENCODER: ");
        		System.out.print(rightEncoder.get());
        		
        		
        		if (intakeout == true){
        	        intake.set(-1);
        		}
        		
        		if (intakein == true){
        			intake.set(0.8);
        		}
        		
        		if (intakein != true && intakeout != true){
        			
        			intake.set(0);
        			
        		}
        		
        		
        		if (SLOWMOTION == true){

        			xLeft = ((xLeft/1.5));
        			xRight = ((xRight/1.5));
        		}
        		
        		if (xLeftButton > 0.1) {
        			myRobot.tankDrive(xLeft+xLeftButton, xRight+xLeftButton);
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
