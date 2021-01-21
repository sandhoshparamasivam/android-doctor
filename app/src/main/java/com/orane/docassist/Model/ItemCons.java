package com.orane.docassist.Model;

public class ItemCons {
    private String id;
    private String patient;
    private String notes;
    private String status;
    private String apptdt;
    private String appttype;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //----------------------------------------------
    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPatient() {
        return patient;
    }

    //--------------------------------------------------------
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    //--------------------------------------------------------
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
    //--------------------------------------------------------
    //--------------------------------------------------------
    public void setAppttype(String appttype) {
        this.appttype = appttype;
    }

    public String getAppttype() {
        return appttype;
    }
    //--------------------------------------------------------




    //--------------------------------------------------------
    public void setApptdt(String apptdt) {
        this.apptdt = apptdt;
    }

    public String getApptdt() {
        return apptdt;
    }
    //--------------------------------------------------------
}
