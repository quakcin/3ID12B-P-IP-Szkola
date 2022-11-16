package pl.hefajstos.hefajstos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "uczen")
public class Student {
    Integer id;
    String Imie;
    String Nazwisko;

    Student(Integer id, String imie, String nazwisko){
        this.id = id;
        this.Imie = imie;
        this.Nazwisko = nazwisko;
    }

    public Student() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    public Integer getId() {
        return id;
    }
}
