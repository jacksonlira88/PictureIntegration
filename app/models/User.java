package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
public class User extends GenericModel {

    @Id
    //@GeneratedValue
	public String id;
	public String email;
	
}
