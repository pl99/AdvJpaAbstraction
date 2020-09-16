package ru.advantum.common.abstraction;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    static final long serialVersionUID = 1L;
}
