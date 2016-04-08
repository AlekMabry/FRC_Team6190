package org.usfirst.frc.team6190.robot;
import static java.lang.Math.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SensorBase;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import java.io.*;
import java.net.*;
import java.util.*;


public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    
    //int session;
    //Image frame;
    
    String autoSelected;
    SendableChooser chooser;
    RobotDrive myRobot;  // class that handles basic drive operations
    //Servo exampleServo = new Servo(9);
    //Servo exampleServo2 = new Servo(7);
    Joystick Xbox = new Joystick(0); // set to ID 1 in DriverStation
    Joystick Logitech = new Joystick(1);
    VictorSP intake = new VictorSP(5);
	Encoder LeftEncoder;
    Encoder RightEncoder;
	Ultrasonic ultra = new Ultrasonic(8,9);
	Encoder leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	Encoder rightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
	Accelerometer ADXL345_SPI = new ADXL345_SPI(SPI.Port.kMXP, Accelerometer.Range.k4G);
	String incomingData = "null";
	
	CameraServer server;

	//AnalogInput turnLeft = new AnalogInput(0);
	//AnalogInput Done = new AnalogInput(2);
	//AnalogInput turnRight = new AnalogInput(1);
	
	double xLeft;
	double xRight;
	double xLeftButton;
	double xRightButton;
	boolean intakeout;
	boolean intakein;
	boolean LIntakeOut;
	boolean LIntakeIn;
	boolean reverseButton;
	boolean downButton;
	boolean upButton;
	
	double lUp;
	double lRotate;
	double speed;
	
    double leftVolts;
	double doneVolts;
	double rightVolts;
	
	
	double xAccel;
	double yAccel;
	double zAccel;

	float leftRatio;
	float rightRatio;
	
	int rightCount;
	int leftCount;

    boolean SingleAxisControl;
    
    double leftValuea;
    double rightValuea;
    
    //BEAGLEBONE LOOP
//    public void HighGoalAlignGPIO(){
    	//Initialize Communications
    	
//    	leftVolts = turnLeft.getVoltage();
//    	doneVolts = Done.getVoltage();
//    	rightVolts = turnRight.getVoltage();
//    	int a = 1;
    	
 //   	if (a == 1){
//    		leftVolts = turnLeft.getVoltage();
//        	doneVolts = Done.getVoltage();
//        	rightVolts = turnRight.getVoltage();
//        	System.out.println("Left: "+leftVolts);
//        	System.out.println("Right: "+rightVolts);
//        	System.out.println("Done: "+Done);
    		
//    		if (leftVolts > 2.5){
//    		    myRobot.arcadeDrive(0, -0.2);
//    	    }
//    	    if (rightVolts > 2.5){
//    		    myRobot.arcadeDrive(0, -0.2);
//    	    }
//    	}

