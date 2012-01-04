package org.specs2.springexample;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author janmachacek
 */
@Entity
public class Rider {
	@Id
	@GeneratedValue
	private Long id;
	@Version
	private int version;
	@NotNull
	private String name;
	private String teamName;
	@NotNull
	@Size(min = 2, max = 100)
	private String username;
	private int age;
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Entry> entries = new HashSet<Entry>();

	public void addEntry(Entry entry) {
		entry.setRider(this);
		this.entries.add(entry);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Set<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Set<Entry> entries) {
		this.entries = entries;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Rider");
		sb.append("{id=").append(id);
		sb.append(", version=").append(version);
		sb.append(", username='").append(username).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append(", teamName='").append(teamName).append('\'');
		sb.append(", age=").append(age);
		sb.append(", entries=").append(entries);
		sb.append('}');
		return sb.toString();
	}
}
