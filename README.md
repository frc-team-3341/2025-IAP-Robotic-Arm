# 2025-IAP-Robotic-Arm

## DriveToTarget Mark 1 (Nishk 12/5)
I wrote a command that finds gets the yaw of the AprilTag in order to tell the robot how much to turn. I use the PIDTurn method to figure out the motor output. Once the yaw is within +-5 degrees of 0, we change the variable "isAligned" to true to tell the command to stop and set the motors to 0. 
