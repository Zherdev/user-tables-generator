package tech.zherdev.usertablesgenerator;

import java.util.GregorianCalendar;

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

    User(String firstName, String lastName, String patrName, int age, String gender,
         CustomDate birthday, int zip, String region,
         String city, String street, int building) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patrName = patrName;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
        this.zip = zip;
        this.region = region;
        this.city = city;
        this.street = street;
        this.building = building;
    }

    User(String firstName, String lastName, String patrName, int age, String gender,
         CustomDate birthday, String inn, int zip, String country, String region,
         String city, String street, int building, int appart) {

        this(firstName, lastName, patrName, age, gender, birthday, zip, region, city,
                street, building);

        this.inn = inn;
        this.country = country;
        this.appart = appart;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public void setAppart(int appart) {
        this.appart = appart;
    }

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

}
