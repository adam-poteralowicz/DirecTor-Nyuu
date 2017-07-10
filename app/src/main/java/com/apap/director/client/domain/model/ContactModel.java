package com.apap.director.client.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alicja Michniewicz
 */
@Getter
@Setter
@NoArgsConstructor
public class ContactModel {

    private long id;
    private String name;
    private ContactKeyModel contactKey;
    private AccountModel owner;
    private String image;

}
