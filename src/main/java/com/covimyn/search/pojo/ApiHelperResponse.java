/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiHelperResponse {

    private int statusCode;
    private String reasonPhrase;
    private Object payload;

}
