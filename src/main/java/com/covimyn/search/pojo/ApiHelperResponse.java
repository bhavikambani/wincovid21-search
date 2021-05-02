/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.pojo;

import lombok.*;

@Data
@AllArgsConstructor
public class ApiHelperResponse {

    private int statusCode;
    private String reasonPhrase;
    private Object payload;

}
