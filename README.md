# 2025-IAP-Robotic-Arm

## DriveToTarget Mark 1 (Nishk 12/5 - time of writing)
I wrote a command that finds gets the yaw of the AprilTag in order to tell the robot how much to turn. I use the PIDTurn method to figure out the motor output. Once the yaw is within +-5 degrees of 0, we change the variable "isAligned" to true to tell the command to stop and set the motors to 0. 
## DriveToTarget Mark 2 (Nishk 12/7 & 12/13)
The algorithm for DriveToTarget (turning aspect for now) should turn at a fixed power output to the motors and stop (60 deg) angle intervals wait for a few seconds, checking to see if an AprilTag is detected (yet to be implemented). We have to do this because the GSC can't detect the AprilTag while turning. Then, I have the ranging part of DriveToTarget that should get the distance away from the AprilTag and drive towards it using a PID controller and encoders (yet to be tested). 
