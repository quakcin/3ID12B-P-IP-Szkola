package pl.hefajstos.hefajstos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "uczen")
public class Student {
    String id;
    String Imie;
    String Nazwisko;

    Student(String id, String imie, String nazwisko){
        this.id = id;
        this.Imie = imie;
        this.Nazwisko = nazwisko;
    }

    public Student() {

    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }
}
