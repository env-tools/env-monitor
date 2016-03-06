package org.envtools.monitor.model.querylibrary.db;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;


@MappedSuperclass
public class AbstractDbIdentifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = " QueryLibSeqGenerator")
    @SequenceGenerator(name="QueryLibSeqGenerator",sequenceName="QUERY_LIB_SEQUENCE", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    public AbstractDbIdentifiable() {
    }

    public AbstractDbIdentifiable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
