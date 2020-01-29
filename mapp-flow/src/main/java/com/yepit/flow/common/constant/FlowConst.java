package com.yepit.flow.common.constant;

/**
 * Created by Administrator on 2018/11/6.
 */
public class FlowConst {
    public static String FLOW_KEYID = "carRental";
    public interface FlowNode {
        public static String FLOW_START = "开始";
        public static String UPLOAD_CUSTOMER_APPLY = "提交客户申请";
        public static String MODIFY_CUSTOMER_APPLY = "修改客户申请";
        public static String FIRST_TRIAL = "初审";
        public static String SECOND_TRIAL = "复审";
        public static String FACE_TRIAL= "面审";
        public static String NOTIFY_FACE_SIGN= "预约面签";
        public static String FACE_INTERVIEW="面签";
        public static String APPOINTMENT_VIST= "预约家访";
        public static String HOME_VISTING= "家访";
        public static String INSURANCE_AFFIRM= "保险确认";
        public static String CAR_CONFIRM= "车辆确认";
        public static String FINAL_JUDGMENT= "终审";
        public static String SEND_CAR= "提车";
        public static String INSTALL_GPRS= "安装GPRS";
        public static String REFUND= "退款";
        public static String MAKE_LOANS= "下款";
        public static String SEND_CAR_CM= "交付车辆";
        public static String FLOW_END= "完成";
    }
    public interface FlowNodeId {
        public static String FLOW_START = "Start";
        public static String UPLOAD_CUSTOMER_APPLY = "UploadCustomerApply";
        public static String FIRST_TRIAL= "firstTrial";
        public static String SECOND_TRIAL= "secondTrial";
        public static String FACE_TRIAL= "faceTrial";
        public static String NOTIFY_FACE_SIGN= "notifyFaceSign";
        public static String FACE_INTERVIEW="faceInterview";
        public static String APPOINTMENT_VIST= "appointmentVist";
        public static String HOME_VISTING= "homeVisting";
        public static String INSURANCE_AFFIRM= "insuranceAffirm";
        public static String CAR_CONFIRM= "carConfirm";
        public static String FINAL_JUDGMENT= "finalJudgment";
        public static String SEND_CAR= "sendcar";
        public static String INSTALL_GPRS= "installGPRS";
        public static String REFUND= "refund";
        public static String MAKE_LOANS= "makeLoans";
        public static String SEND_CAR_CM= "sendCardCustomerManager";
    }

    public interface FlowNodeRel {

    }

    public interface MileStoneStatus {
        public static String START = "-1";//-1 未开始
        public static String DOING = "0";//处理中
        public static String END = "1";//处理完成
    }
    public interface BusTaskStatus {
        public static String START = "0";//刚启动，暂时没提交客户资料
        public static String CREATE = "1";//已上传资料提交申请
        public static String END = "2";//处理完成
    }
    public interface FlowVariable  {
        public static String RESULT = "result";
        public static String USERID = "userId";
        public static String USERNAME="userName";
        public static String BUSID="busId";

    }
}
