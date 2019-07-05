package app.model;

public class Student {
	private Integer id;
	private String name;
	private String email;
	private int gender;

	public Student() {
	}

	public Student(String name, String email, int gender) {
		this.name = name;
		this.email = email;
		this.gender = gender;
	}

	public Student(int id, String name, String email, int gender) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.gender = gender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void display() {
		System.out.println("Sudent Infor:");
		System.out.println("\t Id: " + getId());
		System.out.println("\t Name: " + getName());
		System.out.println("\t Email: " + getEmail());
		System.out.println("\t Gender: " + getGender());
		System.out.println("**** =++++= ****");

	}

}
