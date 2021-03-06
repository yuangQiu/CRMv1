package com.cdsxt.service;

import com.cdsxt.po.Complaint;
import com.cdsxt.po.HandleMethod;
import com.cdsxt.po.User;

import java.util.Date;
import java.util.List;

public interface ComplaintService {
    void add(Complaint complaint);

    List<Complaint> queryAllWithStatus(String status);

    List<User> queryUserByRoleConstant(String roleConstant);

    Complaint queryComplaintById(Integer complaintId);

    void appointAfterSales(Complaint complaint, Integer afterSalesId);

    void handleComplaint(Complaint complaint, String handleMethod);

    void handleCallback(Complaint complaint, Boolean idDone, HandleMethod handleMethod, Date callbackDate);
}
