package com.cc.polyhx2020;

public class Helper extends User{

    private String certificationId;

    // CONSTRUCTOR
    public Helper(String firstName, String lastName, String dateOfBirth, String phoneNumber,
                  String email, String password, String certificationId) {
        super(firstName, lastName, dateOfBirth, phoneNumber, email, password);
        this.certificationId = certificationId;
    }

    // GETTERS & SETTERS
    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

}
