package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
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

public class Pivot extends SubsystemBase {
    double positionIncrement = 0.01; //amount to increment setpoint by
    double setpoint = 0.60; //initial setpoint
    public SparkLimitSwitch FWDLimit;
    public SparkLimitSwitch REVLimit;
    private final SparkMax pivotMotor = new SparkMax(6, MotorType.kBrushless); 
    
    //limit switch FWD and REV soft
    private final double forwardSoftLimit = 0.68; //as the arm goes down
    private final double reverseSoftLimit = 0.52; //as the arm goes up
    
    AbsoluteEncoder absEncoder;
    SparkClosedLoopController pidPivot;

    public Pivot() {
        this.pidPivot = pivotMotor.getClosedLoopController();
        this.absEncoder = pivotMotor.getAbsoluteEncoder();
       
        SparkMaxConfig pivotConfig = new SparkMaxConfig();

        //set up PID constants
        pivotConfig.closedLoop.pid(  
            3,
            0,
            0
        );
        pivotConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
        pivotConfig.smartCurrentLimit(20); //not sure if this is the correct number

        //set up limit switch (soft and hard) configs
        // soft
        SoftLimitConfig softLimitConfig = new SoftLimitConfig();
        softLimitConfig
            .forwardSoftLimitEnabled(true)
            .reverseSoftLimitEnabled(true)
            .forwardSoftLimit(forwardSoftLimit)
            .reverseSoftLimit(reverseSoftLimit);
        
        // hard
        LimitSwitchConfig limitSwitchConfig = new LimitSwitchConfig();
        limitSwitchConfig
        .forwardLimitSwitchType(Type.kNormallyClosed)
        .reverseLimitSwitchType(Type.kNormallyClosed)
        .forwardLimitSwitchEnabled(true)
        .reverseLimitSwitchEnabled(true);

        //apply configs for both types of limit switches
        pivotConfig.apply(softLimitConfig);
        pivotConfig.apply(limitSwitchConfig);
        pivotMotor.configure(pivotConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        FWDLimit = pivotMotor.getForwardLimitSwitch();
        REVLimit = pivotMotor.getReverseLimitSwitch();
    }
    
    
    public Command holdPivot(){
        return this.runOnce(() -> {
            setpoint = absEncoder.getPosition(); // Hold current position
            pidPivot.setReference(setpoint, SparkMax.ControlType.kPosition);
            SmartDashboard.putNumber("Hold Pivot Applied Output", pivotMotor.getAppliedOutput());

        });
    }
    public Command movePivotDown() {
        return this.runOnce(() -> {
            setpoint = Math.max(setpoint + positionIncrement, forwardSoftLimit); //gping down is an increase in the abs encoder value
            pidPivot.setReference(setpoint, SparkMax.ControlType.kPosition);
            SmartDashboard.putNumber("Pivot Down Applied Output", pivotMotor.getAppliedOutput());
            });
        }

    public Command movePivotUp(){
        return this.runOnce(() -> {
            setpoint = Math.min(setpoint - positionIncrement, reverseSoftLimit); //going up is a decrease in the abs encoder value
            pidPivot.setReference(setpoint, SparkMax.ControlType.kPosition);
            SmartDashboard.putNumber("Pivot Up Applied Output", pivotMotor.getAppliedOutput());
        });
    }

    public void periodic() {
        //set up smart dashboard values
        SmartDashboard.putNumber("Pivot Position", absEncoder.getPosition());
        SmartDashboard.putNumber("Pivot Setpoint", setpoint);
        SmartDashboard.putNumber("Pivot Angle (deg)", absEncoder.getPosition() * 360.0);
    }

}