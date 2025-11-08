// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.*;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final Joystick joy1 = new Joystick(Constants.USBOrder.Zero);

  private final DriveTrain dt = new DriveTrain();

  private final TankDrive tankDrive = new TankDrive(dt, joy1);

  double setpoint = 5.0;

  private final Autodrive autodrive = new Autodrive(dt, setpoint);

  private final PIDTurn pidTurn = new PIDTurn(dt, 90.0);
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    dt.setDefaultCommand(tankDrive);
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
 
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autodrive;
    /*return new SequentialCommandGroup( 
      new Autodrive(dt, 1.0), 
      new PIDTurn(dt, 90), 
      new Autodrive(dt, 1.0),
      new PIDTurn(dt, 90),
      new Autodrive(dt, 1.0),
      new PIDTurn(dt, 90),
      new Autodrive(dt, 1.0)
      );*/
  }
}
