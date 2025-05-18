package org.jsp.jsp_19_sgnr.dto;

public class Member {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String status;      // 상태 (요청 ST00, 정상 ST01, 해지 ST02, 일시정지 ST03)
    private String userType;    // 사용자 구분 코드 (일반 10, 관리자 20)

    public Member() {
    }

    public Member(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = "ST00";
        this.userType = "10";
    }

    public Member(String email, String password, String name, String phone, String status, String userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
