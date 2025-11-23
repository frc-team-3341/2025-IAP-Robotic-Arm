// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.List;

import org.ejml.equation.Variable;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.PIDTurn;

public class Vision extends SubsystemBase {
    private final PhotonCamera bottomCamera = new PhotonCamera(Constants.VisionConstants.CameraName);
    /** Creates a new Vision. */
  private double targetYaw = 0.0;
  private Boolean targetVisible = false;

  public Vision() {
    bottomCamera.setPipelineIndex(0); //AprilTag
    //bottomCamera.setPipelineIndex(1); //Cube Pipeline
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var result = bottomCamera.getLatestResult();
    try{
      if(result.hasTargets()){
        List<PhotonTrackedTarget> targets = result.getTargets();
        PhotonTrackedTarget target = targets.get(0);
        SmartDashboard.putNumber("Target ID", target.getFiducialId());
        SmartDashboard.putNumber("Target Yaw", target.getYaw());
        SmartDashboard.putBoolean("Target Found", true);
        targetVisible = true;
      } else{
        SmartDashboard.putBoolean("Target Found", false);
      }
    } catch (Exception e){
      System.out.println("No Target Found: " + e);
    }
    
  }

  public double getYaw(){
    var results = bottomCamera.getAllUnreadResults();
    if (!results.isEmpty()) {
        // Camera processed a new frame since last
        // Get the last one in the list.
        var result = results.get(results.size() - 1);
        if (result.hasTargets()) {
            // At least one AprilTag was seen by the camera
            for (var target : result.getTargets()) {
                if (target.getFiducialId() == 18) {
                    // Found Tag 7, record its information
                    targetYaw = target.getYaw();
                    targetVisible = true;
                    return targetYaw;
                }
            }
        }
    }
    return 0.0;
  }

  public Boolean hasTarget(){
    return targetVisible;
  }
}
