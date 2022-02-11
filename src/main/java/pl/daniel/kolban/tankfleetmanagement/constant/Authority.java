package pl.daniel.kolban.tankfleetmanagement.constant;

public class Authority {
    public static final String[] PRESIDENT_AUTHORITIES = { "president:read", "tank:create", "tank:delete", "tank:update" };
    public static final String[] SUPER_PRESIDENT_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete", "president:delete" };
}
