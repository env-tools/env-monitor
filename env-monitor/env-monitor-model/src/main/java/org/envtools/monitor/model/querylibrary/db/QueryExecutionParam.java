package tables;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;

@Entity
@Table(name="QUERYEXECUTIONPARAMETERS")
public class QueryExecutionParam extends AbstractDbIdentifiable {
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private String value;

    //connection to table
    private QueryExecution queryExecution;
    @ManyToOne
    @JoinColumn(name = "id")
    public QueryExecution getQueryExecution(){
        return queryExecution;
    }

    public QueryExecutionParam() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
