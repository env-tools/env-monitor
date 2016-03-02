package tables;

/**
 * Created by jesa on 25.02.2016.
 */

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name="QUERY")
public class Query extends AbstractDbIdentifiable {
    @Column(name = "TEXT")
    private String text;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;

    //connection to tables
    private ArrayList<QueryParam> queryParams = new ArrayList<QueryParam>(0);
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    public ArrayList<QueryParam> getQueryParams(){
        return queryParams;
    }

    private ArrayList<QueryExecution> queryExecutions = new ArrayList<QueryExecution>(0);
    @OneToMany
    @JoinColumn(name = "id")
    public ArrayList<QueryExecution> getQueryExecutions(){
        return queryExecutions;
    }

    private Category category;
    @ManyToOne
    @JoinColumn(name = "id")
    public Category getParentCategory(){
        return category;
    }

    public Query() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
