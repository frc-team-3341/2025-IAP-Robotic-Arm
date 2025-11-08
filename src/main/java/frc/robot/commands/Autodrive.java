// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveTrain;

public class Autodrive extends Command {
  DriveTrain dt;
  double setpoint;
  /** Creates a new Autodrive. */
  public Autodrive(DriveTrain dt, double setpoint) {
    this.dt = dt;
    this.setpoint = setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(dt);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    dt.resetEncoders();
    dt.tankDrive(0.0,0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    dt.tankDrive(0.3, 0.3);
    SmartDashboard.putNumber("Right Talon Ticks", dt.ticksToMeters());

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    dt.resetEncoders();
    dt.tankDrive(0.0, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (dt.ticksToMeters() >= setpoint) {
      return true;
    }
  else {
    return false;
  }
  }
}
