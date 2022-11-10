package net.therap.mealsystem.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author aladin
 * @since 3/13/22
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Persistent implements Serializable {

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false)
    Date created;

    @Temporal(value = TemporalType.TIMESTAMP)
    Date updated;

    @Version
    int version;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date d) {
        created = d;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date d) {
        updated = d;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int v) {
        version = v;
    }
}
