package com.covimyn.search.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class StatusResponse {

    private static final long serialVersionUID = 5521850192852967435L;
    private StatusResponse.Type statusType;
    private long totalCount;

    public StatusResponse() {
        this.statusType = StatusResponse.Type.SUCCESS;
    }

    public StatusResponse(StatusResponse.Type type) {
        this.statusType = StatusResponse.Type.SUCCESS;
        this.statusType = type;
    }

    public StatusResponse(StatusResponse.Type type, long totalCount) {
        this.statusType = StatusResponse.Type.SUCCESS;
        this.statusType = type;
        this.totalCount = totalCount;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.statusType == StatusResponse.Type.SUCCESS;
    }

    public static enum Type {
        ERROR,
        SUCCESS,
        WARNING;

        private Type() {
        }
    }

}
