package edu.upc.eetac.dsa.walka.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by SergioGM on 05.12.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalkaError {

    private int status;
    private String reason;

    public WalkaError() {
    }

    public WalkaError(int status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
