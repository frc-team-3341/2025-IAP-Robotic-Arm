// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Vision extends SubsystemBase {
    private final PhotonCamera bottomCamera = new PhotonCamera(Constants.VisionConstants.CameraName);

  /** Creates a new Vision. */
  public Vision() {
    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var result = bottomCamera.getLatestResult();
    List<PhotonTrackedTarget> targets = result.getTargets();
    PhotonTrackedTarget bestTarget = result.getBestTarget();

    double yaw = bestTarget.getYaw();
    double pitch = bestTarget.getPitch();
    double area = bestTarget.getArea();
    double skew = bestTarget.getSkew();

    SmartDashboard.putNumber("Yaw:", yaw);                                               

  }
}
