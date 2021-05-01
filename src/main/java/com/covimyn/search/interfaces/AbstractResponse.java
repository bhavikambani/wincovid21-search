package com.covimyn.search.interfaces;

import java.io.Serializable;

public abstract class AbstractResponse implements Serializable {
    private StatusResponse status;

    public AbstractResponse() {
    }

    public AbstractResponse(StatusResponse status) {
        this.status = status;
    }

    public StatusResponse getStatus() {
        return this.status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String toString() {
        return "AbstractResponse [status=" + this.status + "]";
    }
}