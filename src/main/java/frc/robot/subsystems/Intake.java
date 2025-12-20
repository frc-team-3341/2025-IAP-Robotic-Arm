// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private final SparkMax intakeMotor1 = new SparkMax(25, MotorType.kBrushless);
  private boolean intakePurpleCube = false;
  ColorSensor colorSensor = new ColorSensor();

  public Intake() {
    // Configuration for intake motors
    SparkMaxConfig intakeConfig1 = new SparkMaxConfig();
    intakeMotor1.configure(intakeConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

  }
      public Command stopintake() {
          return this.runOnce(() -> {
              intakeMotor1.set(0.0);
          });
      }

      public Command intake() {
        return this.runOnce(() -> {
            intakeMotor1.set(0.1);
            Color color = colorSensor.getColor();
            intakePurpleCube = colorSensor.isPurple(color);
            
        });
      }

      public Command outake() {
        return this.runOnce(() -> {
            intakeMotor1.set(-0.1);
            intakePurpleCube = false;
        });
      }
      

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Intaken Purple Cube?", intakePurpleCube);
  }
}
