package tables;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="QUERYEXECUTION")
public class QueryExecution extends AbstractDbIdentifiable {
    @Column(name = "USER")
    private String user;
    @Column(name = "STARTTIMESTAMP")
    private Timestamp startTimestamp;
    @Column(name = "ENDTIMESTAMP")
    private Timestamp endTimestamp;

    //connection to tables
    private Query query;
    @ManyToOne
    @JoinColumn(name = "id")
    public Query getQuery(){
        return query;
    }

    private Set<QueryExecutionParam> queryExecutionParams = new HashSet<QueryExecutionParam>(0);
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    public Set<QueryExecutionParam> getQueryExecutionParams(){
        return queryExecutionParams;
    }

    private DataSource dataSource;
    @OneToOne
    @JoinColumn(name = "id")
    public DataSource dataSource(){
        return dataSource;
    }

    public QueryExecution() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Timestamp startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
