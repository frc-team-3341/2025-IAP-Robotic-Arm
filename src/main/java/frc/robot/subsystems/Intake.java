// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLimitSwitch;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.LimitSwitchConfig.Type;
import com.revrobotics.spark.config.SoftLimitConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  public SparkLimitSwitch FWDLimit;
  public SparkLimitSwitch REVLimit;
  private final SparkMax intakeMotor1 = new SparkMax(25, MotorType.kBrushless);

  AbsoluteEncoder absEncoder;
  SparkClosedLoopController pidPivot;
  boolean enableTeleop = false;

  private final double forwardSoftLimit = 0.00;
  private final double revSoftLimit = 0.00;

  PIDController pidController = new PIDController(0, 0, 0);

  public boolean probablyHasintake = false;

  public Intake() {
   
    // Configuration for intake motors
    SparkMaxConfig intakeConfig1 = new SparkMaxConfig();
    intakeMotor1.configure(intakeConfig1, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

    // Limit switch configuration
    LimitSwitchConfig limitSwitchConfig = new LimitSwitchConfig();
    limitSwitchConfig.forwardLimitSwitchType(Type.kNormallyClosed);
    limitSwitchConfig.reverseLimitSwitchType(Type.kNormallyClosed);
    limitSwitchConfig.forwardLimitSwitchEnabled(true);
    limitSwitchConfig.reverseLimitSwitchEnabled(true);
    

    // Soft limit configuration
    SoftLimitConfig softLimitConfig = new SoftLimitConfig();
    softLimitConfig.forwardSoftLimitEnabled(true);
    softLimitConfig.reverseSoftLimitEnabled(true);

    // Updated Soft Limits
    //double forwardSoftLimit = zereodOffsetDegrees + (10.0 / 360.0);    // +10 degrees up
    //double reverseSoftLimit = zereodOffsetDegrees + (-44.0 / 360.0);   // -44 degrees down
    
    softLimitConfig.forwardSoftLimit(forwardSoftLimit);
    softLimitConfig.reverseSoftLimit(revSoftLimit);
  }
        public Command stopintake() {
          return this.runOnce(() -> {
              intakeMotor1.set(0.0);
          });
      }
  
      public Command intake() {
          return this.runOnce(() -> {
              intakeMotor1.set(0.3);
              probablyHasintake = true;
              probablyHasintake = false;
          });
      }
  
      public Command outake() {
        return this.runOnce(() -> {
            intakeMotor1.set(-0.3);
            probablyHasintake = false;
        });
    }
      

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
