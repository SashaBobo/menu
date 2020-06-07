package menu.menu_fridge.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String instruction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "recipe_products",
            joinColumns = {@JoinColumn(name = "recipe_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private List<Product> productsInRecipe = new ArrayList<>();

    public String getAuthorName() {
        return author != null ? author.getUsername() : "";
    }
}
