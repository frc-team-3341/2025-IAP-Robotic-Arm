// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Vision extends SubsystemBase {
    private final PhotonCamera bottomCamera = new PhotonCamera(Constants.VisionConstants.CameraName);
    /** Creates a new Vision. */
  private double targetYaw = 0.0;
  Transform3d camToTarget;
  double distToTarget = 0.0;
  private Boolean targetVisible = false;
  private Transform3d bestCameraToTarget;

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
        PhotonTrackedTarget target = targets.get(0); //gets the first target (whatever it considers the first target is)
        
        //Test this: 
        bestCameraToTarget = target.getBestCameraToTarget(); //Transform3d object (Get the transform that maps camera space (X = forward, Y = left, Z = up) to object/fiducial tag space (X forward, Y left, Z up) with the lowest reprojection error)
        SmartDashboard.putNumber("Transform3d X ", bestCameraToTarget.getX());
        SmartDashboard.putNumber("Transform3d y ", bestCameraToTarget.getY());
        SmartDashboard.putNumber("Transform3d rotation ", bestCameraToTarget.getRotation().getAngle());

        targetVisible = true;
        targetYaw = target.getYaw();
        Transform3d camToTarget = target.getBestCameraToTarget();
        double x = camToTarget.getTranslation().getX(); // forward
        double y = camToTarget.getTranslation().getY(); // sideways

        // planar distance ignoring vertical (Z)
        distToTarget = Math.sqrt(x*x + y*y);

        SmartDashboard.putNumber("Target ID", target.getFiducialId());
        SmartDashboard.putNumber("Target Yaw", target.getYaw());
        SmartDashboard.putBoolean("Target Found", targetVisible);
      } else{
        targetVisible = false;
        SmartDashboard.putBoolean("Target Found", targetVisible);
      }
    } catch (Exception e){
      System.out.println("No Target Found: " + e);
    }
    
  }
  public double getYaw(){
    SmartDashboard.putNumber("Vision - Yaw", targetYaw);
    return targetYaw;
  }

  public double getDistanceToTarget(){
    return distToTarget;
  }

  public double getPowerOutputToDistance(double distToTarget, double currDist){
    PIDController distancePID = new PIDController(0.002, 0, 0);
    double output = distancePID.calculate(distToTarget, currDist);
    return output;
  }

  public Boolean hasTarget(){
    return targetVisible;
  }
}
