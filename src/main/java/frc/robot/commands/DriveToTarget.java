// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
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
  boolean isAligned = false;
  private double yawSetPoint = 0.0;

  PIDController pid = new PIDController(0.02, 0.005, 0);//This is the constructor. Kp, ki, and kd are constants

  public DriveToTarget(DriveTrain dt, Vision vision, Joystick joy1) {
    // Use addRequirements() here to declare subsystem dependencies.
    
    // We are passing in the subsystems, commands and joystick needed for this command in the constructor
    this.dt = dt;
    this.vision = vision;
    this.joy1 = joy1;
    pid.setTolerance(5.0); //use with atSetpoint() method to determine if on target
    addRequirements(dt, vision); //only for subsystems, not commands
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isAligned = false;
    dt.resetNavx();
    dt.tankDrive(0,0); //starts at 0 on both motors so the robot does not power on and move away
    yawSetPoint = vision.getYaw(); //get initial yaw value from vision (0.0 deg if no target found)
    SmartDashboard.putNumber("Initial Yaw Setpoint:", yawSetPoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  //  while(!vision.hasTarget()){
  //     double speed = 0.3;
  //     dt.tankDrive(-speed, speed);
  //     SmartDashboard.putNumber("Turning to find Target Speed:", speed);
  //   }

      double output = pid.calculate(dt.getAngle(), yawSetPoint);

      SmartDashboard.putNumber("Command: Navx Angle", dt.getAngle());
      SmartDashboard.putNumber("Output from PID Controller: ", output);

      if(Math.abs(output) > 0.5) { //If PID output is too high, cap it to 0.4
        output = Math.copySign(0.4, output); // Preserves sign, caps magnitude
    }
      dt.tankDrive(-output, output);
      
      if(pid.atSetpoint()) {
        isAligned = true;
      }
        // dt.resetEncoders();
        // double distanceToTarget = vision.getDistanceToTarget();
        // SmartDashboard.putNumber("Distance to Target (m): ", distanceToTarget);
        // double ticksToMeters = dt.metersToTicks(distanceToTarget);
        // SmartDashboard.putNumber("Current Distance (m): ", ticksToMeters);

        // double output = vision.getPowerOutputToDistance(distanceToTarget, ticksToMeters);
        // SmartDashboard.putNumber("Drive to Target Output: ", output);
        // if(Math.abs(output) > 0.4){ //If PID output is too high, cap it to 0.4
        //   output = 0.3;
        // }
        // dt.tankDrive(output, output);
        
    
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