//    }
    
    //ULTRASOUND LOOP
    public void findWall(double distance, double speed, double angle){

    	double range = ultra.getRangeInches(); // reads the range on the ultrasonic sensor
    	boolean loop = true;
    	while (loop = true){
    		myRobot.arcadeDrive(speed, angle);
    		range = ultra.getRangeInches(); // reads the range on the ultrasonic sensor
    		if (range <= distance){
    			myRobot.tankDrive(0, 0);
    			loop = false;
    		}
    	}
    }
    
    //DRIVE STRAIGHT LOOP
    public void driveStraight(double distance, double speed, double angle){
    	boolean continueLoop = true;
    	rightCount = -(rightEncoder.get());
		leftCount = (leftEncoder.get());
		
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
    
    
    public Robot() {
        myRobot = new RobotDrive(0,1,2,3);
        myRobot.setExpiration(0.1);
    }
    
    
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        AnalogInput.setGlobalSampleRate(62500);
        ultra.setAutomaticMode(true);
        
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
    	
    	server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture("cam0");
    	
    	
    	
    	leftEncoder.reset();
    	rightEncoder.reset();
    	
    	//frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        //session = NIVision.IMAQdxOpenCamera("cam0",
        //        NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        //NIVision.IMAQdxConfigureGrab(session);
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

        case defaultAuto:
    	default:
    		driveStraight(8000, 0.8, 0);
    		//findWall(40, 0.8, 0);

    		
    	}

    }

 




	public void teleopPeriodic() {
        //NIVision.IMAQdxStartAcquisition(session);

        ///**
        // * grab an image, draw the circle, and provide it for the camera server
        //* which will in turn send it to the dashboard.
        // */
        //NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

        //while (isOperatorControl() && isEnabled()) {

        //    NIVision.IMAQdxGrab(session, frame, 1);
        //    NIVision.imaqDrawShapeOnImage(frame, frame, rect,
        //            DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
            
            //CameraServer.getInstance().setImage(frame);
    	
        myRobot.setSafetyEnabled(true);        
        while (isOperatorControl() && isEnabled()) {
        	
        	//boolean Button = Xbox.getRawButton(1);
        	boolean Button = false;
        	
        	if (Button == true) {
        		boolean reverseButton = Logitech.getRawButton(11);
        		boolean downButton = Logitech.getRawButton(2);
        		boolean upButton = Logitech.getRawButton(1);
        		
        		double lUp = Logitech.getRawAxis(1);
        		double lRotate = Logitech.getRawAxis(2);
        		double speed = Logitech.getRawAxis(3);
        		
        		if (upButton == true){
        			intake.set(speed);
        		}
        		
            	if (downButton == true){
        			intake.set(-speed);
        		}
        		
        		
        		if (upButton != true && downButton != true){
        			intake.set(0);
        		}
        	    if (reverseButton != true) {
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
        		//boolean shoot = Xbox.getRawButton(3);
        		//boolean shoot2 = Xbox.getRawButton(2);
        		boolean downButton = Logitech.getRawButton(2);
        		boolean upButton = Logitech.getRawButton(1);
        		
        		//if (shoot == true) {
        		//	//exampleServo.setAngle(90);
        		//}
        		//else {
        			//exampleServo.setAngle(0);
        		//}
        		
        		//if (shoot2 == true) {
        			//exampleServo2.setAngle(90);
        		//}
        		//else {
        			//exampleServo2.setAngle(0);
        		//}
        		
        		if (intakeout == true){
        	        intake.set(-1);
        		}
        		
        		if (intakein == true){
        			intake.set(1);
        		}
        		
        		if (upButton == true){
        			intake.set(-1);
        		}
        		
            	if (downButton == true){
        			intake.set(1);
        		}
        		
        		
        		if (intakein != true && intakeout != true){
        			if (downButton!= true && upButton!=true){
        			intake.set(0);
        			}
        		}
        		
        		if (xRightButton > 0.1) {
        			myRobot.tankDrive((xLeft)-xRightButton, (xRight)-xRightButton);
        		}
        		if (xLeftButton > 0.1) {
        			myRobot.tankDrive((xLeft)+xLeftButton, (xRight)+xLeftButton);
        		}
        	if (xLeftButton <= 0.1 && xRightButton <= 0.1) {
            		myRobot.tankDrive((xLeft), (xRight));
        		}
        		
        		//xAccel = ADXL345_SPI.getX();
        		//System.out.println(xAccel);
        		
        		
        		//rightCount = -(rightEncoder.get());
        		//leftCount = (leftEncoder.get());
        		
        		//System.out.println("Right: ");
        		//System.out.println(rightCount);
        		//System.out.println("Left: ");
        		//System.out.println(leftCount);

        		Timer.delay(0.005);
        	}
        }
        }
       
        	
        	
        	//------------------------OLD CODE------------------------------//
        	//boolean Button = Xbox.getRawButton(1);
        	//Button = false;
        	//
        	//if (Button == true) {
        	//	boolean reverseButton = Logitech.getRawButton(11);
        	//	boolean downButton = Logitech.getRawButton(2);
        	//	boolean upButton = Logitech.getRawButton(1);
        	//	
        	//	double lUp = Logitech.getRawAxis(1);
        	//	double lRotate = Logitech.getRawAxis(2);
        	//	double speed = Logitech.getRawAxis(3);
        	//	
        	//	if (upButton == true){
        	//		intake.set(speed);
        	//	}
        	//	
        	//	if (downButton == true){
        	//		intake.set(-speed);
        	//	}
        	//	
        	//	
        	//	if (upButton != true && downButton != true){
        	//		intake.set(0);
        	//	}
        	//    if (reverseButton != true) {
        	//    	lUp = -lUp;
        	//    }
        	//	
            //	lRotate = -lRotate;
            //
        	//
        	//	myRobot.arcadeDrive(lUp, lRotate);
            //
            //
        	//} else {
        	//	double xLeft = Xbox.getRawAxis(5);
        	//	double xRight = Xbox.getRawAxis(1);
        	//	double xLeftButton = Xbox.getRawAxis(2);
        	//	double xRightButton = Xbox.getRawAxis(3);
        	//	boolean intakeout = Xbox.getRawButton(6);
        	//	boolean intakein = Xbox.getRawButton(5);
        	//	boolean shoot = Xbox.getRawButton(3);
        	//	boolean shoot2 = Xbox.getRawButton(2);
        	//	
        	//	if (shoot == true) {
        	//		//exampleServo.setAngle(90);
        	//	}
        	//	else {
        	//		//exampleServo.setAngle(0);
        	//	}
        	//	
        	//	if (shoot2 == true) {
        	//		//exampleServo2.setAngle(90);
        	//	}
        	//	else {
        	//		//exampleServo2.setAngle(0);
        	//	}
        	//	
        	//	if (intakeout == true){
        	//		intake.set(0.8);
        	//	}
        	//	
        	//	if (intakein == true){
        	//		intake.set(-1);
        	//	}
        	//	
        	//	
        	//	if (intakein != true && intakeout != true){
        	//		intake.set(0);
        	//	}
        		
        	//	if (xRightButton > 0.1) {
        	//		myRobot.tankDrive((xLeft)+xRightButton, (xRight)+xRightButton);
        	//	}
        	//	if (xLeftButton > 0.1) {
        	//		myRobot.tankDrive((xLeft)-xLeftButton, (xRight)-xLeftButton);
        	//	}
        	//	if (xLeftButton <= 0.1 && xRightButton <= 0.1) {
            //		myRobot.tankDrive((xLeft), (xRight));
        	//	}
        		
        		//xAccel = ADXL345_SPI.getX();
        		//System.out.println(xAccel);
        		
        		
        		//rightCount = -(rightEncoder.get());
        		//leftCount = (leftEncoder.get());
        		
        		//System.out.println("Right: ");
        		//System.out.println(rightCount);
        		//System.out.println("Left: ");
        		//System.out.println(leftCount);

        	//	Timer.delay(0.005);
        	//}
       
    
        
    
    public void testPeriodic() {
    	
    }

}

