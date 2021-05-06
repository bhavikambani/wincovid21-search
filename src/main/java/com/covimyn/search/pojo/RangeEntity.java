/**
 * Created by Avishek Gurung on 2021-05-05
 */

package com.covimyn.search.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RangeEntity {

    /**
     * We will support two params for range query. GTE and LTE.
     */

    public RangeEntity(String field) {
        this.field = field;
    }

    private String field;
    private String param1;
    private String param2;
    private String value1;
    private String value2;

}
