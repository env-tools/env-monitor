package tables;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;

@Entity
@Table(name="DATASOURCEPROPERTIES")
public class DataSourceProperties extends AbstractDbIdentifiable {
    @Column(name = "PROPERTY")
    private String property;
    @Column(name = "VALUE")
    private String value;

    //connection to table
    private DataSource dataSource;
    @ManyToOne
    @JoinColumn(name = "id")
    public DataSource getDataSource(){
        return dataSource;
    }

    public DataSourceProperties() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
