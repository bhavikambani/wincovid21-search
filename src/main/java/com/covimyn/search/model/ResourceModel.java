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
    private String category;

    @NonNull
    private String subcategory;

    @NonNull
    private String city;

    @NonNull
    private String state;

    @NonNull
    private long categoryId;

    @NonNull
    private long subcategoryId;

    private String address;

    private String pincode;

    private String description;

    @NonNull
    private String phone1;

    private String phone2;

    private String email;

    @NonNull
    private long cityId;

    @NonNull
    private long stateId;

    @NonNull
    private boolean isAvailable;

    private String createdBy;

    private String createdAt;

    private String updatedBy;

    @NonNull
    private String updatedAt;

    @NonNull
    private boolean isVerified;

    @NonNull
    private boolean isValid;
}
