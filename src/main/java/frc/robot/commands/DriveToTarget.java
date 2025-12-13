// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Vision;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DriveToTarget extends Command {
  /** Creates a new DriveToTarget. */
  //These are the subsystems, commands and joystick needed for this DriveToTarget command
  DriveTrain dt;
  Vision vision;
  Joystick joy1;
  double padding = 5.0; //if within 5 degrees of the setpoint angle
  Boolean isAligned = false;
  private double yawSetPoint = 0.0;

  public DriveToTarget(DriveTrain dt, Vision vision, Joystick joy1) {
    // Use addRequirements() here to declare subsystem dependencies.
    
    // We are passing in the subsystems, commands and joystick needed for this command in the constructor
    this.dt = dt;
    this.vision = vision;
    this.joy1 = joy1;

    addRequirements(dt, vision); //only for subsystems, not commands
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isAligned = false;
    dt.resetNavx();
    // while(dt.getNavxCalibrating()){
    //   SmartDashboard.putBoolean("Navx Calibrating", dt.getNavxCalibrating());
    // }
    // SmartDashboard.putBoolean("Navx Calibrating", dt.getNavxCalibrating());
    // if(!dt.getNavxCalibrating()){
    //   dt.resetNavx(); //reset navx to 0 if not calibrating
    // }
    dt.tankDrive(0,0); //starts at 0 on both motors so the robot does not power on and move away
    yawSetPoint = vision.getYaw(); //get initial yaw value from vision (0.0 deg if no target found)
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   while(!vision.hasTarget()){
      double speed = 0.3;
      dt.tankDrive(-speed, speed);
      SmartDashboard.putNumber("Turning to find Target Speed:", speed);
    }
    
    dt.PIDTurn2(yawSetPoint); //use PID turn command to move motors
    SmartDashboard.putBoolean("isAligned", isAligned); 

    if(vision.hasTarget()){
        if(Math.abs(vision.getYaw()) - Math.abs(dt.getAngle()) < padding){
          SmartDashboard.putBoolean("isAligned", isAligned); 
          isAligned = true; 
        }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dt.tankDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isAligned;
  }
}
