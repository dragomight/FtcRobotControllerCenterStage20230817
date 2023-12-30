package org.firstinspires.ftc.teamcode.BillsYarm;

public enum YarmState {
    READY_PLACE, // Driver 2 holds lb -> drivetrain and arm move into ready position for placement
    // after first pixel is placed (driver 2 presses button to clear item from que?), moves to next queued position
    PLACE, // While holding lb press button to place // Disabled when arm is not ready
    FOLD, // Both joints are set to 0
    READY_HANG, // Driver 2 holds rb -> arm moves to hanging position then straight up to ready
    HANG // hook moves in straight line down from READY_HANG
}
