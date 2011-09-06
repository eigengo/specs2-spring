package org.specs2.springexample;

import javax.persistence.*;
import java.util.Date;

/**
 * @author janmachacek
 */
@Entity
public class Entry {
	@Id
	@GeneratedValue
	private Long id;
	@Version
	private int version;
	@ManyToOne
	private Rider rider;
	private Date time;
	private int number;

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

	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Entry");
		sb.append("{id=").append(id);
		sb.append(", version=").append(version);
		sb.append(", time=").append(time);
		sb.append(", number=").append(number);
		sb.append('}');
		return sb.toString();
	}
}
