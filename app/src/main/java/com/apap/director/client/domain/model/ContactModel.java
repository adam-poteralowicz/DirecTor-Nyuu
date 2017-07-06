package com.apap.director.client.domain.model;

import java.util.List;

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
    private List<ContactKeyModel> contactKeys;
    private AccountModel owner;
    private String image;
}
