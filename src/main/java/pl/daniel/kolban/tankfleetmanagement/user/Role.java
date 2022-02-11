package pl.daniel.kolban.tankfleetmanagement.user;


import static pl.daniel.kolban.tankfleetmanagement.constant.Authority.*;

public enum Role {
    ROLE_PRESIDENT(PRESIDENT_AUTHORITIES),
    ROLE_SUPER_PRESIDENT(SUPER_PRESIDENT_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
