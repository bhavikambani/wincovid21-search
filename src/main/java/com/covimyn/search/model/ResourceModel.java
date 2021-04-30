/**
 * Created by Avishek Gurung on 2021-04-30
 */

package com.covimyn.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceModel {

    @NonNull
    private String id;

    @NonNull
    private String name;

    private String comment;

    private String address;

    private String district;

    private String email;

    @NonNull
    private String pincode;

    @NonNull
    private String city;

    @NonNull
    private String state;

    private String phone;

    private String location;

    @NonNull
    private boolean isVerified;

    @NonNull
    private String resourceType;

    @NonNull
    private boolean isAvailable;

    private String feedback;

    private String createdBy;

    private String updatedBy;

    private String createdAt;

    @NonNull
    private String updatedAt;

}
