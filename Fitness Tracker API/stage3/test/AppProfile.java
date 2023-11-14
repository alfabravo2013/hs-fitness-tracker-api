class AppProfile {
    private String name;
    private String description;

    AppProfile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public AppProfile setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class AppProfileMother {
    public static AppProfile demo1() {
        return new AppProfile(
                "Demo App 1",
                "In sit amet posuere magna. Cras cursus tincidunt."
        );
    }

    public static AppProfile demo2NoDescription() {
        return new AppProfile(
                "Demo App 2",
                ""
        );
    }

    public static AppProfile noName() {
        return new AppProfile(
                null,
                "abcde"
        );
    }
}
