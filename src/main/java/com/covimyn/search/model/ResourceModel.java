/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResourceModel {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private long category;

    @NonNull
    private long subcategory;

    private String address;

    private String pincode;

    private String description;

    @NonNull
    private String phone1;

    private String phone2;

    private String email;

    @NonNull
    private long city;

    @NonNull
    private long state;

    @NonNull
    private boolean isAvailable;

    private String createdBy;

    private String createdAt;

    private String updatedBy;

    @NonNull
    private String updatedAt;

    @NonNull
    private boolean isVerified;
}
