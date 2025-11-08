// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveTrain;

public class PIDTurn extends Command {
  DriveTrain dt;
  double setpointAngle;
  PIDController pid = new PIDController(0.00333333333, 0.005, 0);//This is the constructor. Kp, ki, and kd are constants
  int motorSign;

  /** Creates a new PIDTurn. */
  public PIDTurn(DriveTrain dt, double setpointAngle) {
    this.dt = dt;
    this.setpointAngle = setpointAngle;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt);
    pid.setTolerance(5.0);

    if (setpointAngle >= 0){ //If the motor is one, it is a counterclockwise turn
      motorSign = 1;
    } else{
    motorSign = -1;
  }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    dt.resetNavx();
    dt.tankDrive(0,0); //stars at 0 on both motors so the robot does not power on and move away

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double output = pid.calculate(dt.getAngle(), setpointAngle);
    dt.tankDrive(-motorSign*output, motorSign*output); //one of the motors is negative so that the robot turns
  }
  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dt.tankDrive(0,0);
  }

  //Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pid.atSetpoint();
  }
}
