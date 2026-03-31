package com.taskengine.taskengine_task_service.constant;

public final class Constant {


    public static final String INVALID_ATTRIBUTES = "Invalid Attribute";
    public static final String ASSIGNED_CREATED_USER_CAN_EDIT = "Assigned and Created User can only edit the task";
    public static final String REMARK_BLANK = "Remark field should not be blank";
    public static final String REMARK_UPDATED ="Remark has been updated by: " ;
    public static final String COMPLETED_TO_REOPEN ="Completed Task can only move to REOPEN status" ;
    public static final String STATUS_NOT_BACK_TO_CREATED = "Task cannot be moved back to CREATED";
    public static final String TASK_CLAIMED_OR_ACCEPTED = "Task must be claimed or accepted before changing status";
    public static final String  TASK_CANCELLED = "Cancelled task cannot be modified";
    public static final String TABLE_EMPTY = "Table is Empty";
    public static final String USER_NOT_ACTIVE = "User is not Active";
    public static final String USER_AUTHENTICATED = "User is not authenticated";
    public static final String TASK_ACTIVE = "Task is Active";
    public static final String TASK_ACTIVATED = "Task has been Activated";
    public static final String TASK_DEACTIVATED = "Task has been De-Activated";
    public static final String TASK_ASSIGNED = "Task has been assigned to";
    public static final String TASK_ALREADY_ASSIGNED = "Task has been already assigned to";
    public static final String  USER_NOT_BLANK = "Assigned user email cannot be blank";
    public static final String TASK_CLAIMED = "Task has been claimed by: ";
    public static final String TASK_ALREADY_ACCEPETED = "Task already accepted by: ";
    public static final String TASK_ACCEPETED_BY = "Task has been accepted by";
    public static final String DEPENDENCY_BLANK = "Dependency list is blank";
    public static final String DEPENDENCY_UPDATED = "Dependency has been updated ";

    private Constant(){}





    public static final String CANCELLED_TASK_CANT_EDIT ="Cancelled Task cannot be edited" ;
    public static final String TASK_STATUS = "Task Status is :";
    public static final String UPDATED = "Task Details have been updated";
    public static final String TASK_CREATOR_CAN_EDIT = "Task Creator can only edit the task";
    public static final String TASK_BLOCKED = "Task is Blocked Due to this dependent Task ";
    public static final String TASK_INACTIVE = "Task is not Active";
    public static final String NO_DEPENDENCY = "No Dependency found you can proceed with priority basis !!";
    public static final String CIRCULAR_DEPENDENCY = "Found Circular Dependency";

}
