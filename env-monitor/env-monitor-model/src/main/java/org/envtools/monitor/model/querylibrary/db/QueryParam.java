package tables;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;

@Entity
@Table(name="QUERYPARAMETERS")
public class QueryParam extends AbstractDbIdentifiable {
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;

    //connection to table
    private Query query;
    @ManyToOne
    @JoinColumn(name = "id")
    public Query getQuery(){
        return query;
    }

    public QueryParam() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
