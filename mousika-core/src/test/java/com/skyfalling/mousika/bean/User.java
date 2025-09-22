package com.skyfalling.mousika.bean;

import lombok.*;

/**
 * @author skyfalling {@literal <skyfalling@live.com>}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @NonNull
    private String name;
    @NonNull
    private int age;
    private Contact contact;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Contact {
        String email;
        String phone;
    }
}