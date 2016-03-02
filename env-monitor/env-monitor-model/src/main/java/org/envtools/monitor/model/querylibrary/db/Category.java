package tables;

/**
 * Created by jesa on 25.02.2016.
 */
import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name="CATEGORY")
public class Category extends AbstractDbIdentifiable {
    @Column(name = "OWNER")
    private String owner;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;

    //connection to tables
    private ArrayList<Query> queries = new ArrayList<Query>(0);
    @OneToMany
    @JoinColumn(name = "id")
    public ArrayList<Query> getQueries(){
        return queries;
    }

    private ArrayList<Category> categories = new ArrayList<Category>(0);
    @OneToMany
    @JoinColumn(name = "id")
    public ArrayList<Category> getChildCategories(){
        return categories;
    }

    private Category category;
    @ManyToOne
    @JoinColumn(name = "id")
    public Category getParentCategory(){
        return category;
    }

    public Category() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
