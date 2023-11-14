class DevProfile {
    private String email;
    private String password;

    DevProfile(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public DevProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DevProfile setPassword(String password) {
        this.password = password;
        return this;
    }
}

class DevProfileMother {
    static DevProfile alice() {
        return new DevProfile("alice@gmail.com", "qwerty");
    }

    static DevProfile bob() {
        return new DevProfile("bob@example.net", "12345");
    }

    static DevProfile carol() {
        return new DevProfile("carol@dev.org", "secret");
    }

    static DevProfile dave() {
        return new DevProfile("dangerousdave@idsw.com", "secret");
    }

    static DevProfile withBadEmail(String email) {
        var password = String.valueOf(System.currentTimeMillis());
        return new DevProfile(email, password);
    }

    static DevProfile withBadPassword(String password) {
        var email = "user-" + System.currentTimeMillis();
        return new DevProfile(email, password);
    }
}
