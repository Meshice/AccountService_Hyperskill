package account.service;

public enum Event {

    CREATE_USER("A user has been successfully registered"),
    CHANGE_PASSWORD("A user has changed the password successfully"),
    ACCESS_DENIED("A user is trying to access a resource without access rights"),
    LOGIN_FAILED("Failed authentication"),
    GRANT_ROLE("Grant role %s to %s"),
    REMOVE_ROLE("Remove role %s from %s"),
    LOCK_USER("The Administrator has locked the user"),
    UNLOCK_USER("The Administrator has unlocked a user"),
    DELETE_USER("The Administrator has deleted a user"),
    BRUTE_FORCE("A user has been blocked on suspicion of a brute force attack");

    private final String message;

    Event(String message) {
        this.message = message;
    }

    public String getEventMessage() {
        return message;
    }
}
