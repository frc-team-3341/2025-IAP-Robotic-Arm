package frc.robot.subsystems;

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

public class Pivot extends SubsystemBase {
    double setpoint;

    private final SparkMax pivotMotor = new SparkMax(0, MotorType.kBrushless); //insert value
    
    //limit switch FWD and REV soft
    private final double forwardSoftLimit = 0.00;
    private final double reverseSoftLimit = 0.00;
    //absolute encoder
    AbsoluteEncoder absEncoder;
    
    //pid 
    SparkClosedLoopController pidPivot;
    //enable teleop
    //boolean enableTeleop = false; 

    PIDController pidController = new PIDController(0,0,0); //insert values

    public Pivot() {
        //inset closed loop for pivot motor
        this.pidPivot = pivotMotor.getClosedLoopController();
        this.absEncoder = pivotMotor.getAbsoluteEncoder();
        //inset open loop for absolute encoder
       
        SparkMaxConfig pivotConfig = new SparkMaxConfig();
        pivotMotor.configure(pivotConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //set up PID constants
        pivotConfig.closedLoop.pid(
            0,
            0,
            0
        );
        pivotConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);

        pivotConfig.smartCurrentLimit(0); //inset values
        

        //absolute encoder zeroOffSet
        //absolute encoder zeroCentered

        //set up limit switch (soft and hard) configs
        //soft
        SoftLimitConfig softLimitConfig = new SoftLimitConfig();
        softLimitConfig.forwardSoftLimitEnabled(true);
        softLimitConfig.reverseSoftLimitEnabled(true);

        softLimitConfig.forwardSoftLimit(forwardSoftLimit);
        softLimitConfig.reverseSoftLimit(reverseSoftLimit);
        
        pivotConfig.apply(softLimitConfig);
        
    }
    
    /*public Command toggleTeleop() {
        return this.runOnce(() -> {
            enableTeleop = !enableTeleop;
        });
    }*/
    public Command stopPivot(){
        return this.runOnce(() -> {
            pivotMotor.set(0.0);
        });
    }
    public Command movePivotDown() {
        return this.runOnce(() -> {
            setpoint += 0; //set setpoint
            setpoint = Math.max(setpoint, forwardSoftLimit); //check absolute encoder for min or max 
            this.pidPivot.setReference(setpoint, SparkMax.ControlType.kPosition);
            });
        }

    public Command movePivotUp(){
        return this.runOnce(() -> {
                setpoint += 0; // set setpoint
                setpoint = Math.min(setpoint, reverseSoftLimit); //check absolute encoder for min or max 
                this.pidPivot.setReference(setpoint, SparkMax.ControlType.kPosition);
        });
    }

    public void periodic() {
        //set up smart dashboard values
        
        /*SmartDashboard.putNumber("Angle from 0", (153.2 - (absEncoder.getPosition() * 360.0)));
        SmartDashboard.putNumber("Angle of Pivot", (absEncoder.getPosition() * 360.0));
        SmartDashboard.putNumber("pivot pos", this.absEncoder.getPosition());*/
    }

}