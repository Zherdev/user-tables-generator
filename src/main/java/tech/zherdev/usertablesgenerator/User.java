/*
 * User
 *
 * Ivan Zherdev, 2019
 */
package tech.zherdev.usertablesgenerator;

/**
 * Класс User предназначен для хранения пользовательских данных.
 *
 * @author Ivan Zherdev
 */
public class User {

    private String firstName;
    private String lastName;
    private String patrName;
    private int age;
    private String gender;
    private CustomDate birthday;
    private String inn;
    private int zip;
    private String country;
    private String region;
    private String city;
    private String street;
    private int building;
    private int appart;

    /** Неполный конструктор */
    User(String firstName, String lastName, String patrName, int age, String gender,
         CustomDate birthday, String region,
         String city, String street, int building) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.patrName = patrName;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
        this.region = region;
        this.city = city;
        this.street = street;
        this.building = building;
    }

    /** Полный констркутор */
    User(String firstName, String lastName, String patrName, int age, String gender,
         CustomDate birthday, String inn, int zip, String country, String region,
         String city, String street, int building, int appart) {

        this(firstName, lastName, patrName, age, gender, birthday, region, city,
                street, building);

        this.inn = inn;
        this.country = country;
        this.appart = appart;
        this.zip = zip;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setAppart(int appart) {
        this.appart = appart;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPatrName() {
        return this.patrName;
    }

    public CustomDate getBirthday() {
        return this.birthday;
    }

    public String getGender() {
        return this.gender;
    }

    public String getInn() {
        return this.inn;
    }

    public int getZip() {
        return this.zip;
    }

    public String getCountry() {
        return this.country;
    }

    public String getRegion() {
        return this.region;
    }

    public String getCity() {
        return this.city;
    }

    public String getStreet() {
        return this.street;
    }

    public int getBuilding() {
        return this.building;
    }

    public int getAppart() {
        return this.appart;
    }

    /** @return массив атрибутов пользователя */
    public Object[] getValues() {
        Object[] result = new Object[14];
        result[0] = firstName;
        result[1] = lastName;
        result[2] = patrName;
        result[3] = age;
        result[4] = gender;
        result[5] = birthday;
        result[6] = inn;
        result[7] = zip;
        result[8] = country;
        result[9] = region;
        result[10] = city;
        result[11] = street;
        result[12] = building;
        result[13] = appart;

        return result;
    }

    /** @return массив атрибутов пользователя в строковом представлении */
    public String[] toStringArray() {
        String[] result = new String[14];
        Object[] attrs = getValues();

        for(int i = 0; i < result.length; i++) {
            result[i] = attrs[i].toString();
        }

        return result;
    }

}
